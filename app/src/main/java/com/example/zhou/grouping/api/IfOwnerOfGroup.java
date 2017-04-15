package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/15.
 */

public interface IfOwnerOfGroup {
    @GET("selectIfOwnerOFGroup.php")
    Observable<Result> isOwner(@Query("gID") String gid, @Query("cID") String cID);
}
