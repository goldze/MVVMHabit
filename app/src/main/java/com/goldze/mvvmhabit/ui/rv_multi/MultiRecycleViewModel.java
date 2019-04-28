package com.goldze.mvvmhabit.ui.rv_multi;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Create Author：goldze
 * Create Date：2019/01/25
 * Description：
 */

public class MultiRecycleViewModel extends BaseViewModel {
    private static final String MultiRecycleType_Head = "head";
    private static final String MultiRecycleType_Left = "left";
    private static final String MultiRecycleType_Right = "right";

    public MultiRecycleViewModel(@NonNull Application application) {
        super(application);
        //模拟10个条目，数据源可以来自网络
        for (int i = 0; i < 20; i++) {
            if (i == 0) {
                MultiItemViewModel item = new MultiRecycleHeadViewModel(this);
                //条目类型为头布局
                item.multiItemType(MultiRecycleType_Head);
                observableList.add(item);
            } else {
                String text = "我是第" + i + "条";
                if (i % 2 == 0) {
                    MultiItemViewModel item = new MultiRecycleLeftItemViewModel(this, text);
                    //条目类型为左布局
                    item.multiItemType(MultiRecycleType_Left);
                    observableList.add(item);
                } else {
                    MultiItemViewModel item = new MultiRecycleRightItemViewModel(this, text);
                    //条目类型为右布局
                    item.multiItemType(MultiRecycleType_Right);
                    observableList.add(item);
                }
             }
        }
    }

    //给RecyclerView添加ObservableList
    public ObservableList<MultiItemViewModel> observableList = new ObservableArrayList<>();
    //RecyclerView多布局添加ItemBinding
    public ItemBinding<MultiItemViewModel> itemBinding = ItemBinding.of(new OnItemBind<MultiItemViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, MultiItemViewModel item) {
            //通过item的类型, 动态设置Item加载的布局
            String itemType = (String) item.getItemType();
            if (MultiRecycleType_Head.equals(itemType)) {
                //设置头布局
                itemBinding.set(BR.viewModel, R.layout.item_multi_head);
            } else if (MultiRecycleType_Left.equals(itemType)) {
                //设置左布局
                itemBinding.set(BR.viewModel, R.layout.item_multi_rv_left);
            } else if (MultiRecycleType_Right.equals(itemType)) {
                //设置右布局
                itemBinding.set(BR.viewModel, R.layout.item_multi_rv_right);
            }
        }
    });
}
