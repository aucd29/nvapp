package net.sarangnamu.libtutorial;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import net.sarangnamu.libcore.OnResultListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public final class TutorialParams implements Serializable {
    public final List<Integer> viewList;
    public final OnResultListener<Integer> finishedListener;
    public final OnResultListener<ViewDataBinding> viewDataBindingListener;

    private TutorialParams(final Builder builder) {
        this.viewList       = builder.viewList;
        this.finishedListener = builder.finishedListener;
        this.viewDataBindingListener = builder.viewDataBindingListener;
    }

    public Bundle bundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TutorialFragment.PARAMS, this);

        return bundle;
    }

    public static Builder builder() {
        return new Builder();
    }

    public final static class Builder {
        private List<Integer> viewList = new ArrayList<>();
        private OnResultListener<Integer> finishedListener = null;
        private OnResultListener<ViewDataBinding> viewDataBindingListener = null;

        private Builder() {

        }

        public Builder view(@LayoutRes int layoutId) {
            this.viewList.add(layoutId);
            return this;
        }

        public Builder viewList(@NonNull List<Integer> viewList) {
            this.viewList = viewList;
            return this;
        }

        public Builder finishedListener(@NonNull OnResultListener<Integer> finishedListener) {
            this.finishedListener = finishedListener;
            return this;
        }

        public Builder viewDataBindingListener(@NonNull OnResultListener<ViewDataBinding> viewDataBindingListener) {
            this.viewDataBindingListener = viewDataBindingListener;
            return this;
        }

        public TutorialParams build() {
            return new TutorialParams(this);
        }
    }
}
