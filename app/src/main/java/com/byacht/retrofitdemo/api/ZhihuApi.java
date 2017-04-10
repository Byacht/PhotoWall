package com.byacht.retrofitdemo.api;


import com.byacht.retrofitdemo.ZhihuDailyData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import rx.Observable;

/**
 * Created by dn on 2017/4/8.
 */

public interface ZhihuApi {
    @GET("/api/4/news/latest")
    Call<ZhihuDailyData> getZhihu();

    @HTTP(method = "GET", path = "/api/4/news/latest", hasBody = false)
    Observable<ZhihuDailyData> getZhihuData();
}
