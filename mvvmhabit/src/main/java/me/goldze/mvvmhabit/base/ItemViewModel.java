package me.goldze.mvvmhabit.base;

import androidx.annotation.NonNull;

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
