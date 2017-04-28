package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.NumberResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/28.
 */

public interface ExitFromGroupAndSGroupAPI {
    @GET("exitFromGroupAndSGroup.php")
    Observable<NumberResult> exitFromGroupAndSGroup(@Query("gID") String gid, @Query("cID") String cID);
}
