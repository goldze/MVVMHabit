package me.goldze.mvvmhabit.binding.viewadapter.viewgroup;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {

    @BindingAdapter({"itemView", "observableList"})
    public static void addViews(ViewGroup viewGroup, final ItemView itemView, final ObservableList<IBindingItemViewModel> viewModelList) {
        if (viewModelList != null && !viewModelList.isEmpty()) {
            viewGroup.removeAllViews();
            for (IBindingItemViewModel viewModel : viewModelList) {
                ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        itemView.layoutRes(), viewGroup, true);
                binding.setVariable(itemView.bindingVariable(), viewModel);
                viewModel.injecDataBinding(binding);
            }
        }
    }
}

