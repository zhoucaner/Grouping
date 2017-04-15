package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.JoinInGroupResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Zhou on 2017/4/15.
 */

public interface JoinGroupAPI {
    @GET("joinInGroup.php")
    Observable<JoinInGroupResult> joinInGroup(@QueryMap Map<String, String> options);
}
