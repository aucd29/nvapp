package net.sarangnamu.nvapp.model;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import net.sarangnamu.nvapp.MainApp;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.room.NvAppRoom;
import net.sarangnamu.nvapp.model.room.category.CategoryItem;
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

    }

    public void init(@NonNull Context context) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("INIT DATA MANAGER");
        }

        if (mDb == null) {
            mDb = Room.databaseBuilder(context, NvAppRoom.class, "nvapp.db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        if (mLog.isDebugEnabled()) {
                            mLog.debug("ROOM CREATE");
                        }

                        mDisposable.add(rxdb().subscribe(nvAppRoom -> {
                                nvAppRoom.navigation().prePopulate(populateNavigationData());
                                nvAppRoom.category().prePopulate(popuplateCategoryData());
                            }));
                    }
                })
                .build();

            // https://stackoverflow.com/questions/47619718/room-database-not-created
            mDisposable.add(rxdb().subscribe(db -> db.navigation().count()));
        }
    }

    public Single<NvAppRoom> rxdb() {
        return Single.just(mDb).subscribeOn(Schedulers.io());
    }

    public NvAppRoom db() {
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

    private CategoryItem[] popuplateCategoryData() {
        List<CategoryItem> dataList = new ArrayList<>();

        dataList.add(new CategoryItem("뉴스", "NEWS", true));
        dataList.add(new CategoryItem("연예", "ENT", true));
        dataList.add(new CategoryItem("스포츠", "SPORTS", true));
        dataList.add(new CategoryItem("쇼핑", "SHOPPING", true));
        dataList.add(new CategoryItem("우리동네", "PLACE", true));

        dataList.add(new CategoryItem("뿜", false));
        dataList.add(new CategoryItem("웹툰", false));
        dataList.add(new CategoryItem("영화", false));
        dataList.add(new CategoryItem("테크", false));
        dataList.add(new CategoryItem("경제M", false));
        dataList.add(new CategoryItem("리빙", false));
        dataList.add(new CategoryItem("어학당", false));
        dataList.add(new CategoryItem("푸드", false));
        dataList.add(new CategoryItem("패션", false));
        dataList.add(new CategoryItem("자동차", false));
        dataList.add(new CategoryItem("건강", false));
        dataList.add(new CategoryItem("여행", false));
        dataList.add(new CategoryItem("충전", false));
        dataList.add(new CategoryItem("직업", false));
        dataList.add(new CategoryItem("책문화", false));
        dataList.add(new CategoryItem("게임", false));
        dataList.add(new CategoryItem("과학", false));
        dataList.add(new CategoryItem("부모", false));
        dataList.add(new CategoryItem("동물공감", false));
        dataList.add(new CategoryItem("FARM", false));
        dataList.add(new CategoryItem("중국", false));
        dataList.add(new CategoryItem("법률", false));
        dataList.add(new CategoryItem("디자인", false));
        dataList.add(new CategoryItem("비즈니스", false));
        dataList.add(new CategoryItem("연얘 결혼", false));
        dataList.add(new CategoryItem("동영상", false));
        dataList.add(new CategoryItem("공연전시", false));
        dataList.add(new CategoryItem("뮤직", false));
        dataList.add(new CategoryItem("함께", false));
        dataList.add(new CategoryItem("검색", false));
        dataList.add(new CategoryItem("마이구독", false));
        dataList.add(new CategoryItem("스쿨", false));

        return dataList.toArray(new CategoryItem[dataList.size()]);
    }

    public void destroy() {
        mDisposable.clear();
        mDisposable.dispose();
    }
}
