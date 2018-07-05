package net.sarangnamu.nvapp.model;


import net.sarangnamu.nvapp.BuildConfig;
import net.sarangnamu.nvapp.MainApp;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.model.local.navigation.NavServiceItem;
import net.sarangnamu.nvapp.model.realm.navigation.NavServiceRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 3. <p/>
 */
public class DataManager {
    private static DataManager mInst;

    public static DataManager get() {
        if (mInst == null) {
            mInst = new DataManager();
        }

        return mInst;
    }

    private DataManager() {
        Realm.init(MainApp.context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
        .initialData(m -> {
            initNavGrid(m);
        })
        .deleteRealmIfMigrationNeeded().build());

        if (BuildConfig.DEBUG) {
//            RealmLog.add(new AndroidLogger(Log.VERBOSE));
        }
    }

    private void initNavGrid(Realm m) {
        List<NavServiceRealm> dataList = new ArrayList<>();
        dataList.add(new NavServiceRealm(R.string.fa_envelope, R.string.nav_grid_mail));
        dataList.add(new NavServiceRealm(R.string.fa_sticky_note, R.string.nav_grid_facing));
        dataList.add(new NavServiceRealm(R.string.fa_bookmark, R.string.nav_grid_bookmark));
        dataList.add(new NavServiceRealm(R.string.fa_comment_dots, R.string.nav_grid_talk));
        dataList.add(new NavServiceRealm(R.string.fa_coffee, R.string.nav_grid_cafe));
        dataList.add(new NavServiceRealm(R.string.fa_cube, R.string.nav_grid_blog));
        dataList.add(new NavServiceRealm(R.string.fa_store, R.string.nav_grid_knowledge));
        dataList.add(new NavServiceRealm(R.string.fa_shopping_bag, R.string.nav_grid_shopping));
        dataList.add(new NavServiceRealm(R.string.fa_won_sign, R.string.nav_grid_webtoon));
        dataList.add(new NavServiceRealm(R.string.fa_bug, R.string.nav_grid_lab));

        m.insertOrUpdate(dataList);
    }

    public List<NavServiceItem> navGridList() {
        // REALM 의 실제 객체가 NavServiceRealmRealmProxy 형태로 구성되어 있어서 바로 사용할수가 없는 문제 존재
        Realm m = Realm.getDefaultInstance();
        List<NavServiceRealm> list = m.where(NavServiceRealm.class).findAll();
        List<NavServiceItem> items = new ArrayList<>();
        for (NavServiceRealm data : list) {
            items.add(new NavServiceItem(data));
        }

        return items;
    }
}
