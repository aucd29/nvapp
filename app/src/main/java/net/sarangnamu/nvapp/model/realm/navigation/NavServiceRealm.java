package net.sarangnamu.nvapp.model.realm.navigation;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import io.realm.RealmObject;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 3. <p/>
 */
public class NavServiceRealm extends RealmObject {
    public int icon;
    public int label;

    public NavServiceRealm() {}

    public NavServiceRealm(@StringRes int icon, @StringRes int label) {
        this.icon  = icon;
        this.label = label;
    }
}
