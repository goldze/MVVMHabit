package com.goldze.mvvmhabit.ui.vm;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.ui.fragment.FormFragment;
import com.goldze.mvvmhabit.ui.fragment.NetWorkFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * Created by goldze on 2017/7/17.
 */

public class DemoViewModel extends BaseViewModel {
    public DemoViewModel(Context context) {
        //要使用父类的context相关方法,记得加上这一句
        super(context);
    }

    //网络访问点击事件
    public BindingCommand netWorkClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(NetWorkFragment.class.getCanonicalName());
        }
    });
    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(FormFragment.class.getCanonicalName());
        }
    });
    //表单修改点击事件
    public BindingCommand formModifyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //模拟一个修改的实体数据
            FormEntity entity = new FormEntity();
            entity.setName("goldze");
            entity.setSex("1");
            entity.setBir("xxxx年xx月xx日");
            entity.setMarry(true);
            //传入实体数据
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("entity", entity);
            startContainerActivity(FormFragment.class.getCanonicalName(), mBundle);
        }
    });
    //权限申请
    public BindingCommand permissionsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //请求打开相机权限
            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                ToastUtils.showShort("相机权限已经打开，直接跳入相机");
                            } else {
                                ToastUtils.showShort("权限被拒绝");
                            }
                        }
                    });
        }
    });

    //异常全局异常捕获
    public BindingCommand exceptionClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //伪造一个异常
            Integer.parseInt("a");
        }
    });
    //文件下载
    public BindingCommand fileDownLoadClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            downloadFile();
        }
    });

    public void downloadFile() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String loadUrl = "http://a.gdown.baidu.com/data/wisegame/2828a29ba864e167/neihanduanzi_660.apk?from=a1101";
        String destFileDir = context.getCacheDir().getPath();  //文件存放的路径
        String destFileName = System.currentTimeMillis() + ".apk";//文件存放的名称
        DownLoadManager.getInstance().load(loadUrl, new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                ToastUtils.showShort("文件下载完成！");
            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) progress);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort("文件下载失败！");
            }
        });
    }
}
