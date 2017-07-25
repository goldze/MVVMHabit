package me.goldze.mvvmhabit.binding.viewadapter.image;


import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"uri"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String uri, @DrawableRes int placeholderRes) {
        if (!TextUtils.isEmpty(uri)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext()).load(uri).into(imageView);
        }
    }
}

