package com.goldze.mvvmhabit.entity;

import me.goldze.mvvmhabit.binding.viewadapter.spinner.IKeyAndValue;

/**
 * Created by goldze on 2017/7/17.
 * Spinner条目数据实体
 * 该实体类可以自定义,比如该类是数据库实体类. 或者是数据字典实体类, 但需要实现IKeyAndValue接口, 返回key和value两个值就可以在Spinner中绑定使用了
 */

public class SpinnerItemData implements IKeyAndValue {
    //key是下拉显示的文字
    private String key;
    //value是对应需要上传给后台的值, 这个可以根据具体业务具体定义
    private String value;

    public SpinnerItemData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}