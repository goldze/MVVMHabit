package me.goldze.mvvmhabit.http;

/**
 * Created by goldze on 2017/5/10.
 */
public abstract class CallBack {
    public void onStart(){}

    public void onCompleted(){}

    abstract public void onError(Throwable e);

    public void onProgress(long fileSizeDownloaded){}

    abstract public void onSucess(String path, String name, long fileSize);
}
