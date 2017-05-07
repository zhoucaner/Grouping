package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/5/4.
 */

public interface UpdatePasswdAPI {
    @GET("updatePassword.php")
    Observable<Result>updatePasswd(@Query("cID")String cID, @Query("cPasswd")String cPasswd);
}
