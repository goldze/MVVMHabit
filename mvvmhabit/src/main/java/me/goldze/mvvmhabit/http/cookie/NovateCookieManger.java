package me.goldze.mvvmhabit.http.cookie;

import android.content.Context;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


/**
 * RetrofitClient
 * Created by Tamic on 2016-06-15.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */

public class NovateCookieManger implements CookieJar {

    private static final String TAG = "NovateCookieManger";
    private static Context mContext;
    private static PersistentCookieStore cookieStore;

    /**
     * Mandatory constructor for the NovateCookieManger
     */
    public NovateCookieManger(Context context) {
        mContext = context;
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(mContext);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

}
