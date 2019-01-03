package com.goldze.mvvmhabit.ui.form;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentFormBinding;
import com.goldze.mvvmhabit.entity.FormEntity;

import java.util.Calendar;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;

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
    public void initData() {
        //通过binding拿到toolbar控件, 设置给Activity
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.include.toolbar);
        //View层传参到ViewModel层
        viewModel.setFormEntity(entity);
        //初始化标题
        viewModel.initToolbar();
    }

    @Override
    public void initViewObservable() {
        //监听日期选择
        viewModel.uc.showDateDialogObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        viewModel.setBir(year, month, dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.setMessage("生日选择");
                datePickerDialog.show();
            }
        });
        viewModel.entityJsonLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String submitJson) {
                MaterialDialogUtils.showBasicDialog(getContext(), "提交的json实体数据：\r\n" + submitJson).show();
            }
        });
    }
}
