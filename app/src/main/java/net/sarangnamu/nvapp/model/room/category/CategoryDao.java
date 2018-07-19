package net.sarangnamu.nvapp.model.room.category;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 17. <p/>
 */
@Dao
public interface CategoryDao {
    @Insert
    void prePopulate(CategoryItem... items);

    @Query("SELECT * FROM " + CategoryItem.TABLE)
    List<CategoryItem> list();

    @Query("UPDATE category SET enable=:setEnableValue WHERE enable IN (SELECT _id FROM category WHERE enable=:getEnableValue)")
    void toggle(boolean setEnableValue, boolean getEnableValue);

    @Insert
    void insert(CategoryItem item);

    @Update
    void update(CategoryItem item);

    @Query("DELETE FROM " + CategoryItem.TABLE + " WHERE _id=:id")
    void delete(int id);
}
