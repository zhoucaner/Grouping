package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.LoadSGroupMembers;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/22.
 */

public interface LoadSGroupMembersAPI {
    @GET("loadSGroupMembers.php")
    Observable<List<LoadSGroupMembers>> loadSGroupMembers(@Query("gID")String gID,@Query("sgID")String sgID);
}
