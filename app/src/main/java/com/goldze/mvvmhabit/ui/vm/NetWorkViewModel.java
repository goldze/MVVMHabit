package com.goldze.mvvmhabit.ui.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.os.Handler;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.service.DemoApiService;
import com.goldze.mvvmhabit.utils.RetrofitClient;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class NetWorkViewModel extends BaseViewModel {
    private int itemIndex = 0;

    public NetWorkViewModel(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        requestNetWork();
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成的观察者
        public ObservableBoolean isFinishRefreshing = new ObservableBoolean(false);
        //上拉加载完成的观察者
        public ObservableBoolean isFinishLoadmore = new ObservableBoolean(false);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<NetWorkItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemView
    public ItemBinding<NetWorkItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_network);

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("下拉刷新");
            //重新请求
            requestNetWork();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (itemIndex > 50) {
                ToastUtils.showLong("兄die，你太无聊啦~崩是不可能的~");
                uc.isFinishLoadmore.set(!uc.isFinishLoadmore.get());
                return;
            }
            ToastUtils.showShort("上拉加载");
            //模拟网络请求完成后收回
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //刷新完成收回
                    uc.isFinishLoadmore.set(!uc.isFinishLoadmore.get());
                    //模拟一部分假数据
                    for (int i = 0; i < 10; i++) {
                        DemoEntity.ItemsEntity item = new DemoEntity.ItemsEntity();
                        item.setId(-1);
                        item.setName("模拟条目" + itemIndex++);
                        //动态添加Item
                        observableList.add(new NetWorkItemViewModel(context, item));
                    }
                }
            }, 3000);
        }
    });

    private void requestNetWork() {
        RetrofitClient.getInstance().create(DemoApiService.class)
                .demoGet()
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 请求code异常处理, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<BaseResponse<DemoEntity>>() {
                    @Override
                    public void accept(BaseResponse<DemoEntity> response) throws Exception {
                        itemIndex = 0;
                        //关闭对话框
                        dismissDialog();
                        //清除列表
                        observableList.clear();
                        //刷新完成收回
                        uc.isFinishRefreshing.set(!uc.isFinishRefreshing.get());
                        //请求成功
                        if (response.getCode() == 1) {
                            //将实体赋给全局变量，双向绑定动态添加Item
                            for (DemoEntity.ItemsEntity entity : response.getResult().getItems()) {
                                observableList.add(new NetWorkItemViewModel(context, entity));
                            }
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            ToastUtils.showShort("数据错误");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissDialog();
                        ToastUtils.showShort("请求异常");
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observableList.clear();
        observableList = null;
    }
}
