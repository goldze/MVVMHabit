package me.goldze.mvvmhabit.base;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IBaseActivity {
    /**
     * 初始化界面传递参数
     */
    void initParam();

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    int initContentView();

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    BaseViewModel initViewModel();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
