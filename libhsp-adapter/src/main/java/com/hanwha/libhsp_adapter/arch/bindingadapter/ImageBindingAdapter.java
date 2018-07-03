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

package com.hanwha.libhsp_adapter.arch.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.hanwha.libhsp_adapter.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 5. 10.. <p/>
 */
public class ImageBindingAdapter {
    private static final Logger mLog = LoggerFactory.getLogger(ImageBindingAdapter.class);

    @BindingAdapter("android:src")
    public static void imageSrc(@NonNull ImageView view, @DrawableRes int resid) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("BIND IMAGE : (" + view + ") " + resid);
        }

//        Picasso.with(view.getContext()).load(resid)
////                .placeholder(R.drawable.ic_autorenew_black_24dp)
//                .into(view);

        // 리소스의 경우 피카소에서 제대로 동작하지 않아 임시적으로 수정
        view.setImageResource(resid);
    }

    @BindingAdapter("android:src")
    public static void imagePath(@NonNull ImageView view, @NonNull String imagePath) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("BIND IMAGE : " + imagePath);
        }

        Context context = view.getContext();
        File fp = new File(imagePath);

        if (!fp.exists()) {
            if (mLog.isDebugEnabled()) {
                mLog.debug("FILE NOT FOUND " + imagePath);
            }

            // TODO 나중에 default 이미지를 심던지 ..
            return ;
        }

        if (isVideo(context, fp)) {
            Picasso picasso = new Picasso.Builder(context)
                    .addRequestHandler(new VideoRequestHandler())
                    .build();

            picasso.load(VideoRequestHandler.VIDEO_SCHEME + ":" + imagePath)
                    .placeholder(R.drawable.ic_autorenew_black_24dp)
                    .into(view);
        } else {
            Picasso.with(context).load(fp)
                    .placeholder(R.drawable.ic_autorenew_black_24dp)
                    .into(view);
        }
    }

    private static boolean isVideo(@NonNull Context context, File fp) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(context, Uri.fromFile(fp));
            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);

            return "yes".equals(hasVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    static class VideoRequestHandler extends RequestHandler {
        static String VIDEO_SCHEME = "video";

        public boolean canHandleRequest(Request data) {
            String scheme = data.uri.getScheme();

            return VIDEO_SCHEME.equals(scheme);
        }

        @Override
        public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(request.uri.getPath(),
                    MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            return new RequestHandler.Result(bm, Picasso.LoadedFrom.DISK);
        }
    }
}
