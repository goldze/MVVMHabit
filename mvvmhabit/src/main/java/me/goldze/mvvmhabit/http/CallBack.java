package me.goldze.mvvmhabit.http;

/**
 * Created by Tamic on 2016-08-02.
 */
public abstract class CallBack {
    public void onStart(){}

    public void onCompleted(){}

    abstract public void onError(Throwable e);

    public void onProgress(long fileSizeDownloaded){}

    abstract public void onSucess(String path, String name, long fileSize);
}
