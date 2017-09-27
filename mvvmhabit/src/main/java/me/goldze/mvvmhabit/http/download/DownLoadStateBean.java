package me.goldze.mvvmhabit.http.download;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by goldze on 2017/5/11.
 */

public class DownLoadStateBean implements Serializable, Parcelable {
    long total; //  文件总大小
    long bytesLoaded; //已加载文件的大小
    String tag; // 多任务下载时的一个标记

    public DownLoadStateBean(long total, long bytesLoaded) {
        this.total = total;
        this.bytesLoaded = bytesLoaded;
    }

    public DownLoadStateBean(long total, long bytesLoaded, String tag) {
        this.total = total;
        this.bytesLoaded = bytesLoaded;
        this.tag = tag;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBytesLoaded() {
        return bytesLoaded;
    }

    public void setBytesLoaded(long bytesLoaded) {
        this.bytesLoaded = bytesLoaded;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.total);
        dest.writeLong(this.bytesLoaded);
        dest.writeString(this.tag);
    }

    protected DownLoadStateBean(Parcel in) {
        this.total = in.readLong();
        this.bytesLoaded = in.readLong();
        this.tag = in.readString();
    }

    public static final Creator<DownLoadStateBean> CREATOR = new Creator<DownLoadStateBean>() {
        @Override
        public DownLoadStateBean createFromParcel(Parcel source) {
            return new DownLoadStateBean(source);
        }

        @Override
        public DownLoadStateBean[] newArray(int size) {
            return new DownLoadStateBean[size];
        }
    };
}
