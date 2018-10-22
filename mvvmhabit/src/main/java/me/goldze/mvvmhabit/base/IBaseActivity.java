package me.goldze.mvvmhabit.base;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IBaseActivity{
    /**
     * 初始化界面传递参数
     */
    void initParams();
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
