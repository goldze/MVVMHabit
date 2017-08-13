package com.goldze.mvvmhabit.ui.vm;

import com.google.gson.Gson;

import android.app.DatePickerDialog;
import android.databinding.ObservableBoolean;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.widget.DatePicker;

import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.entity.SpinnerItemData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.viewadapter.spinner.IKeyAndValue;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by goldze on 2017/7/17.
 */

public class FormViewModel extends BaseViewModel {
    public FormEntity entity;
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    public List<IKeyAndValue> sexItemDatas;

    public class UIChangeObservable {
        //刷新界面的观察者
        public ObservableBoolean refreshUIObservable = new ObservableBoolean(false);
    }

    public FormViewModel(Fragment fragment, FormEntity entity) {
        super(fragment);
        this.entity = entity;
        initData();
    }

    public void initData() {
        sexItemDatas = new ArrayList<>();
        //SpinnerItemData该实体可以是
        sexItemDatas.add(new SpinnerItemData("男", "1"));
        sexItemDatas.add(new SpinnerItemData("女", "2"));
    }

    //性别选择的监听
    public BindingCommand<IKeyAndValue> onSexSelectorCommand = new BindingCommand<>(new Action1<IKeyAndValue>() {
        @Override
        public void call(IKeyAndValue iKeyAndValue) {
            entity.setSex(iKeyAndValue.getValue());
        }
    });
    //生日选择的监听
    public BindingCommand onBirClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //设置数据到实体中
                    entity.setBir(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                    //回调到Fragment中刷新界面
                    uc.refreshUIObservable.set(!uc.refreshUIObservable.get());
                }
            }, year, month, day);
            datePickerDialog.setMessage("生日选择");
            datePickerDialog.show();
        }
    });
    //是否已婚Switch点状态改变回调
    public BindingCommand<Boolean> onMarryCheckedChangeCommand = new BindingCommand<>(new Action1<Boolean>() {
        @Override
        public void call(Boolean isChecked) {
            entity.setMarry(isChecked);
        }
    });
    //提交按钮点击事件
    public BindingCommand onCmtClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            String submitJson = new Gson().toJson(entity);
            MaterialDialogUtils.showBasicDialog(context, "提交的json实体数据：\r\n" + submitJson).show();

//            RetrofitClient.getInstance().create(DemoApiService.class)
//                    .demoPost("666")
//                    .compose(RxUtils.bindToLifecycle(context)) //生命周期与界面同步
//                    .compose(RxUtils.schedulersTransformer()) //线程调度
//                    .doOnSubscribe(new Action0() {
//                        @Override
//                        public void call() {
//                            showDialog();
//                        }
//                    })
//                    .subscribe(new Action1<BaseResponse<DemoEntity>>() {
//                        @Override
//                        public void call(BaseResponse<DemoEntity> demoEntityBaseResponse) {
//                            dismissDialog();
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            dismissDialog();
//                        }
//                    });
        }
    });
}
