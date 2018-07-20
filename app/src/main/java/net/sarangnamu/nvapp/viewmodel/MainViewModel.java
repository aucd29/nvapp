package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.model.room.category.CategoryItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class MainViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(MainViewModel.class);
    
    public MutableLiveData<List<CategoryItem>> tabList = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        Disposable d = DataManager.get().rxdb()
            .observeOn(Schedulers.computation())
            .map(db -> db.category().list(true))
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(items -> tabList.postValue(items));
    }

    @BindingAdapter("bindTabLayout")
    public static void bindTabList(TabLayout tab, Object dumy) {
        Disposable d = DataManager.get().rxdb().subscribeOn(Schedulers.computation())
            .subscribe(db -> {
                List<CategoryItem> items = db.category().list(true);

                for (CategoryItem item : items) {
                    tab.addTab(tab.newTab().setText(item.label));
                }

                tab.setTabGravity(TabLayout.GRAVITY_FILL);
            });
    }

    public void showNavigation() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SHOW NAVIGATION");
        }
        
    }
}
