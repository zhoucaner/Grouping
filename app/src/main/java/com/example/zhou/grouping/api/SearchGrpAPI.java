package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.GroupBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/15.
 */

public interface SearchGrpAPI {
    @GET("loadGroupInfro.php")
    Observable<GroupBean> searchGrp(@Query("gID")String gID);
}
