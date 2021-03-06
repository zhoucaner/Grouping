package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.ExitFromSGroupResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Zhou on 2017/4/25.
 */

public interface ExitFromSGroupAPI {
    @GET("exitFromSGroup.php")
    Observable<ExitFromSGroupResult>exitFromSGroup(@QueryMap Map<String, String> options);
}
