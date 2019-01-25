package com.goldze.mvvmhabit.ui.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by goldze on 2017/7/17.
 * FragmentPager适配器
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;//ViewPager要填充的fragment列表
    private List<String> title;//tab中的title文字列表

    //使用构造方法来将数据传进去
    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> title) {
        super(fm);
        this.list = list;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {//获得position中的fragment来填充
        return list.get(position);
    }

    @Override
    public int getCount() {//返回FragmentPager的个数
        return list.size();
    }

    //FragmentPager的标题,如果重写这个方法就显示不出tab的标题内容
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
