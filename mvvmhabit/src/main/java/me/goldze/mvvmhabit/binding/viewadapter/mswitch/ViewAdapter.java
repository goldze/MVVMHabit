package me.goldze.mvvmhabit.binding.viewadapter.mswitch;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

import static android.R.attr.checked;
import static me.goldze.mvvmhabit.R.attr.switchState;

/**
 * Created by goldze on 2017/6/18.
 */

public class ViewAdapter {
    /**
     * 设置开关状态
     *
     * @param mSwitch Switch控件
     */
    @BindingAdapter("switchState")
    public static void setSwitchState(Switch mSwitch, boolean isChecked) {
        mSwitch.setChecked(isChecked);
    }

    /**
     * Switch的状态改变监听
     *
     * @param mSwitch        Switch控件
     * @param changeListener 事件绑定命令
     */
    @BindingAdapter("onCheckedChangeCommand")
    public static void onCheckedChangeCommand(final Switch mSwitch, final BindingCommand<Boolean> changeListener) {
        if (changeListener != null) {
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    changeListener.execute(isChecked);
                }
            });
        }
    }
}
