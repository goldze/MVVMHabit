package com.goldze.mvvmhabit.service;

import com.goldze.mvvmhabit.entity.DemoEntity;

import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by 曾宪泽 on 2017/6/15.
 */

public interface DemoApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoGet();
}
