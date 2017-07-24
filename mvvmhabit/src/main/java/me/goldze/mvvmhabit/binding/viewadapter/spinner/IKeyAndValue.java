package me.goldze.mvvmhabit.binding.viewadapter.spinner;

/**
 * Created by goldze on 2017/6/18.
 * 下拉Spinner控件的键值对, 实现该接口,返回key,value值, 在xml绑定List<IKeyAndValue>
 */
public interface IKeyAndValue {
    String getKey();

    String getValue();
}
