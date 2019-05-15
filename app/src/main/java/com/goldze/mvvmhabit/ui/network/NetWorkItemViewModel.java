package com.goldze.mvvmhabit.ui.network;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.ui.network.detail.DetailFragment;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class NetWorkItemViewModel extends ItemViewModel<NetWorkViewModel> {
    public ObservableField<DemoEntity.ItemsEntity> entity = new ObservableField<>();
    public Drawable drawableImg;

    public NetWorkItemViewModel(@NonNull NetWorkViewModel viewModel, DemoEntity.ItemsEntity entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.mipmap.ic_launcher);
    }

    /**
     * 获取position的方式有很多种,indexOf是其中一种，常见的还有在Adapter中、ItemBinding.of回调里
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //这里可以通过一个标识,做出判断，已达到跳入不同界面的逻辑
            if (entity.get().getId() == -1) {
                viewModel.deleteItemLiveData.setValue(NetWorkItemViewModel.this);
            } else {
                //跳转到详情界面,传入条目的实体对象
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("entity", entity.get());
                viewModel.startContainerActivity(DetailFragment.class.getCanonicalName(), mBundle);
            }
        }
    });
    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //以前是使用Messenger发送事件，在NetWorkViewModel中完成删除逻辑
//            Messenger.getDefault().send(NetWorkItemViewModel.this, NetWorkViewModel.TOKEN_NETWORKVIEWMODEL_DELTE_ITEM);
            //现在ItemViewModel中存在ViewModel引用，可以直接拿到LiveData去做删除
            ToastUtils.showShort(entity.get().getName());
        }
    });
//    /**
//     * 可以在xml中使用binding:currentView="@{viewModel.titleTextView}" 拿到这个控件的引用, 但是强烈不推荐这样做，避免内存泄漏
//     **/
//    private TextView tv;
//    //将标题TextView控件回调到ViewModel中
//    public BindingCommand<TextView> titleTextView = new BindingCommand(new BindingConsumer<TextView>() {
//        @Override
//        public void call(TextView tv) {
//            NetWorkItemViewModel.this.tv = tv;
//        }
//    });
}
