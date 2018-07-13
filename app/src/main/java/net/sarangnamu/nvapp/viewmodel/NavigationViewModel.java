package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.hanwha.libhsp_adapter.arch.viewmodel.RecyclerViewModel;

import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.model.room.navigation.NavigationItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public class NavigationViewModel extends RecyclerViewModel<NavigationItem> {
    private static final Logger mLog = LoggerFactory.getLogger(NavigationViewModel.class);
    
    public ObservableInt loginMsg         = new ObservableInt(R.string.nav_pls_login);
    public ObservableInt loginMsgVisible  = new ObservableInt(View.VISIBLE);
    public ObservableInt userIdVisible    = new ObservableInt(View.GONE);
    public ObservableInt userEmailVisible = new ObservableInt(View.GONE);
    public ObservableField<Spanned> encouragingLogin = new ObservableField<>();

    public ObservableInt horDecoration    = new ObservableInt(R.drawable.shape_divider_hor);
    public ObservableInt verDecoration    = new ObservableInt(R.drawable.shape_divider_ver);
    public ObservableInt spanCount        = new ObservableInt(4);

    public NavigationViewModel(Application app) {
        super(app);

        encouragingLogin();
    }

    public void init(@NonNull LifecycleOwner owner) {
        DataManager.get().db().navigation().list().observe(owner, it -> {
            if (mLog.isDebugEnabled()) {
                mLog.debug("NAVIGATION ITEM COUNT : " + it.size());
            }

            ArrayList<NavigationItem> list = new ArrayList<>(it);
            list.add(new NavigationItem(NavigationItem.T_PLUS)); // 마지막에 add 버튼을 추가

            setItems(list);
        });
        initAdapter(new String[] {"nav_grid_item", "nav_grid_plus"});
    }

    public void close() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("close");
        }
    }

    public void notification() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("notification");
        }
    }

    public void setting() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("setting");
        }
    }

    public void gridReset() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("RESET");
        }
    }

    public void gridOrderBy() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("ORDER BY");
        }
    }

    public void allServices() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("ALL SERVICES");
        }
        
    }
    
    public void startPay() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("START PAY");
        }

    }
    
    public void banner() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("BANNER ");
        }
        
    }

    public void login() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("LOGIN");
        }

    }

    public void notice() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("NOTICE");
        }
    }

    public void clickService(int labelId) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("CLICK SERVICE : " + string(labelId));
        }
        
    }
    
    public void clickAddService() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("ADD SERVICE");
        }
        
    }

    private void encouragingLogin() {
        String msg = string(R.string.nav_encouraging_login);
        Spanned html;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            html = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY);
        } else {
            html = Html.fromHtml(msg);
        }

        encouragingLogin.set(html);
    }
}
