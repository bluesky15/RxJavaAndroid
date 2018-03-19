package com.lkb.rxjava.android.rxjavaandroid;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.io.IOException;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by "lkb" on 3/19/2018.
 */

public class MainViewModel extends ViewModel {
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
}
