package net.sarangnamu.nvapp.model.local.navigation;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 9. <p/>
 */
@Dao
public interface NavigationDao {
    @Insert
    void prePopulate(NavigationItem... items);

    @Query("SELECT * FROM " + NavigationItem.TABLE)
    LiveData<List<NavigationItem>> list();

    @Query("SELECT COUNT(*) FROM " + NavigationItem.TABLE)
    int count();

    @Insert
    void insert(NavigationItem item);

    @Update
    void update(NavigationItem item);

    @Query("DELETE FROM " + NavigationItem.TABLE + " WHERE _id=:id")
    void delete(int id);
}
