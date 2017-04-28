package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.LoadSGroup;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/21.
 */

public interface LoadSGroupAPI {
    @GET("loadSGroups.php")
    Observable<List<LoadSGroup>>loadSGroup(@Query("gID")String gID);
}
