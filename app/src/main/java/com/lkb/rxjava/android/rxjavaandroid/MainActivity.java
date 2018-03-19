package com.lkb.rxjava.android.rxjavaandroid;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by "lkb" on 3/17/2018.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView okHttpResponse;
    private CompositeDisposable _disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        okHttpResponse = findViewById(R.id.okhttp_response);
        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
        DisposableObserver<String> s = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                okHttpResponse.setText(s);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error in network", t);
            }

            @Override
            public void onComplete() {

            }
        };
        model.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
        _disposables.add(s);

    }

    @Override
    protected void onPause() {
        super.onPause();
        _disposables.clear();
    }
}
