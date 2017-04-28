package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.CreateNewSGroupResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Zhou on 2017/4/26.
 */

public interface CreateNewSGroupAPI {
    @GET("createNewSGroup.php")
    Observable<CreateNewSGroupResult> createNewSGroup(@QueryMap Map<String, String> options);
}
