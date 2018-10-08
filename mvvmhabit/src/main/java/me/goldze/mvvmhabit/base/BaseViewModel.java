package me.goldze.mvvmhabit.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.binding.viewadapter.viewgroup.IBindingItemViewModel;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;

/**
 * Created by goldze on 2017/6/15.
 */
public class BaseViewModel extends AndroidViewModel implements IBaseViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeLiveData uc = new UIChangeLiveData();

    public class UIChangeLiveData extends LiveData {
        public MutableLiveData<String> showDialogLiveData = new MutableLiveData();
        public MutableLiveData<Boolean> dismissDialogLiveData = new MutableLiveData();
        public MutableLiveData<Map<String, Object>> startActivityLiveData = new MutableLiveData();
        public MutableLiveData<Map<String, Object>> startContainerActivityLiveData = new MutableLiveData();
        public MutableLiveData<Boolean> finishLiveData = new MutableLiveData();
        public MutableLiveData<Boolean> onBackPressedLiveData = new MutableLiveData();
    }

    public void showDialog() {
        showDialog("请稍后...");
    }

    public void showDialog(String title) {
        uc.showDialogLiveData.postValue(title);
    }

    public void dismissDialog() {
        uc.dismissDialogLiveData.postValue(uc.dismissDialogLiveData.getValue() == null ? false : !uc.dismissDialogLiveData.getValue());
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startActivityLiveData.postValue(params);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Map<String, Object> params = new HashMap();
        params.put(ParameterField.CANONICALNAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startContainerActivityLiveData.postValue(params);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        uc.finishLiveData.postValue(uc.finishLiveData.getValue() == null ? false : !uc.finishLiveData.getValue());
    }

    /**
     * 返回上一层
     */
    public void onBackPressed() {
        uc.onBackPressedLiveData.postValue(uc.onBackPressedLiveData.getValue() == null ? false : !uc.onBackPressedLiveData.getValue());
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerRxBus() {
    }

    @Override
    public void removeRxBus() {
    }

    public static class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICALNAME = "CANONICALNAME";
        public static String BUNDLE = "BUNDLE";
    }
}
