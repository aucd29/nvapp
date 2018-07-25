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

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 5. 30.. <p/>
 */
public class FragmentParams {
    public static final String ANI_LEFT  = "left";
    public static final String ANI_RIGHT = "right";
    public static final String ANI_UP    = "up";

    @IdRes
    public final int containerViewId;
    public final Bundle bundle;
    public final Class<?> fragment;
    public final String anim;
    public final boolean addMode;
    public final boolean backStack;
    public final boolean commitAllowingStateLoss;
    public final BaseFragmentManager.TransitionListener transitionListener;

    private FragmentParams(Builder builder) {
        this.containerViewId    = builder.containerViewId;
        this.bundle             = builder.bundle;
        this.fragment           = builder.fragment;
        this.anim               = builder.anim;

        this.addMode            = builder.addMode;
        this.backStack          = builder.backStack;
        this.commitAllowingStateLoss = builder.commitAllowingStateLoss;

        this.transitionListener = builder.transitionListener;
    }

    public static Builder builder() {
        return new Builder();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Builder
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static class Builder {
        @IdRes
        private int containerViewId;
        private Bundle bundle;
        private Class<?> fragment;
        private String anim = null;
        private boolean addMode = false;
        private boolean backStack = true;
        private boolean commitAllowingStateLoss = false;
        private BaseFragmentManager.TransitionListener transitionListener;

        public Builder containerId(@IdRes int containerViewId) {
            this.containerViewId = containerViewId;
            return this;
        }

        public Builder fragment(Class<?> fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder bundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public Builder backStack(boolean backStack) {
            this.backStack = backStack;
            return this;
        }

        public Builder transitionListener(BaseFragmentManager.TransitionListener transitionListener) {
            this.transitionListener = transitionListener;
            return this;
        }

        public Builder add() {
            this.addMode = true;
            return this;
        }

        public Builder replace() {
            this.addMode = false;
            return this;
        }

        public Builder animation(@NonNull String direction) {
            this.anim = direction;
            return this;
        }

        public Builder commitAllowingStateLoss(boolean commitAllowingStateLoss) {
            this.commitAllowingStateLoss = commitAllowingStateLoss;
            return this;
        }

        public FragmentParams build() {
            return new FragmentParams(this);
        }
    }
}
