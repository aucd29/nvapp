package net.sarangnamu.common.widget;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected T mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, layoutId());
    }

    @LayoutRes
    protected abstract int layoutId();

    protected <VM extends ViewModel> VM viewModel(@NonNull Class<VM> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }
}
