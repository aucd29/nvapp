package net.sarangnamu.nvapp.model.local.navigation;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.hanwha.libhsp_adapter.arch.adapter.IHspDiff;
import com.hanwha.libhsp_adapter.arch.adapter.IHspItem;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 9. <p/>
 */
@Entity(tableName = NavigationItem.TABLE)
public final class NavigationItem implements IHspDiff, IHspItem {
    public static final String TABLE = "navigation";

    public static final int T_DEFAULT = 0;
    public static final int T_PLUS = 1;

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public int icon;
    public int label;

    @Ignore
    public int type = T_DEFAULT;

    public NavigationItem(int icon, int label) {
        this.icon  = icon;
        this.label = label;
    }

    public NavigationItem(int type) {
        this.type = type;
    }

    @Override
    public boolean compare(Object item) {
        NavigationItem obj = (NavigationItem) item;
        return icon == obj.icon && label == obj.label && type == obj.type;
    }

    @Override
    public int type() {
        return type;
    }
}
