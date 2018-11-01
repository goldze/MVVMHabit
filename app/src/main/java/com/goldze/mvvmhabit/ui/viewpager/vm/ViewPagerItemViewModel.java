package com.goldze.mvvmhabit.ui.viewpager.vm;

import android.support.annotation.NonNull;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends ItemViewModel {
    public String text;
    public SingleLiveEvent<String> clickEvent = new SingleLiveEvent();

    public ViewPagerItemViewModel(@NonNull BaseViewModel viewModel, String text) {
        super(viewModel);
        this.text = text;
    }

    public BindingCommand onItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击之后将逻辑转到adapter中处理
            clickEvent.setValue(text);
        }
    });
}
