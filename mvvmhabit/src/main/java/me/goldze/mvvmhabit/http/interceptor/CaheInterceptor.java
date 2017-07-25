package me.goldze.mvvmhabit.http.interceptor;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import me.goldze.mvvmhabit.http.NetworkUtil;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by goldze on 2017/5/10.
 * 无网络状态下智能读取缓存的拦截器
 */
public class CaheInterceptor implements Interceptor {

    private Context context;

    public CaheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtil.isNetworkAvailable(context)) {
            Response response = chain.proceed(request);
            // read from cache for 60 s
            int maxAge = 60;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "当前无网络! 为你智能加载缓存", Toast.LENGTH_SHORT).show();
                }
            });
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            //set cahe times is 3 days
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
