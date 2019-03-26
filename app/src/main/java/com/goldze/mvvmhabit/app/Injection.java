package com.goldze.mvvmhabit.app;

import android.annotation.SuppressLint;
import android.content.Context;

import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.data.source.LocalDataSource;
import com.goldze.mvvmhabit.data.source.http.HttpDataSourceImpl;
import com.goldze.mvvmhabit.data.source.http.service.DemoApiService;
import com.goldze.mvvmhabit.data.source.local.LocalDataSourceImpl;
import com.goldze.mvvmhabit.utils.RetrofitClient;

import io.reactivex.annotations.NonNull;

import static android.support.v4.util.Preconditions.checkNotNull;


/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 * Created by goldze on 2019/3/26.
 */
public class Injection {
    @SuppressLint("RestrictedApi")
    public static DemoRepository provideDemoRepository(@NonNull Context context) {
        //检查Context是否为空
        checkNotNull(context);
        //网络API服务
        DemoApiService apiService = RetrofitClient.getInstance().create(DemoApiService.class);
        return DemoRepository.getInstance(HttpDataSourceImpl.getInstance(apiService), LocalDataSourceImpl.getInstance());
    }
}
