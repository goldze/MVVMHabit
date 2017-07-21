package me.goldze.mvvmhabit.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import me.goldze.mvvmhabit.http.RetrofitClient;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by 曾宪泽 on 2017/6/19.
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //初始化工具类
        Utils.init(this);
        //初始化网络请求
        RetrofitClient.init(this);
        //开启打印日志
        KLog.init(true);
        registerActivityLifecycleCallbacks(mCallbacks);
    }

    /**
     * 获得当前app运行的AppContext
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.getAppManager().addActivity(activity);
            KLog.d(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            AppManager.getAppManager().removeActivity(activity);
            KLog.d(TAG, "onActivityStarted() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            KLog.d(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            KLog.d(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            KLog.d(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            KLog.d(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            KLog.d(TAG, "onActivityDestroyed() called with: activity = [" + activity + "]");
        }
    };
}
