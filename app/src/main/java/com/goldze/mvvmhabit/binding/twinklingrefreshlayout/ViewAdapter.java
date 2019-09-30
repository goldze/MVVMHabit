package com.goldze.mvvmhabit.binding.twinklingrefreshlayout;


import androidx.databinding.BindingAdapter;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import me.goldze.mvvmhabit.binding.command.BindingCommand;


/**
 * Created by goldze on 2017/6/16.
 * TwinklingRefreshLayout列表刷新的绑定适配器
 */
public class ViewAdapter {

    @BindingAdapter(value = {"onRefreshCommand", "onLoadMoreCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(TwinklingRefreshLayout layout, final BindingCommand onRefreshCommand, final BindingCommand onLoadMoreCommand) {
        layout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
            }
        });
    }
}
