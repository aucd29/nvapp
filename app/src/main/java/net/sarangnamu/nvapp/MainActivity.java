package net.sarangnamu.nvapp;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.main.nav.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationViewModel nav = ViewModelProviders.of(this).get(NavigationViewModel.class);
        UserInfoViewModel user  = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        user.init();

        ActivityMainBinding bd = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initNav(bd);

        bd.navMain.setVmodel(nav);
        bd.navMain.setUser(user);
    }

    public void initNav(@NonNull ActivityMainBinding bd) {
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 4) {
            @Override public boolean canScrollVertically() { return false; }
        };

        bd.navMain.grid.setLayoutManager(manager);
        ViewCompat.setNestedScrollingEnabled(bd.navMain.grid, false);
    }
}
