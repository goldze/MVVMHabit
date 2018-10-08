package me.goldze.mvvmhabit.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * ItemViewModel
 * Created by goldze on 2018/10/3.
 */

public class ItemViewModel<VM extends BaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}
