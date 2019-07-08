package com.goldze.mvvmhabit.ui.network;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.DemoEntity;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class NetWorkViewModel extends BaseViewModel<DemoRepository> {
    public SingleLiveEvent<NetWorkItemViewModel> deleteItemLiveData = new SingleLiveEvent<>();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent finishLoadmore = new SingleLiveEvent<>();
    }

    public NetWorkViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<NetWorkItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<NetWorkItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_network);
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("下拉刷新");
            requestNetWork();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (observableList.size() > 50) {
                ToastUtils.showLong("兄dei，你太无聊啦~崩是不可能的~");
                uc.finishLoadmore.call();
                return;
            }
            //模拟网络上拉加载更多
            addSubscribe(model.loadMore()
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            ToastUtils.showShort("上拉加载");
                        }
                    })
                    .subscribe(new Consumer<DemoEntity>() {
                        @Override
                        public void accept(DemoEntity entity) throws Exception {
                            for (DemoEntity.ItemsEntity itemsEntity : entity.getItems()) {
                                NetWorkItemViewModel itemViewModel = new NetWorkItemViewModel(NetWorkViewModel.this, itemsEntity);
                                //双向绑定动态添加Item
                                observableList.add(itemViewModel);
                            }
                            //刷新完成收回
                            uc.finishLoadmore.call();
                        }
                    }));
        }
    });

    /**
     * 网络请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     */
    public void requestNetWork() {
        //建议调用addSubscribe()添加Disposable，请求与View周期同步
        //addSubscribe();
        model.demoGet()
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider())) //请求与View周期同步（过度期，尽量少使用）
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("正在请求...");
                    }
                })
                .subscribe(new Consumer<BaseResponse<DemoEntity>>() {
                    @Override
                    public void accept(BaseResponse<DemoEntity> response) throws Exception {
                        //清除列表
                        observableList.clear();
                        //请求成功
                        if (response.getCode() == 1) {
                            for (DemoEntity.ItemsEntity entity : response.getResult().getItems()) {
                                NetWorkItemViewModel itemViewModel = new NetWorkItemViewModel(NetWorkViewModel.this, entity);
                                //双向绑定动态添加Item
                                observableList.add(itemViewModel);
                            }
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            ToastUtils.showShort("数据错误");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                        //请求刷新完成收回
                        uc.finishRefreshing.call();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                        //请求刷新完成收回
                        uc.finishRefreshing.call();
                    }
                });
    }

    /**
     * 删除条目
     *
     * @param netWorkItemViewModel
     */
    public void deleteItem(NetWorkItemViewModel netWorkItemViewModel) {
        //点击确定，在 observableList 绑定中删除，界面立即刷新
        observableList.remove(netWorkItemViewModel);
    }

    /**
     * 获取条目下标
     *
     * @param netWorkItemViewModel
     * @return
     */
    public int getItemPosition(NetWorkItemViewModel netWorkItemViewModel) {
        return observableList.indexOf(netWorkItemViewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
