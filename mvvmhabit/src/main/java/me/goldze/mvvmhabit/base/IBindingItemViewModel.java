package me.goldze.mvvmhabit.base;

/**
 * Created by 曾宪泽 on 2017/7/18.
 */

import android.databinding.ViewDataBinding;

public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
