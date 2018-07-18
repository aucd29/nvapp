package net.sarangnamu.nvapp.model.room.category;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.hanwha.libhsp_adapter.arch.adapter.IHspDiff;
import com.hanwha.libhsp_adapter.arch.adapter.IHspItem;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 17. <p/>
 */
@Entity(tableName = CategoryItem.TABLE)
public class CategoryItem implements IHspDiff, IHspItem {
    public static final String TABLE = "category";
    
    public static final int T_DEFAULT = 0;

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String label;

    public boolean enable;

    public CategoryItem(@NonNull String label, boolean enable) {
        this.label = label;
        this.enable = enable;
    }

    @Override
    public boolean compare(Object item) {
        CategoryItem obj = (CategoryItem) item;
        return label.equals(obj.label) && enable == obj.enable;
    }

    @Override
    public int type() {
        return T_DEFAULT;
    }
}
