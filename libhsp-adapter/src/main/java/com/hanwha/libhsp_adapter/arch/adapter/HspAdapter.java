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

package com.hanwha.libhsp_adapter.arch.adapter;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 5. 8.. <p/>
 */
public class HspAdapter<ITEM> extends RecyclerView.Adapter<HspViewHolder> {
    private static final Logger mLog = LoggerFactory.getLogger(HspAdapter.class);

    private static final String METHOD_NAME_VIEW_MODEL = "setVmodel";
    private static final String METHOD_NAME_ITEM = "setItem";
    private static final String METHOD_NAME_BIND = "bind";

    /** recycler view 에 출력할 아이템 리스트 */
    private List<ITEM> mItems;
    /** row 에 해당하는 xml 에 등록될 view model */
    private ViewModel mViewModel;
    /** row 에 해당하는 xml 파일명 */
    private String[] mLayouts;

    public HspAdapter(@NonNull String layoutId) {
        mLayouts = new String[] { layoutId };
    }

    public HspAdapter(@NonNull String[] layoutIds) {
        mLayouts = layoutIds;
    }

    @NonNull
    @Override
    public HspViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId    = context.getResources().getIdentifier(mLayouts[viewType], "layout", context.getPackageName());
        View view       = LayoutInflater.from(context).inflate(layoutId, parent, false);

        if (mLog.isTraceEnabled()) {
            mLog.trace(String.format(Locale.getDefault(), "LAYOUT ID : %s (%d)", mLayouts[viewType], layoutId));
        }

        // view binding class 는 file name 에 의해 생성되므로 hsp adapter 의 생성자에서 전달 받은 파일명을
        // 기준으로 binding class 를 얻는다.
        try {
            ViewDataBinding binding;
            String bindingClassPath = bindClassName(context, mLayouts[viewType]);

            if (mLog.isTraceEnabled()) {
                mLog.trace("BINDING CLASS : " + bindingClassPath);
            }

            Class<?> bindingClass = Class.forName(bindingClassPath);
            Method method = bindingClass.getDeclaredMethod(METHOD_NAME_BIND, new Class<?>[]{ View.class });
            binding = (ViewDataBinding) method.invoke(null, new Object[] { view });

            HspViewHolder vh = new HspViewHolder(view);
            vh.mBinding = binding;

            return vh;
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HspViewHolder holder, int position) {
        if (mViewModel != null) {
            invokeMethod(holder.mBinding, METHOD_NAME_VIEW_MODEL, mViewModel.getClass(), mViewModel, false);
        }

        if (mItems != null) {
            ITEM item = mItems.get(position);
            invokeMethod(holder.mBinding, METHOD_NAME_ITEM, item.getClass(), item, true);
        }

        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }

        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems == null) {
            return 0;
        }

        ITEM item = mItems.get(position);
        if (item instanceof IHspItem) {
            return ((IHspItem) item).type();
        }

        return 0;
    }

    public void setItems(final RecyclerView recycler, final List<ITEM> items) {
        if (mItems == null) {
            mItems = new ArrayList<>();
            mItems.addAll(items);
            notifyItemRangeChanged(0, items.size());

            return ;
        }

        final List<ITEM> oldItems = mItems;

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItems.get(oldItemPosition).equals(items.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ITEM oldItem = oldItems.get(oldItemPosition);
                ITEM newItem = items.get(newItemPosition);

                if (oldItem instanceof IHspDiff) {
                    return ((IHspDiff) oldItem).compare(newItem);
                }

                return oldItem.equals(newItem);
            }
        });

        // https://stackoverflow.com/questions/43458146/diffutil-in-recycleview-making-it-autoscroll-if-a-new-item-is-added
        result.dispatchUpdatesTo(new ListUpdateCallback() {
            private int mFirstInsert = -1;

            @Override
            public void onInserted(int position, int count) {
                if (mFirstInsert == -1 || mFirstInsert > position) {
                    mFirstInsert = position;
                    recycler.smoothScrollToPosition(mFirstInsert);
                }

                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                notifyItemRangeChanged(position, count, payload);
            }
        });

//        result.dispatchUpdatesTo(this);

        mItems.clear();
        mItems.addAll(items);
    }

    public void setViewModel(ViewModel model) {
        mViewModel = model;
    }

    private static String bindClassName(@NonNull Context context, @NonNull String layoutId) {
        StringBuilder sb = new StringBuilder();

        sb.append(context.getPackageName());
        sb.append(".databinding.");
        sb.append(Character.toUpperCase(layoutId.charAt(0)));

        for (int i = 1; i < layoutId.length(); ++i) {
            char c = layoutId.charAt(i);

            if (c == '_') {
                c = layoutId.charAt(++i);
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
        }

        sb.append("Binding");

        return sb.toString();
    }

    protected static void invokeMethod(ViewDataBinding binding, String methodName, Class<?> argType, Object argValue, boolean log) {
        try {
            Method method = binding.getClass().getDeclaredMethod(methodName, new Class<?>[] { argType });
            method.invoke(binding, new Object[] { argValue });
        } catch (Exception ignored) {
            if (log) {
                mLog.debug("NOT FOUND : " + ignored.getMessage());
            }
        }
    }
}
