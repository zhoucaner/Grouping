package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.Result;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Zhou on 2017/5/3.
 */

public interface UpdatePersonalMsgAPI {
    @GET("updatePersonalMsg.php")
    Observable<Result>updatePersonalMsg(@QueryMap Map<String, String> options);
}
