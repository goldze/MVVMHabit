package com.goldze.mvvmhabit.ui.vm;

import com.goldze.mvvmhabit.databinding.FragmentDetailBinding;
import com.goldze.mvvmhabit.entity.DemoEntity;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by 曾宪泽 on 2017/7/17.
 */

public class DetailViewModel extends BaseViewModel {
    public DemoEntity.ItemsEntity entity;

    public DetailViewModel(DemoEntity.ItemsEntity entity) {
        this.entity = entity;
    }
}
