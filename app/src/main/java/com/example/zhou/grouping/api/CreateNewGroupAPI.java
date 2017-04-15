package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.Result;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Zhou on 2017/4/15.
 */

public interface CreateNewGroupAPI {
    @GET("createNewGroup.php")
    Observable<Result>createNewGroup(@QueryMap Map<String, String> options);
}
