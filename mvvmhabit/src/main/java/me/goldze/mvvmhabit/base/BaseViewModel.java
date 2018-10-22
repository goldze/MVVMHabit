package me.goldze.mvvmhabit.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by goldze on 2017/6/15.
 */
public class BaseViewModel extends AndroidViewModel implements IBaseViewModel {
    private UIChangeLiveData uc;
    private LifecycleProvider lifecycle;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = lifecycle;
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle;
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    public void showDialog() {
        showDialog("请稍后...");
    }

    public void showDialog(String title) {
        uc.showLoadingLiveData.postValue(title);
    }

    @UiThread
    public void dismissDialog() {
        uc.dismissLoadingLiveData.call();
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
    public void startActivity(@NonNull Class<?> clz, @Nullable Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
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
    public void startContainerActivity(@NonNull String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(@NonNull String canonicalName, @Nullable Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CANONICAL_NAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startContainerActivityLiveData.postValue(params);
    }

    /**
     * 关闭界面
     */
    @UiThread
    public void finish() {
        uc.finishLiveData.call();
    }

    /**
     * 显示toast或者SnackBar
     */
    @UiThread
    public void showTips(@NonNull String msg) {
        uc.tipsLiveData.setValue(msg);
    }

    /**
     * 返回上一层
     */
    @UiThread
    public void onBackPressed() {
        uc.onBackPressedLiveData.call();
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

    public static final class UIChangeLiveData {
        // 实际项目中，对话框可能有很多种样式，这里showDialogLiveData和dismissDialogLiveData只能显示loading对话框
        private MutableLiveData<String> showLoadingLiveData = new MutableLiveData<>();
        private SingleLiveEvent<Void> dismissLoadingLiveData = new SingleLiveEvent<>();
        private SingleLiveEvent<String> tipsLiveData = new SingleLiveEvent<>();
        private SingleLiveEvent<Void> finishLiveData = new SingleLiveEvent<>();
        private SingleLiveEvent<Void> onBackPressedLiveData = new SingleLiveEvent<>();
        private MutableLiveData<Map<String, Object>> startActivityLiveData = new MutableLiveData<>();
        private MutableLiveData<Map<String, Object>> startContainerActivityLiveData = new MutableLiveData<>();

        public MutableLiveData<String> getShowLoadingLiveData() {
            return showLoadingLiveData;
        }

        public MutableLiveData<Void> getDismissLoadingLiveData() {
            return dismissLoadingLiveData;
        }

        public MutableLiveData<Map<String, Object>> getStartActivityLiveData() {
            return startActivityLiveData;
        }

        public SingleLiveEvent<String> getTipsLiveData() {
            return tipsLiveData;
        }

        public MutableLiveData<Map<String, Object>> getStartContainerActivityLiveData() {
            return startContainerActivityLiveData;
        }

        public MutableLiveData<Void> getFinishLiveData() {
            return finishLiveData;
        }

        public MutableLiveData<Void> getOnBackPressedLiveData() {
            return onBackPressedLiveData;
        }
    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
