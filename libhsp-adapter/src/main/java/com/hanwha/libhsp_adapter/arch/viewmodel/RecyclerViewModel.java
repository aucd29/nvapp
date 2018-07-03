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

package com.hanwha.libhsp_adapter.arch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;


import com.hanwha.libhsp_adapter.arch.adapter.HspAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 5. 11.. <p/>
 */
public abstract class RecyclerViewModel<T> extends AndroidViewModel {
    public ObservableArrayList<T> items = new ObservableArrayList<>();
    public ObservableField<HspAdapter<T>> adapter = new ObservableField<>();

    public RecyclerViewModel(@NonNull Application application) {
        super(application);
    }

    public void initAdapter(@NonNull String id) {
        HspAdapter<T> adapter = new HspAdapter<>(id);
        adapter.setViewModel(this);

        this.adapter.set(adapter);
    }

    public void initAdapter(@NonNull String[] ids) {
        HspAdapter<T> adapter = new HspAdapter<>(ids);
        adapter.setViewModel(this);

        this.adapter.set(adapter);
    }

    public void setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
