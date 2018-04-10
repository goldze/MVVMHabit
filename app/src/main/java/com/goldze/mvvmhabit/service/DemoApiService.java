package com.goldze.mvvmhabit.service;

import com.goldze.mvvmhabit.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 曾宪泽 on 2017/6/15.
 */

public interface DemoApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoPost(@Field("token") String token);

    /**
     * 下载文件
     *
     * @param fileUrl 文件下载路径
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
