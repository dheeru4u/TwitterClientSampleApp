package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dkthaku on 6/8/16.
 */
public class Configuration {
    /**
     * Maximum number of characters per direct message
     */
    @SerializedName("dm_text_character_limit")
    public final int dmTextCharacterLimit;

    /**
     * Slugs which are not user names
     */
    @SerializedName("non_username_paths")
    public final List<String> nonUsernamePaths;

    /**
     * Maximum size in bytes for the media file.
     */
    @SerializedName("photo_size_limit")
    public final long photoSizeLimit;

    /**
     * Maximum resolution for the media files.
     */
    @SerializedName("photo_sizes")
    public final MediaEntity.Sizes photoSizes;

    /**
     * Current t.co URL length
     */
    @SerializedName("short_url_length_https")
    public final int shortUrlLengthHttps;

    public Configuration(int dmTextCharacterLimit, List<String> nonUsernamePaths,
                         long photoSizeLimit, MediaEntity.Sizes photoSizes, int shortUrlLengthHttps) {
        this.dmTextCharacterLimit = dmTextCharacterLimit;
        this.nonUsernamePaths = nonUsernamePaths;
        this.photoSizeLimit = photoSizeLimit;
        this.photoSizes = photoSizes;
        this.shortUrlLengthHttps = shortUrlLengthHttps;
    }
}
