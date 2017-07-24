package com.goldze.mvvmhabit.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by goldze on 2017/7/17.
 */

public class DemoEntity {
    private String nextPageToken;
    private String prevPageToken;
    private int requestCount;
    private int responseCount;
    private int totalResults;
    private List<ItemsEntity> items;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public int getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(int responseCount) {
        this.responseCount = responseCount;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public static class ItemsEntity implements Parcelable{
        private String detail;
        private String href;
        private int id;
        private String img;
        private String name;
        private String pubDate;
        private int type;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.detail);
            dest.writeString(this.href);
            dest.writeInt(this.id);
            dest.writeString(this.img);
            dest.writeString(this.name);
            dest.writeString(this.pubDate);
            dest.writeInt(this.type);
        }

        public ItemsEntity() {
        }

        protected ItemsEntity(Parcel in) {
            this.detail = in.readString();
            this.href = in.readString();
            this.id = in.readInt();
            this.img = in.readString();
            this.name = in.readString();
            this.pubDate = in.readString();
            this.type = in.readInt();
        }

        public static final Creator<ItemsEntity> CREATOR = new Creator<ItemsEntity>() {
            @Override
            public ItemsEntity createFromParcel(Parcel source) {
                return new ItemsEntity(source);
            }

            @Override
            public ItemsEntity[] newArray(int size) {
                return new ItemsEntity[size];
            }
        };
    }
}
