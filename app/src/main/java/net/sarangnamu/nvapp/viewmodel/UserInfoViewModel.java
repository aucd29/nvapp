package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.remote.user.UserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public class UserInfoViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(UserInfoViewModel.class);

    public ObservableField<UserInfo> info = new ObservableField<>();

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        // LOGIN


        // LOGOUT
        if (mLog.isDebugEnabled()) {
            mLog.debug("LOGOUT INFO");
        }

        UserInfo userinfo = new UserInfo();
        userinfo.pic = ContextCompat.getDrawable(getApplication(), R.drawable.ic_android_black_24dp);

        info.set(userinfo);
    }
}
