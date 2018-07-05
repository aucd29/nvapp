package net.sarangnamu.nvapp.model.local.navigation;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.hanwha.libhsp_adapter.arch.adapter.IHspDiff;

import net.sarangnamu.nvapp.model.realm.navigation.NavServiceRealm;

import io.realm.RealmObject;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 3. <p/>
 */
public class NavServiceItem implements IHspDiff {
    public int icon;
    public int label;

    public NavServiceItem(NavServiceRealm m) {
        this.icon  = m.icon;
        this.label = m.label;
    }

    public NavServiceItem(@StringRes int icon, @StringRes int label) {
        this.icon  = icon;
        this.label = label;
    }

    @Override
    public boolean compare(Object item) {
        NavServiceItem newItem = (NavServiceItem) item;
        return icon == newItem.icon && label == newItem.label;
    }
}
