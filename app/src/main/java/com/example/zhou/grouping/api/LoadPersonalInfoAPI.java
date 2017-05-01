package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.LoadPersonalInfoResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/5/1.
 */

public interface LoadPersonalInfoAPI {
    @GET("loadPersonalInfo.php")
    Observable<LoadPersonalInfoResult> loadPersonalInfo(@Query("cID")String cID);
}
