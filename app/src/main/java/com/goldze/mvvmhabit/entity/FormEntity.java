package com.goldze.mvvmhabit.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

/**
 * Created by goldze on 2017/7/17.
 */

public class FormEntity extends BaseObservable implements Parcelable {
    private String id;
    private String name;
    private String sex;
    private String Bir;
    private String hobby;
    private Boolean isMarry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBir() {
        return Bir;
    }

    public void setBir(String bir) {
        Bir = bir;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public FormEntity() {
    }

    public Boolean getMarry() {
        return isMarry;
    }

    public void setMarry(Boolean marry) {
        isMarry = marry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.Bir);
        dest.writeString(this.hobby);
        dest.writeValue(this.isMarry);
    }

    protected FormEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.Bir = in.readString();
        this.hobby = in.readString();
        this.isMarry = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<FormEntity> CREATOR = new Creator<FormEntity>() {
        @Override
        public FormEntity createFromParcel(Parcel source) {
            return new FormEntity(source);
        }

        @Override
        public FormEntity[] newArray(int size) {
            return new FormEntity[size];
        }
    };
}
