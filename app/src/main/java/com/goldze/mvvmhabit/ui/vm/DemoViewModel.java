package com.goldze.mvvmhabit.ui.vm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.ui.activity.ContainerActivity;
import com.goldze.mvvmhabit.ui.fragment.FormFragment;
import com.goldze.mvvmhabit.ui.fragment.NetWorkFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import rx.functions.Action0;

/**
 * Created by goldze on 2017/7/17.
 */

public class DemoViewModel extends BaseViewModel {
    public DemoViewModel(Context context) {
        //要使用父类的context相关方法,记得加上这一句
        super(context);
    }

    //网络访问点击事件
    public BindingCommand netWorkClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent();
            intent.setClass(context, ContainerActivity.class);
            intent.putExtra(ContainerActivity.FRAGMENT, NetWorkFragment.class.getCanonicalName());
            context.startActivity(intent);
        }
    });
    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent();
            intent.setClass(context, ContainerActivity.class);
            intent.putExtra(ContainerActivity.FRAGMENT, FormFragment.class.getCanonicalName());
            context.startActivity(intent);
        }
    });
    //表单修改点击事件
    public BindingCommand formModifyClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            //模拟一个修改的实体数据
            FormEntity entity = new FormEntity();
            entity.setName("goldze");
            entity.setSex("1");
            entity.setBir("xxxx年xx月xx日");
            entity.setMarry(true);
            //传入实体数据
            Intent intent = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("entity", entity);
            intent.setClass(context, ContainerActivity.class);
            intent.putExtra(ContainerActivity.BUNDLE, mBundle);
            intent.putExtra(ContainerActivity.FRAGMENT, FormFragment.class.getCanonicalName());
            context.startActivity(intent);
        }
    });
}
