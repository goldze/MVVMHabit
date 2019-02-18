package me.goldze.mvvmhabit.binding.viewadapter.spinner;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    /**
     * 双向的SpinnerViewAdapter, 可以监听选中的条目,也可以回显选中的值
     *
     * @param spinner        控件本身
     * @param itemDatas      下拉条目的集合
     * @param valueReply     回显的value
     * @param bindingCommand 条目点击的监听
     */
    @BindingAdapter(value = {"itemDatas", "valueReply", "resource", "dropDownResource", "onItemSelectedCommand"}, requireAll = false)
    public static void onItemSelectedCommand(final Spinner spinner, final List<IKeyAndValue> itemDatas, String valueReply, int resource, int dropDownResource, final BindingCommand<IKeyAndValue> bindingCommand) {
        if (itemDatas == null) {
            throw new NullPointerException("this itemDatas parameter is null");
        }
        List<String> lists = new ArrayList<>();
        for (IKeyAndValue iKeyAndValue : itemDatas) {
            lists.add(iKeyAndValue.getKey());
        }
        if (resource == 0) {
            resource = android.R.layout.simple_spinner_item;
        }
        if (dropDownResource == 0) {
            dropDownResource = android.R.layout.simple_spinner_dropdown_item;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(spinner.getContext(), resource, lists);
        adapter.setDropDownViewResource(dropDownResource);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IKeyAndValue iKeyAndValue = itemDatas.get(position);
                //将IKeyAndValue对象交给ViewModel
                bindingCommand.execute(iKeyAndValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //回显选中的值
        if (!TextUtils.isEmpty(valueReply)) {
            for (int i = 0; i < itemDatas.size(); i++) {
                IKeyAndValue iKeyAndValue = itemDatas.get(i);
                if (valueReply.equals(iKeyAndValue.getValue())) {
                    spinner.setSelection(i);
                    return;
                }
            }
        }
    }
}
