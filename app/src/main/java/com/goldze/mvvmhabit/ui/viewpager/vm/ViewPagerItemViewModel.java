package com.goldze.mvvmhabit.ui.viewpager.vm;

import android.content.Context;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends BaseViewModel {
    public String text;

    public ViewPagerItemViewModel(Context context, String text) {
        super(context);
        this.text = text;
    }
}
