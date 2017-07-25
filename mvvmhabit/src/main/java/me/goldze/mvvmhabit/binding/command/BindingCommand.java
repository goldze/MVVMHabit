package me.goldze.mvvmhabit.binding.command;


import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * About : kelin的ReplyCommand
 * 执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
public class BindingCommand<T> {
    private Action0 execute0;
    private Action1<T> execute1;
    private Func0<Boolean> canExecute0;

    public BindingCommand(Action0 execute) {
        this.execute0 = execute;
    }

    /**
     * @param execute 带泛型参数的命令绑定
     */
    public BindingCommand(Action1<T> execute) {
        this.execute1 = execute;
    }

    /**
     * @param execute 触发命令
     * @param canExecute0 true则执行,反之不执行
     */
    public BindingCommand(Action0 execute, Func0<Boolean> canExecute0) {
        this.execute0 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     * @param execute 带泛型参数触发命令
     * @param canExecute0 true则执行,反之不执行
     */
    public BindingCommand(Action1<T> execute, Func0<Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     * 执行Action命令
     */
    public void execute() {
        if (execute0 != null && canExecute0()) {
            execute0.call();
        }
    }

    /**
     * 执行带泛型参数的命令
     *
     * @param parameter 泛型参数
     */
    public void execute(T parameter) {
        if (execute1 != null && canExecute0()) {
            execute1.call(parameter);
        }
    }

    /**
     * 是否需要执行
     *
     * @return true则执行, 反之不执行
     */
    private boolean canExecute0() {
        if (canExecute0 == null) {
            return true;
        }
        return canExecute0.call();
    }


}
