package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.SelectIfOpen;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/5/7.
 */

public interface SelectIfOpenAPI {
    @GET("selectIfOpen.php")
    Observable<SelectIfOpen> selectIfOpen(@Query("gID")String gID);
}
