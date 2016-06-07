package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by dkthaku on 6/3/16.
 */
@Parcel //(value = Parcel.Serialization.BEAN, analyze = {MediaEntity.class})
public class MediaEntity extends UrlEntity   {

    /**
     * ID of the media expressed as a 64-bit integer.
     */
    @SerializedName("id")
    public final long id;

    /**
     * ID of the media expressed as a string.
     */
    @SerializedName("id_str")
    public final String idStr;

    /**
     * A http:// URL pointing directly to the uploaded media file.
     *
     * For media in direct messages, media_url is the same https URL as media_url_https and must be
     * accessed via an authenticated twitter.com session or by signing a request with the user's
     * access token using OAuth 1.0A. It is not possible to directly embed these images in a web
     * page.
     */
    @SerializedName("media_url")
    public final String mediaUrl;

    /**
     * A https:// URL pointing directly to the uploaded media file, for embedding on https pages.
     *
     * For media in direct messages, media_url_https must be accessed via an authenticated
     * twitter.com session or by signing a request with the user's access token using OAuth 1.0A.
     * It is not possible to directly embed these images in a web page.
     */
    @SerializedName("media_url_https")
    public final String mediaUrlHttps;

    /**
     * An object showing available sizes for the media file.
     */
    @SerializedName("sizes")
    public final Sizes sizes;

    /**
     * For Tweets containing media that was originally associated with a different tweet, this ID
     * points to the original Tweet.
     */
    @SerializedName("source_status_id")
    public final long sourceStatusId;

    /**
     * For Tweets containing media that was originally associated with a different tweet, this
     * string-based ID points to the original Tweet.
     */
    @SerializedName("source_status_id_str")
    public final String sourceStatusIdStr;

    /**
     * Type of uploaded media.
     */
    @SerializedName("type")
    public final String type;

    /**
     * An object showing details for the video file. This field is present only when there is a
     * video in the payload.
     */
    @SerializedName("video_info")
    public final VideoInfo videoInfo;

    /**
     * @deprecated use {@link MediaEntity#MediaEntity(String, String, String, int, int, long,
     * String, String, String, Sizes, long, String, String, VideoInfo)} instead
     */
    @Deprecated
    public MediaEntity(String url, String expandedUrl, String displayUrl, int start, int end,
                       long id, String idStr, String mediaUrl, String mediaUrlHttps, Sizes sizes,
                       long sourceStatusId, String sourceStatusIdStr, String type) {
        this(url, expandedUrl, displayUrl, start, end, id, idStr, mediaUrl, mediaUrlHttps, sizes,
                sourceStatusId, sourceStatusIdStr, type, null);
    }
  //  /*
    public MediaEntity() {
        super();
        this.id = -1;
        this.idStr = null;
        this.mediaUrl = null;
        this.mediaUrlHttps = null;
        this.sizes = null;
        this.sourceStatusId = -1;
        this.sourceStatusIdStr = null;
        this.type = null;
        this.videoInfo = null;
    }
  //  */

    public MediaEntity(String url, String expandedUrl, String displayUrl, int start, int end,
                       long id, String idStr, String mediaUrl, String mediaUrlHttps, Sizes sizes,
                       long sourceStatusId, String sourceStatusIdStr, String type, VideoInfo videoInfo) {
        super(url, expandedUrl, displayUrl, start, end);
        this.id = id;
        this.idStr = idStr;
        this.mediaUrl = mediaUrl;
        this.mediaUrlHttps = mediaUrlHttps;
        this.sizes = sizes;
        this.sourceStatusId = sourceStatusId;
        this.sourceStatusIdStr = sourceStatusIdStr;
        this.type = type;
        this.videoInfo = videoInfo;
    }


    public long getMediaId() {
        return id;
    }

    public String getIdStr() {
        return idStr;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    public Sizes getSizes() {
        return sizes;
    }

    public long getSourceStatusId() {
        return sourceStatusId;
    }

    public String getSourceStatusIdStr() {
        return sourceStatusIdStr;
    }

    public String getType() {
        return type;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public static class Sizes implements Serializable {
        /**
         * Information for a medium-sized version of the media.
         */
        @SerializedName("medium")
        public final Size medium;

        /**
         * Information for a thumbnail-sized version of the media.
         */
        @SerializedName("thumb")
        public final Size thumb;

        /**
         * Information for a small-sized version of the media.
         */
        @SerializedName("small")
        public final Size small;

        /**
         * Information for a large-sized version of the media.
         */
        @SerializedName("large")
        public final Size large;

        public Sizes(Size thumb, Size small, Size medium, Size large) {
            this.thumb = thumb;
            this.small = small;
            this.medium = medium;
            this.large = large;
        }
    }

    public static class Size implements Serializable {
        /**
         * Width in pixels of this size.
         */
        @SerializedName("w")
        public final int w;

        /**
         * Height in pixels of this size.
         */
        @SerializedName("h")
        public final int h;

        /**
         * Resizing method used to obtain this size. A value of fit means that the media was resized
         * to fit one dimension, keeping its native aspect ratio. A value of crop means that the
         * media was cropped in order to fit a specific resolution.
         */
        @SerializedName("resize")
        public final String resize;

        public Size(int w, int h, String resize) {
            this.w = w;
            this.h = h;
            this.resize = resize;
        }
    }


    @Override
    public String toString() {
        return "MediaEntity{" +
                "id=" + id +
                ", idStr='" + idStr + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", mediaUrlHttps='" + mediaUrlHttps + '\'' +
                ", sizes=" + sizes +
                ", sourceStatusId=" + sourceStatusId +
                ", sourceStatusIdStr='" + sourceStatusIdStr + '\'' +
                ", type='" + type + '\'' +
                ", videoInfo=" + videoInfo +
                '}';
    }
}
