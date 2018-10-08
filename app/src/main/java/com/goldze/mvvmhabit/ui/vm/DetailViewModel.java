package com.goldze.mvvmhabit.ui.vm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goldze.mvvmhabit.entity.DemoEntity;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2017/7/17.
 */

public class DetailViewModel extends BaseViewModel {
    public DemoEntity.ItemsEntity entity;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDemoEntity(DemoEntity.ItemsEntity entity) {
        this.entity = entity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
    }
}
