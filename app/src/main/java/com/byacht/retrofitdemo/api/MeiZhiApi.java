package com.byacht.retrofitdemo.api;

import com.byacht.retrofitdemo.MeiZhiData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by dn on 2017/3/29.
 */

public interface MeiZhiApi {

    @GET("/api/data/福利/10/{page}")
    Call<MeiZhiData> getMeiZhi(@Path("page") int page);

    @HTTP(method = "GET", path = "/api/data/福利/10/{page}", hasBody = false)
    Observable<MeiZhiData> getMeiZhiData(@Path("page") int page);
}
