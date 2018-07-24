package net.sarangnamu.nvapp.model.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import net.sarangnamu.nvapp.model.local.category.CategoryDao;
import net.sarangnamu.nvapp.model.local.category.CategoryItem;
import net.sarangnamu.nvapp.model.local.navigation.NavigationDao;
import net.sarangnamu.nvapp.model.local.navigation.NavigationItem;


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 9. <p/>
 */

// https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we

@Database(entities = {
    NavigationItem.class,
    CategoryItem.class
}, version = 1, exportSchema = false)
public abstract class NvAppRoom extends RoomDatabase {
    public abstract NavigationDao navigation();
    public abstract CategoryDao category();
}
