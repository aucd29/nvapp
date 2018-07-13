package net.sarangnamu.nvapp.model;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import net.sarangnamu.nvapp.MainApp;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.room.NvAppRoom;
import net.sarangnamu.nvapp.model.room.navigation.NavigationItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 3. <p/>
 *
 * https://stackoverflow.com/questions/45022813/android-room-orm-after-have-initial-values
 */
public class DataManager {
    private static final Logger mLog = LoggerFactory.getLogger(DataManager.class);

    private static DataManager mInst;

    private NvAppRoom mDb;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public static DataManager get() {
        if (mInst == null) {
            mInst = new DataManager();
        }

        return mInst;
    }

    private DataManager() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("INIT DATA MANAGER");
        }

        init();
        // https://stackoverflow.com/questions/47619718/room-database-not-created
        mDisposable.add(rxdb().subscribe(db -> db.navigation().count()));
    }

    private void init() {
        if (mDb == null) {
            mDb = Room.databaseBuilder(MainApp.context, NvAppRoom.class, "nvapp.db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        if (mLog.isDebugEnabled()) {
                            mLog.debug("ROOM CREATE");
                        }

                        mDisposable.add(rxdb().subscribe(nvAppRoom ->
                            nvAppRoom.navigation().prePopulate(populateNavigationData()) ));
                    }
                })
                .build();
        }
    }

    public Single<NvAppRoom> rxdb() {
        init();
        return Single.just(mDb).subscribeOn(Schedulers.io());
    }

    public NvAppRoom db() {
        init();

        return mDb;
    }

    private NavigationItem[] populateNavigationData() {
        List<NavigationItem> dataList = new ArrayList<>();
        dataList.add(new NavigationItem(R.string.fa_envelope, R.string.nav_grid_mail));
        dataList.add(new NavigationItem(R.string.fa_sticky_note, R.string.nav_grid_facing));
        dataList.add(new NavigationItem(R.string.fa_bookmark, R.string.nav_grid_bookmark));
        dataList.add(new NavigationItem(R.string.fa_comment_dots, R.string.nav_grid_talk));
        dataList.add(new NavigationItem(R.string.fa_coffee, R.string.nav_grid_cafe));
        dataList.add(new NavigationItem(R.string.fa_cube, R.string.nav_grid_blog));
        dataList.add(new NavigationItem(R.string.fa_store, R.string.nav_grid_knowledge));
        dataList.add(new NavigationItem(R.string.fa_shopping_bag, R.string.nav_grid_shopping));
        dataList.add(new NavigationItem(R.string.fa_won_sign, R.string.nav_grid_webtoon));
        dataList.add(new NavigationItem(R.string.fa_bug, R.string.nav_grid_lab));

        return dataList.toArray(new NavigationItem[dataList.size()]);
    }

    public void destroy() {
        mDisposable.clear();
        mDisposable.dispose();
    }
}
