package net.sarangnamu.nvapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;

import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.main.nav.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationViewModel nav = ViewModelProviders.of(this).get(NavigationViewModel.class);
        UserInfoViewModel user  = ViewModelProviders.of(this).get(UserInfoViewModel.class);

        nav.init(this);
        user.init();

        mRoot = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mRoot.navMain.setVmodel(nav);
        mRoot.navMain.setUser(user);

        // TEST CODE https://stackoverflow.com/questions/32583169/how-to-show-hide-navigation-drawer-programmatically
        mRoot.drawerLayout.post(() -> {
            mRoot.drawerLayout.openDrawer(Gravity.START);
        });
    }

    @Override
    public void onBackPressed() {
        if (mRoot.drawerLayout.isDrawerOpen(Gravity.START)) {
            mRoot.drawerLayout.closeDrawer(Gravity.START);
            return ;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DataManager.get().destroy();

        super.onDestroy();
    }
}
