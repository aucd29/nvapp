package net.sarangnamu.nvapp.view.tutorial;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import net.sarangnamu.nvapp.MainActivity;
import net.sarangnamu.nvapp.databinding.TutorialCategoryBinding;
import net.sarangnamu.nvapp.viewmodel.CategoryViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class TutorialCategory {
    private static final Logger mLog = LoggerFactory.getLogger(TutorialCategory.class);

    public static void event(@NonNull MainActivity activity, @NonNull CompositeDisposable disposable,
                             @NonNull TutorialCategoryBinding binding) {
        if (mLog.isTraceEnabled()) {
            mLog.trace("TUTORIAL CATEGORY");
        }

        CategoryViewModel categoryModel = ViewModelProviders.of(activity).get(CategoryViewModel.class);
        categoryModel.mDisposable = disposable;
        categoryModel.init(activity);

        binding.setCategoryModel(categoryModel);
    }
}
