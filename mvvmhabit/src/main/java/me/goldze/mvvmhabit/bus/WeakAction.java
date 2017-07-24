package me.goldze.mvvmhabit.bus;

import java.lang.ref.WeakReference;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * About : kelin的WeakAction
 */
public class WeakAction<T> {
    private Action0 action;
    private Action1<T> action1;
    private boolean isLive;
    private Object target;
    private WeakReference reference;

    public WeakAction(Object target, Action0 action) {
        reference = new WeakReference(target);
        this.action = action;

    }

    public WeakAction(Object target, Action1<T> action1) {
        reference = new WeakReference(target);
        this.action1 = action1;
    }

    public void execute() {
        if (action != null && isLive()) {
            action.call();
        }
    }

    public void execute(T parameter) {
        if (action1 != null
                && isLive()) {
            action1.call(parameter);
        }
    }

    public void markForDeletion() {
        reference.clear();
        reference = null;
        action = null;
        action1 = null;
    }

    public Action0 getAction() {
        return action;
    }

    public Action1 getAction1() {
        return action1;
    }

    public boolean isLive() {
        if (reference == null) {
            return false;
        }
        if (reference.get() == null) {
            return false;
        }
        return true;
    }


    public Object getTarget() {
        if (reference != null) {
            return reference.get();
        }
        return null;
    }
}
