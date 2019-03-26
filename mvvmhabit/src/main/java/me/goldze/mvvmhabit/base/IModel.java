package me.goldze.mvvmhabit.base;

/**
 * Created by goldze on 2017/6/15.
 */
public interface IModel {
    /**
     * Model层与ViewModel共消亡
     */
    void onCleared();
}
