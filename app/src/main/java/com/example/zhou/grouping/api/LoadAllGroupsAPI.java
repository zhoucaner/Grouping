package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.LoadGroups;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/15.
 */

public interface LoadAllGroupsAPI {
    @GET("loadProjects.php")
    Observable<List<LoadGroups>> searchGrp(@Query("cID")String cID);
}
