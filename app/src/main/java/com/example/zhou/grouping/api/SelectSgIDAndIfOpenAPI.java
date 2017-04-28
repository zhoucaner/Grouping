package com.example.zhou.grouping.api;

import com.example.zhou.grouping.httpBean.SelectSgIDAndIfOpen;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhou on 2017/4/22.
 */

public interface SelectSgIDAndIfOpenAPI {
    @GET("selectsgIDAndIfOpen.php")
    Observable<SelectSgIDAndIfOpen> selectSgIDAndIfOpen(@Query("gID")String gID, @Query("cID")String cID,@Query("sgID")String sgID);
}
