package net.sarangnamu.nvapp.model.room.navigation;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 9. <p/>
 */
@Entity(tableName = NavigationItem.TABLE)
public class NavigationItem {
    public static final String TABLE = "navigation";

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public int icon;
    public int label;

    public NavigationItem(int icon, int label) {
        this.icon  = icon;
        this.label = label;
    }
}
