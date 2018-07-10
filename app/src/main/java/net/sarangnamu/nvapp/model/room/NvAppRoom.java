package net.sarangnamu.nvapp.model.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import net.sarangnamu.nvapp.MainApp;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.room.navigation.NavigationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 9. <p/>
 */

// https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we

@Database(entities = {
    NavigationItem.class
}, version = 1, exportSchema = false)
public abstract class NvAppRoom extends RoomDatabase {
    public abstract NavigationDao navigation();
}
