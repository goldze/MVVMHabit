package com.goldze.mvvmhabit.ui.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.service.DemoApiService;
import com.goldze.mvvmhabit.utils.RetrofitClient;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by goldze on 2017/7/17.
 */

public class NetWorkViewModel extends BaseViewModel {

    public NetWorkViewModel(Context context) {
        super(context);
        //请求网络数据
        requestNetWork();
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //请求成功的观察者
        public ObservableBoolean isRequestSuccess = new ObservableBoolean(false);
    }

    //给RecyclerView添加ObservableList
    public final ObservableList<NetWorkItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemView
    public final ItemViewSelector<NetWorkItemViewModel> itemView = new ItemViewSelector<NetWorkItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, NetWorkItemViewModel item) {
            //设置item中ViewModel的id和item的layout
            itemView.set(BR.viewModel, R.layout.item_network);
        }

        @Override
        public int viewTypeCount() {
            //RecyclerView需要划分的部分数，如果是一个list,就返回1，如果带有head和list，就返回2
            return 1;
        }
    };
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            ToastUtils.showShort("下拉刷新");
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            ToastUtils.showShort("上拉加载");
        }
    });

    private void requestNetWork() {
        RetrofitClient.getInstance().create(DemoApiService.class)
                .demoGet()
                .compose(RxUtils.bindToLifecycle(context)) //生命周期与界面同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 请求code异常处理, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog();
                    }
                })
                .subscribe(new Action1<BaseResponse<DemoEntity>>() {
                    @Override
                    public void call(BaseResponse<DemoEntity> response) {
                        dismissDialog();
                        //请求成功
                        if (response.getCode() == 1) {
                            //将实体赋给全局变量
                            for (DemoEntity.ItemsEntity entity : response.getResult().getItems()) {
                                observableList.add(new NetWorkItemViewModel(context, entity));
                            }
                            //刷新界面
//                            uc.isRequestSuccess.set(!uc.isRequestSuccess.get());
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            ToastUtils.showShort("数据错误");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissDialog();
                        ToastUtils.showShort("请求异常");
                        throwable.printStackTrace();
                    }
                });
    }
}
