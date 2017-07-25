package com.goldze.mvvmhabit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.goldze.mvvmhabit.BuildConfig;
import me.goldze.mvvmhabit.http.CallBack;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.cookie.CookieJarImpl;
import me.goldze.mvvmhabit.http.cookie.store.PersistentCookieStore;
import me.goldze.mvvmhabit.http.interceptor.BaseInterceptor;
import me.goldze.mvvmhabit.http.interceptor.CaheInterceptor;
import me.goldze.mvvmhabit.http.interceptor.Level;
import me.goldze.mvvmhabit.http.interceptor.LoggingInterceptor;
import me.goldze.mvvmhabit.utils.KLog;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    private static OkHttpClient okHttpClient;
    public static String baseUrl;
    private static Context mContext;
    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull Context context, String baseUrl) {
        RetrofitClient.mContext = context.getApplicationContext();
        RetrofitClient.baseUrl = baseUrl;
    }

    public static RetrofitClient getInstance() {
        return new RetrofitClient();
    }

    public static RetrofitClient getInstance(String url) {

        return new RetrofitClient(url);
    }

    public static RetrofitClient getInstance(String url, Map<String, String> headers) {
        return new RetrofitClient(url, headers);
    }

    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url) {
        this(url, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "goldze_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new LoggingInterceptor.Builder()
                        .loggable(BuildConfig.DEBUG)
                        .setLevel(Level.BASIC)
                        .log(Platform.INFO)
                        .request("Request")
                        .response("Response")
                        .addHeader("version", BuildConfig.VERSION_NAME).build()
                )
                .addNetworkInterceptor(new CaheInterceptor(mContext))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    /**
     * ApiBaseUrl
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        baseUrl = newApiBaseUrl;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     *
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }

    /**
     * DownSubscriber
     */
    class DownSubscriber<ResponseBody> extends Subscriber<ResponseBody> {
        CallBack callBack;

        public DownSubscriber(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (callBack != null) {
                callBack.onStart();
            }
        }

        @Override
        public void onCompleted() {
            if (callBack != null) {
                callBack.onCompleted();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (callBack != null) {
                callBack.onError(e);
            }
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            DownLoadManager.getInstance(callBack).writeResponseBodyToDisk(mContext, (okhttp3.ResponseBody) responseBody);

        }
    }
}
