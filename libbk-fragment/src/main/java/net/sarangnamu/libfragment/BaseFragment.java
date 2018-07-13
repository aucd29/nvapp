/*
 * Copyright (C) Hanwha System Corp. 2018. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sarangnamu.libfragment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 5. 28.. <p/>
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    protected T mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false);

        return mBinding.getRoot();
    }

    @LayoutRes
    protected abstract int layoutId();

    protected <VM extends ViewModel> VM viewModel(@NonNull Class<VM> modelClass) {
        return ViewModelProviders.of(getActivity()).get(modelClass);
    }

    protected <VB extends ViewDataBinding> VB inflate(@LayoutRes int layoutId, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return DataBindingUtil.inflate(inflater, layoutId, parent, true);
    }
}
