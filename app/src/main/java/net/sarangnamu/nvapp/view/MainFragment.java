package net.sarangnamu.nvapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.sarangnamu.libfragment.BaseFragment;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.databinding.LayoutMainBinding;
import net.sarangnamu.nvapp.model.room.category.CategoryItem;
import net.sarangnamu.nvapp.viewmodel.MainViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class MainFragment extends BaseFragment<LayoutMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(MainFragment.class);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mLog.isDebugEnabled()) {
            mLog.debug("CREATE");
        }

        MainViewModel vmodel = viewModel(MainViewModel.class);
        vmodel.init();
        vmodel.tabList.observe(this, list -> {
            if (mLog.isDebugEnabled()) {
                mLog.debug("TAB COUNT : " + list.size());
            }
            mBinding.viewpager.setAdapter(new MainPageAdapter(getChildFragmentManager(), list));
        });

        mBinding.tab.setupWithViewPager(mBinding.viewpager);
        mBinding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewpager.setCurrentItem(tab.getPosition(), true);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        vmodel.notificationCount(2);

        mBinding.setVmodel(vmodel);
    }

    @Override
    protected int layoutId() {
        return R.layout.layout_main;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // WebFragment
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static class WebFragment extends Fragment {
        private WebView mWeb;

        public static WebFragment create(String name) {
            Bundle args = new Bundle();
            args.putString("name", name);

            WebFragment frgmt = new WebFragment();
            frgmt.setArguments(args);

            return frgmt;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            String name = getArguments().getString("name");

            if (mLog.isDebugEnabled()) {
                mLog.debug("NAME : " + name);
            }

            mWeb = new WebView(inflater.getContext());
            mWeb.setWebChromeClient(new WebChromeClient() {

            });
            mWeb.setWebViewClient(new WebViewClient() {

            });
            mWeb.loadUrl("http://m.naver.com");
            mWeb.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

            return mWeb;
        }
    }

    public class MainPageAdapter extends FragmentPagerAdapter {
        List<CategoryItem> mList;

        MainPageAdapter(FragmentManager fm, List<CategoryItem> list) {
            super(fm);

            mList = list;
        }

        @Override
        public Fragment getItem(int pos) {
            CategoryItem item = mList.get(pos);

            return WebFragment.create(item.label);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (mList == null || mList.get(position) == null) {
                return "Unknown";
            }

            return mList.get(position).label;
        }
    }

}
