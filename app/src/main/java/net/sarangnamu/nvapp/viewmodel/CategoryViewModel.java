package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hanwha.libhsp_adapter.arch.viewmodel.RecyclerViewModel;

import net.sarangnamu.libdingbat.widget.AwesomeSolid;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.model.local.category.CategoryItem;

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
        Disposable dp = DataManager.get().rxdb()
            .subscribeOn(Schedulers.computation())
            .subscribe(db -> setItems(db.category().list()));
        initAdapter("tutorial_category_item");
    }

    public void selectAll() {
        Disposable dp = DataManager.get().rxdb().subscribeOn(Schedulers.computation())
            .subscribe(db -> db.category().toggle(true, false));
    }

    public void toggle(View view, CategoryItem item) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("TOGGLE : " + item.enable);
        }

        item.enable = !item.enable;
        this.adapter.get().notifyItemChanged(item.position());

        Disposable dp = DataManager.get().rxdb()
            .subscribeOn(Schedulers.computation())
            .subscribe(db -> db.category().update(item));
    }

    @BindingAdapter("bindCategoryBackground")
    public static void bindCategoryBg(View view, Boolean enable) {
        if (enable) {
            view.setBackgroundResource(R.drawable.shape_category_enable);
        } else {
            view.setBackgroundResource(R.drawable.shape_category_disable);
        }
    }

    @BindingAdapter("bindCategoryIconBackground")
    public static void bindCategoryButtonBg(AwesomeSolid view, Boolean enable) {
        if (enable) {
            view.setText(R.string.fa_check);
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));

            view.setScaleX(.1f);
            view.setScaleY(.1f);
            view.animate().scaleX(1f).scaleY(1f).start();
        } else {
            view.setText(R.string.fa_plus);
            view.setTextColor(0xffdedede);

            view.setScaleX(1);
            view.setScaleY(1);
        }
    }
}
