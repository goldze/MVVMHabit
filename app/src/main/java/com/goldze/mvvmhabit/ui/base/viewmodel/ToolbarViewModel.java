package com.goldze.mvvmhabit.ui.base.viewmodel;

import android.app.Application;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Create Author：goldze
 * Create Date：2019/01/03
 * Description： 对应include标题的ToolbarViewModel
 * Toolbar的封装方式有很多种，具体封装需根据项目实际业务和习惯来编写
 * 所有例子仅做参考,业务多种多样,可能我这里写的例子和你的需求不同，理解如何使用才最重要。
 */

public class ToolbarViewModel<M extends BaseModel> extends BaseViewModel<M> {
    //标题文字
    public ObservableField<String> titleText = new ObservableField<>("");
    //右边文字
    public ObservableField<String> rightText = new ObservableField<>("更多");
    //右边文字的观察者
    public ObservableInt rightTextVisibleObservable = new ObservableInt(View.GONE);
    //右边图标的观察者
    public ObservableInt rightIconVisibleObservable = new ObservableInt(View.GONE);
    //兼容databinding，去泛型化
    public ToolbarViewModel toolbarViewModel;

    public ToolbarViewModel(@NonNull Application application) {
        this(application, null);
    }

    public ToolbarViewModel(@NonNull Application application, M model) {
        super(application, model);
        toolbarViewModel = this;
    }

    /**
     * 设置标题
     *
     * @param text 标题文字
     */
    public void setTitleText(String text) {
        titleText.set(text);
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    public void setRightText(String text) {
        rightText.set(text);
    }

    /**
     * 设置右边文字的显示和隐藏
     *
     * @param visibility
     */
    public void setRightTextVisible(int visibility) {
        rightTextVisibleObservable.set(visibility);
    }

    /**
     * 设置右边图标的显示和隐藏
     *
     * @param visibility
     */
    public void setRightIconVisible(int visibility) {
        rightIconVisibleObservable.set(visibility);
    }

    /**
     * 返回按钮的点击事件
     */
    public final BindingCommand backOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand rightTextOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            rightTextOnClick();
        }
    });
    public BindingCommand rightIconOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            rightIconOnClick();
        }
    });

    /**
     * 右边文字的点击事件，子类可重写
     */
    protected void rightTextOnClick() {
    }

    /**
     * 右边图标的点击事件，子类可重写
     */
    protected void rightIconOnClick() {
    }
}
