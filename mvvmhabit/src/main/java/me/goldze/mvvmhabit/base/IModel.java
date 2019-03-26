package me.goldze.mvvmhabit.base;

/**
 * Created by goldze on 2017/6/15.
 */
public interface IModel {
    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。Model层同样不能持有长生命周期对象
     */
    void onCleared();
}
