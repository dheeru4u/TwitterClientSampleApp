package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by dkthaku on 6/3/16.
 */

// @Parcel(value = Parcel.Serialization.BEAN, analyze = {UrlEntity.class})
@Parcel
public class UrlEntity extends Entity  {

    /**
     * Wrapped URL, corresponding to the value embedded directly into the raw Tweet text, and the
     * values for the indices parameter.
     */
    @SerializedName("url")
    public final String url;

    /**
     * Expanded version of display_url
     */
    @SerializedName("expanded_url")
    public final String expandedUrl;

    /**
     * Version of the URL to display to clients.
     */
    @SerializedName("display_url")
    public final String displayUrl;

    public UrlEntity(String url, String expandedUrl, String displayUrl, int start, int end) {
        super(start, end);

        this.url = url;
        this.expandedUrl = expandedUrl;
        this.displayUrl = displayUrl;
    }

  //  /*
    public UrlEntity() {

        this.url = null;
        this.expandedUrl = null;
        this.displayUrl = null;
    }
   // */

    public String getUrl() {
        return url;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    @Override
    public String toString() {
        return "UrlEntity{" +
                "url='" + url + '\'' +
                ", expandedUrl='" + expandedUrl + '\'' +
                ", displayUrl='" + displayUrl + '\'' +
                '}';
    }


}
