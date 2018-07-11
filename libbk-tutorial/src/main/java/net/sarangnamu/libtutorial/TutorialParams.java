package net.sarangnamu.libtutorial;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import net.sarangnamu.libcore.OnResultListener;
import net.sarangnamu.libtutorial.viewmodel.BaseTutorialViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public final class TutorialParams implements Serializable {
    public final List<Integer> viewList;
    public final OnResultListener<Integer> resultListener;
    public final BaseTutorialViewModel viewModel;

    private TutorialParams(final Builder builder) {
        this.viewList       = builder.viewList;
        this.resultListener = builder.resultListener;
        this.viewModel      = builder.viewModel;
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
        private OnResultListener<Integer> resultListener = null;
        private BaseTutorialViewModel viewModel;

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

        public Builder resultListener(@NonNull OnResultListener<Integer> resultListener) {
            this.resultListener = resultListener;
            return this;
        }

        public Builder viewModel(@NonNull BaseTutorialViewModel viewModel) {
            this.viewModel = viewModel;
            return this;
        }

        public TutorialParams build() {
            return new TutorialParams(this);
        }
    }
}
