package com.goldze.mvvmhabit.ui.fragment;

import android.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentFormBinding;
import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.ui.vm.FormViewModel;

import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2017/7/17.
 * 表单提交/编辑界面
 */

public class FormFragment extends BaseFragment<FragmentFormBinding, FormViewModel> {

    private FormEntity entity = new FormEntity();

    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            entity = mBundle.getParcelable("entity");
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_form;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public FormViewModel initViewModel() {
        return new FormViewModel(this, entity);
    }

    @Override
    public void initViewObservable() {
        //监听刷新界面
        viewModel.uc.refreshUIObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                //调用父类的刷新方法
                refreshLayout();
            }
        });
    }
}
