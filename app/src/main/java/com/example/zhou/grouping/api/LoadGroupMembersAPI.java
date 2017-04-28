package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.GroupMembers;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/19.
 */

public interface LoadGroupMembersAPI {
    @GET("loadGroupMembers.php")
    Observable<List<GroupMembers>> loadGroupMembers(@Query("gID")String gID);
}
