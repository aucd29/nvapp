package net.sarangnamu.nvapp.main.nav.viewmodel;

import android.app.Application;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.hanwha.libhsp_adapter.arch.viewmodel.RecyclerViewModel;

import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.Model;
import net.sarangnamu.nvapp.model.local.navigation.NavServiceItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public class NavigationViewModel extends RecyclerViewModel<NavServiceItem> {
    private static final Logger mLog = LoggerFactory.getLogger(NavigationViewModel.class);
    
    public ObservableInt loginMsg         = new ObservableInt(R.string.nav_pls_login);
    public ObservableInt loginMsgVisible  = new ObservableInt(View.VISIBLE);
    public ObservableInt userIdVisible    = new ObservableInt(View.GONE);
    public ObservableInt userEmailVisible = new ObservableInt(View.GONE);
    public ObservableField<Spanned> encouragingLogin = new ObservableField<>();

    public NavigationViewModel(Application app) {
        super(app);

        encouragingLogin();
        setItems(Model.get().navGridList());
        initAdapter("nav_grid_item");
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

    private String string(@StringRes int resid) {
        return getApplication().getString(resid);
    }
}
