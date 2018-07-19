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

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
        if (mLog.isDebugEnabled()) {
            mLog.debug("BIND ADAPTER (" + recycler.getId() + ") ITEM COUNT (" + items.size() + ")");
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

    @BindingAdapter({"bindHorDecoration", "bindVerDecoration"})
    public static void bindDecorator(@NonNull RecyclerView recycler, int horDrawable, int verDrawable) {
        //https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview

        if (mLog.isDebugEnabled()) {
            mLog.debug("BIND DECORATION: hor(" + horDrawable + "), ver(" + verDrawable + ")");
        }

        final Context context = recycler.getContext();

        if (horDrawable > 0) {
            DividerItemDecoration hdecorator = new DividerItemDecoration(
                context, DividerItemDecoration.HORIZONTAL);
            hdecorator.setDrawable(ContextCompat.getDrawable(context, horDrawable));

            recycler.addItemDecoration(hdecorator);
        }

        if (verDrawable > 0) {
            DividerItemDecoration vdecorator = new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL);
            vdecorator.setDrawable(ContextCompat.getDrawable(context, verDrawable));

            recycler.addItemDecoration(vdecorator);
        }
    }

    @BindingAdapter("bindLockedGridLayoutManager")
    public static void bindLockedGridLayoutManager(@NonNull RecyclerView recycler, int spancount) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("BIND GRID LAYOUT: SPAN COUNT(" + spancount + ")");
        }

        final Context context = recycler.getContext();

        recycler.setLayoutManager(new GridLayoutManager(context, spancount) {
            @Override public boolean canScrollVertically() { return false; }
        });

        ViewCompat.setNestedScrollingEnabled(recycler, false);
    }
}
