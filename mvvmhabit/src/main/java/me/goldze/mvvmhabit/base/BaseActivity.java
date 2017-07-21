package me.goldze.mvvmhabit.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import me.goldze.mvvmhabit.bus.WeakMessenger;


/**
 * Created by 曾宪泽 on 2017/6/15.
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider绑定生命周期
 */

public class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseActivity {
    protected V binding;
    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParam();

        initViewDataBinding();

        initData();

        initViewObservable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeakMessenger.getDefault().unregister(this);
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView());
        binding.setVariable(initVariableId(), viewModel = initViewModel());
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }

    @Override
    public void initParam() {

    }

    @Override
    public int initContentView() {
        return 0;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public VM initViewModel() {
        return null;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }
}
