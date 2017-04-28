package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.NumberResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/28.
 */

public interface SelectIfsgOwnerOfSGroupAPI {
    @GET("selectIfsgOwnerOfSGroup.php")
    Observable<NumberResult>selectIfsgOwnerOfSGroup(@Query("gID")String gID, @Query("cID")String cID);
}
