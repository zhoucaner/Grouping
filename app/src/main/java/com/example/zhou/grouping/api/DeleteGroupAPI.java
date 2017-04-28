package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.NumberResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/28.
 */

public interface DeleteGroupAPI {
    @GET("deleteGroup.php")
    Observable<NumberResult> deleteGroup(@Query("gID") String gid);
}
