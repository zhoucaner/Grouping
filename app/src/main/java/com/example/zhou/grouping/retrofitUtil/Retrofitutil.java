package com.example.zhou.grouping.retrofitUtil;

/**
 * Created by Zhou on 2017/4/14.
 */

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by falling on 2017/4/2.
 */

public class Retrofitutil {
    public static final String API_BASE_URL = "http://192.168.1.3/";

    private static Retrofit mRetrofit = new Retrofit.Builder().
            baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    public static Retrofit getmRetrofit() {
        return mRetrofit;
    }

    public static final String API_BASE_URL_JAVA = "http://192.168.1.3:8080/";

    private static Retrofit mRetrofitJAVA = new Retrofit.Builder().
            baseUrl(API_BASE_URL_JAVA)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    public static Retrofit getJAVAmRetrofit() {
        return mRetrofitJAVA;
    }

}