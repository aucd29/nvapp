package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.hanwha.libhsp_adapter.arch.viewmodel.RecyclerViewModel;

import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.model.room.category.CategoryItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 17. <p/>
 */
public class CategoryViewModel extends RecyclerViewModel<CategoryItem> {
    private static final Logger mLog = LoggerFactory.getLogger(CategoryViewModel.class);

    public CategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(@NonNull LifecycleOwner owner) {
        DataManager.get().db().category().list().observe(owner, this::setItems);
        initAdapter("tutorial_category_item");
    }

    public void selectAll() {
        Disposable dp = DataManager.get().rxdb().subscribeOn(Schedulers.computation())
            .subscribe(db -> db.category().toggle(true, false));
    }

    public void toggle(CategoryItem item) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("TOGGLE : " + item.enable);
        }

        item.enable = !item.enable;

        Disposable dp = DataManager.get().rxdb()
            .subscribeOn(Schedulers.computation())
            .subscribe(db -> db.category().update(item));
    }
}
