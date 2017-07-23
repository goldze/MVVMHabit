package me.goldze.mvvmhabit.binding.viewadapter.image;


import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    //requireAll 是意思是是否需要绑定全部参数, false为否
    @BindingAdapter(value = {"uri", "placeholderRes",}, requireAll = false)
    public static void setImageUri(ImageView imageView, String uri, @DrawableRes int placeholderRes) {
        if (!TextUtils.isEmpty(uri)) {
            if (placeholderRes == 0) {
                Glide.with(imageView.getContext()).load(uri).into(imageView);
            } else {
                Glide.with(imageView.getContext()).load(uri).into(imageView);
            }
        }
    }
}

