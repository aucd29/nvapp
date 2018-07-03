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
 * limitations under the License. */

package com.hanwha.libhsp_adapter.arch.bindingadapter;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hanwha.libhsp_adapter.arch.adapter.HspAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 5. 9.. <p/>
 */
public class RecyclerBindingAdapter {
    private static final Logger mLog = LoggerFactory.getLogger(RecyclerBindingAdapter.class);

    @BindingAdapter({"bindAdapter", "bindItems"})
    public static void bindAdapter(@NonNull RecyclerView recycler, RecyclerView.Adapter adapter, ArrayList<?> items) {
        if (mLog.isTraceEnabled()) {
            mLog.trace("BIND ADAPTER");
        }

        HspAdapter hspadapter;
        if (recycler.getAdapter() == null) {
            hspadapter = (HspAdapter) adapter;
            recycler.setAdapter(adapter);
        } else {
            hspadapter = (HspAdapter) recycler.getAdapter();
        }

        hspadapter.setItems(recycler, items);
    }
}
