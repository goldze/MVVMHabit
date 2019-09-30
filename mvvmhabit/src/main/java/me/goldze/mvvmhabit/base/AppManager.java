package me.goldze.mvvmhabit.base;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import java.util.Stack;

/**
 * Created by goldze on 2017/6/15.
 * activity堆栈式管理
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static Stack<Fragment> fragmentStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单例模式
     *
     * @return AppManager
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public static Stack<Fragment> getFragmentStack() {
        return fragmentStack;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }


    /**
     * 是否有activity
     */
    public boolean isActivity() {
        if (activityStack != null) {
            return !activityStack.isEmpty();
        }
        return false;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }


    /**
     * 添加Fragment到堆栈
     */
    public void addFragment(Fragment fragment) {
        if (fragmentStack == null) {
            fragmentStack = new Stack<Fragment>();
        }
        fragmentStack.add(fragment);
    }

    /**
     * 移除指定的Fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentStack.remove(fragment);
        }
    }


    /**
     * 是否有Fragment
     */
    public boolean isFragment() {
        if (fragmentStack != null) {
            return !fragmentStack.isEmpty();
        }
        return false;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Fragment currentFragment() {
        if (fragmentStack != null) {
            Fragment fragment = fragmentStack.lastElement();
            return fragment;
        }
        return null;
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
//          android.os.Process.killProcess(android.os.Process.myPid());
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            //System.exit(0);
        } catch (Exception e) {
            activityStack.clear();
            e.printStackTrace();
        }
    }
}