package com.lkb.rxjava.android.rxjavaandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        DisposableSubscriber<String> s = new DisposableSubscriber<String>() {
            @Override
            public void onNext(String s) {
                okHttpResponse.setText(s);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error in network",t);
            }

            @Override
            public void onComplete() {

            }
        };
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
        _disposables.add(s);

    }

    @Nullable
    private String getTime() throws IOException {
        String result = "";
        String url = "http://time.jsontest.com/";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        result = response.body().string();

        return result;
    }

    public Flowable<String> getObservable() {
        return Flowable.defer(() -> {
            try {
                return Flowable.just(getTime());
            } catch (IOException e) {
                return Flowable.error(e);
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        _disposables.clear();
    }
}
