package com.goldze.mvvmhabit.ui.vm;

import com.goldze.mvvmhabit.entity.DemoEntity;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2017/7/17.
 */

public class DetailViewModel extends BaseViewModel {
    public DemoEntity.ItemsEntity entity;

    public DetailViewModel(DemoEntity.ItemsEntity entity) {
        this.entity = entity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
    }
}
