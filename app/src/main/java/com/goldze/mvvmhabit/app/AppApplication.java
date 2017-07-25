package com.goldze.mvvmhabit.app;

import com.goldze.mvvmhabit.utils.RetrofitClient;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.Utils;

/**
 * Created by goldze on 2017/7/16.
 */

public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化工具类
        Utils.init(this);
        //初始化网络请求
        RetrofitClient.init(this, "http://www.oschina.net/");
        //开启打印日志
        KLog.init(true);
    }
}
