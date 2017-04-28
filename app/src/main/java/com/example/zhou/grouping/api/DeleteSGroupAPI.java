package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.DeleteSGroupResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/25.
 */

public interface DeleteSGroupAPI {
    @GET("deleteSGroup.php")
    Observable<DeleteSGroupResult> deleteSGroup(@Query("gID") String gid, @Query("sgID") String sgID);
}
