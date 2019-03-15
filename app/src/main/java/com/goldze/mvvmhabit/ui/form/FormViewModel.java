package com.goldze.mvvmhabit.ui.form;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.entity.SpinnerItemData;
import com.goldze.mvvmhabit.ui.base.viewmodel.ToolbarViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.binding.viewadapter.spinner.IKeyAndValue;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class FormViewModel extends ToolbarViewModel {
    public FormEntity entity;

    public List<IKeyAndValue> sexItemDatas;
    public SingleLiveEvent<String> entityJsonLiveData = new SingleLiveEvent<>();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc;

    public class UIChangeObservable {
        //显示日期对话框
        public ObservableBoolean showDateDialogObservable;

        public UIChangeObservable() {
            showDateDialogObservable = new ObservableBoolean(false);
        }
    }

    public FormViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uc = new UIChangeObservable();
        //sexItemDatas 一般可以从本地Sqlite数据库中取出数据字典对象集合，让该对象实现IKeyAndValue接口
        sexItemDatas = new ArrayList<>();
        sexItemDatas.add(new SpinnerItemData("男", "1"));
        sexItemDatas.add(new SpinnerItemData("女", "2"));
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        //初始化标题栏
        setRightTextVisible(View.VISIBLE);
        if (TextUtils.isEmpty(entity.getId())) {
            //ID为空是新增
            setTitleText("表单提交");
        } else {
            //ID不为空是修改
            setTitleText("表单编辑");
        }
    }

    @Override
    public void rightTextOnClick() {
        ToastUtils.showShort("更多");
    }

    public void setFormEntity(FormEntity entity) {
        if (this.entity == null) {
            this.entity = entity;
        }
    }

    //性别选择的监听
    public BindingCommand<IKeyAndValue> onSexSelectorCommand = new BindingCommand<>(new BindingConsumer<IKeyAndValue>() {
        @Override
        public void call(IKeyAndValue iKeyAndValue) {
            entity.setSex(iKeyAndValue.getValue());
        }
    });
    //生日选择的监听
    public BindingCommand onBirClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //回调到view层(Fragment)中显示日期对话框
            uc.showDateDialogObservable.set(!uc.showDateDialogObservable.get());
        }
    });
    //是否已婚Switch点状态改变回调
    public BindingCommand<Boolean> onMarryCheckedChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean isChecked) {
            entity.setMarry(isChecked);
        }
    });
    //提交按钮点击事件
    public BindingCommand onCmtClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            String submitJson = new Gson().toJson(entity);
            entityJsonLiveData.setValue(submitJson);
        }
    });

    public void setBir(int year, int month, int dayOfMonth) {
        //设置数据到实体中，自动刷新界面
        entity.setBir(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
        //刷新实体,驱动界面更新
        entity.notifyChange();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
