package com.example.combinedsliderandcreatepost;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;

public class ImageVideoModel implements Serializable {

    private String URL;

    private boolean isImage;

    private boolean isVideo;

    private boolean isFromServer;

    private boolean isEditingInfo;

    private boolean isLoaded;


    public ImageVideoModel(String URL, boolean isImage, boolean isVideo, boolean isEditingInfo, boolean isFromServer) {
        this.URL = URL;
        this.isImage = isImage;
        this.isVideo = isVideo;
        this.isEditingInfo = isEditingInfo;
        this.isFromServer = isFromServer;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        this.isImage = image;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        this.isVideo = video;
    }

    public boolean isFromServer() {
        return isFromServer;
    }

    public void setFromServer(boolean fromServer) {
        this.isFromServer = fromServer;
    }

    public boolean isEditingInfo() {
        return isEditingInfo;
    }

    public void setEditingInfo(boolean editingInfo) {
        isEditingInfo = editingInfo;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    @BindingAdapter({"android:src"})
    public static void loadImageIntoView(ImageView view, String imageURL) {
        Glide.with(view.getContext())
                .load(imageURL)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(view);
    }
}
