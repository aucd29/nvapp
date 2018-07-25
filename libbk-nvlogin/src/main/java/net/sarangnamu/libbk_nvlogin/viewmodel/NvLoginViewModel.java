package net.sarangnamu.libbk_nvlogin.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import net.sarangnamu.libcore.SharedPref;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class NvLoginViewModel extends AndroidViewModel {
    private static final String K_CLIENT_ID     = "nv_login_client_id";
    private static final String K_CLIENT_SECRET = "nv_login_client_secret";
    private static final String K_CLIENT_NAME   = "nv_login_client_name";

    private SharedPref mPref;

    public NvLoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(@NonNull String clientId,
                     @NonNull String clientSecret, @NonNull String clientName) {

        mPref = SharedPref.create(getApplication());
        mPref.set(K_CLIENT_ID, clientId);
        mPref.set(K_CLIENT_SECRET, clientSecret);
        mPref.set(K_CLIENT_NAME, clientName);
    }

    public void oauth() {

    }

    public void login() {

    }

    public void logout() {

    }


}
