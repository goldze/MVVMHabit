package com.goldze.mvvmhabit.ui.network.detail;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.goldze.mvvmhabit.entity.DemoEntity;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2017/7/17.
 */

public class DetailViewModel extends BaseViewModel {
    public ObservableField<DemoEntity.ItemsEntity> entity = new ObservableField<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDemoEntity(DemoEntity.ItemsEntity entity) {
        this.entity.set(entity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
    }
}
