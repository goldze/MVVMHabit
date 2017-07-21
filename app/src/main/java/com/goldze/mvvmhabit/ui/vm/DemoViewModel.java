package com.goldze.mvvmhabit.ui.vm;

import android.content.Context;
import android.content.Intent;

import com.goldze.mvvmhabit.ui.activity.ContainerActivity;
import com.goldze.mvvmhabit.ui.fragment.NetWorkFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import rx.functions.Action0;

/**
 * Created by 曾宪泽 on 2017/7/17.
 */

public class DemoViewModel extends BaseViewModel {
    public DemoViewModel(Context context) {
        super(context);
    }

    public BindingCommand netWorkClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent();
            intent.setClass(context, ContainerActivity.class);
            intent.putExtra(ContainerActivity.FRAGMENT, NetWorkFragment.class.getCanonicalName());
            context.startActivity(intent);
        }
    });
}
