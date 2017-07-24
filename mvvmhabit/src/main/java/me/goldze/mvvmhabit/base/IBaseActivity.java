package me.goldze.mvvmhabit.base;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IBaseActivity {

    void initParam();

    int initContentView();

    int initVariableId();

    BaseViewModel initViewModel();

    void initData();

    void initViewObservable();
}
