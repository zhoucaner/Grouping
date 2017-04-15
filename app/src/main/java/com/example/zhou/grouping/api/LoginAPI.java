package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.UserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/14.
 */

public interface LoginAPI {
    @GET("login.php")
    Observable<UserBean> login(@Query("cID")String cID);
}
