package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dkthaku on 6/8/16.
 */
public class Image {

    @SerializedName("w")
    public final int w;

    @SerializedName("h")
    public final int h;

    @SerializedName("image_type")
    public final String imageType;

    public Image(int w, int h, String imageType) {
        this.w = w;
        this.h = h;
        this.imageType = imageType;
    }
}
