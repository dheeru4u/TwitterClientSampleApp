package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;

/**
 * Created by dkthaku on 6/3/16.
 */

/**
 * Provides metadata and additional contextual information about content posted in a tweet.
 */

// @Parcel(value = Parcel.Serialization.BEAN, analyze = {TweetEntities.class})
@Parcel
public class TweetEntities  {

    /**
     * Represents URLs included in the text of a Tweet or within textual fields of a user object.
     */
    @SerializedName("urls")
    public final List<UrlEntity> urls;

    /**
     * Represents other Twitter users mentioned in the text of the Tweet.
     */
    @SerializedName("user_mentions")
    public final List<MentionEntity> userMentions;

    /**
     * Represents media elements uploaded with the Tweet.
     */
    @SerializedName("media")
    public final List<MediaEntity> media;

    /**
     * Represents hashtags which have been parsed out of the Tweet text.
     */
    @SerializedName("hashtags")
    public final List<HashtagEntity> hashtags;

    public TweetEntities(List<UrlEntity> urls, List<MentionEntity> userMentions,
                         List<MediaEntity> media, List<HashtagEntity> hashtags) {
        this.urls = getSafeList(urls);
        this.userMentions = getSafeList(userMentions);
        this.media = getSafeList(media);
        this.hashtags = getSafeList(hashtags);
    }
// /*
    public TweetEntities() {
        this.urls = null;
        this.userMentions = null;
        this.media = null;
        this.hashtags = null;
    }
 //   */
    private <T> List<T> getSafeList(List<T> entities) {
        // Entities may be null if Gson does not find object to parse. When that happens, make sure
        // to return an empty list.
        if (entities == null) {
            return Collections.EMPTY_LIST;
        } else {
            return Collections.unmodifiableList(entities);
        }
    }

    public List<UrlEntity> getUrls() {
        return urls;
    }

    public List<MentionEntity> getUserMentions() {
        return userMentions;
    }

    public List<MediaEntity> getMedia() {
        return media;
    }

    public List<HashtagEntity> getHashtags() {
        return hashtags;
    }

    @Override
    public String toString() {
        return "TweetEntities{" +
                "urls=" + urls +
                ", userMentions=" + userMentions +
                ", media=" + media +
                ", hashtags=" + hashtags +
                '}';
    }
}
