package com.goldze.mvvmhabit.ui.vm;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.ui.fragment.DetailFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by goldze on 2017/7/17.
 */

public class NetWorkItemViewModel extends BaseViewModel {
    public DemoEntity.ItemsEntity entity;

    public NetWorkItemViewModel(Context context, DemoEntity.ItemsEntity entity) {
        super(context);
        this.entity = entity;
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            //跳转到详情界面,传入条目的实体对象
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("entity", entity);
            startContainerActivity(DetailFragment.class.getCanonicalName(), mBundle);
        }
    });

    /**
     *  可以在xml中使用binding:currentView="@{viewModel.titleTextView}" 拿到这个控件的引用, 但是不推荐这样做
     *
    private TextView tv;
    //将标题TextView控件回调到ViewModel中
    public BindingCommand<TextView> titleTextView = new BindingCommand(new Action1<TextView>() {
        @Override
        public void call(TextView tv) {
            NetWorkItemViewModel.this.tv = tv;
        }
    });
    */
}
