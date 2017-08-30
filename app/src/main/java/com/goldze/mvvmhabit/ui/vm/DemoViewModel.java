package com.goldze.mvvmhabit.ui.vm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.ui.fragment.FormFragment;
import com.goldze.mvvmhabit.ui.fragment.NetWorkFragment;
import com.tbruyelle.rxpermissions.RxPermissions;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import rx.functions.Action0;
import rx.functions.Action1;

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
            startContainerActivity(NetWorkFragment.class.getCanonicalName());
        }
    });
    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            startContainerActivity(FormFragment.class.getCanonicalName());
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
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("entity", entity);
            startContainerActivity(FormFragment.class.getCanonicalName(), mBundle);
        }
    });
    //权限申请
    public BindingCommand permissionsClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            //请求打开相机权限
            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                ToastUtils.showShort("相机权限已经打开，直接跳入相机");
                            } else {
                                ToastUtils.showShort("权限被拒绝");
                            }
                        }
                    });
        }
    });

    //异常全局异常捕获
    public BindingCommand exceptionClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            //伪造一个异常
            Integer.parseInt("a");
        }
    });
}
