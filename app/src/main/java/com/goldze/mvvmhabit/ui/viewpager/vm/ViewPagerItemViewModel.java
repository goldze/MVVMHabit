package com.goldze.mvvmhabit.ui.viewpager.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.ui.vm.NetWorkItemViewModel;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

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
