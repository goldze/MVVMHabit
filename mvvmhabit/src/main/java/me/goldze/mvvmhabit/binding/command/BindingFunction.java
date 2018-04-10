package me.goldze.mvvmhabit.binding.command;

import me.goldze.mvvmhabit.R;

/**
 * Represents a function with zero arguments.
 *
 * @param <T> the result type
 */
public interface BindingFunction<T> {
    T call();
}
