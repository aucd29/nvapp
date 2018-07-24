package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import net.sarangnamu.nvapp.callback.MainCallback;
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

    public ObservableInt notificationVisible = new ObservableInt(View.GONE);
    public ObservableInt notificationCount   = new ObservableInt(0);
    public ObservableInt drawerLockMode      = new ObservableInt(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    public MainCallback mainCallback;

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

    public void showNavigation() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SHOW NAVIGATION");
        }

        if (mainCallback != null) {
            mainCallback.showNavigation();
        }
    }
}
