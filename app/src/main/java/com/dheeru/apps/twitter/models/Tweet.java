package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by dkthaku on 6/3/16.
 */

// @Parcel(value = Parcel.Serialization.BEAN, analyze = {Tweet.class})

@Parcel
public class Tweet  {
    public static final long INVALID_ID = -1L;
    private static final long serialVersionUID = 4663450673958L;


    /**
     * Nullable. Represents the geographic location of this Tweet as reported by the user or client
     * application. The inner coordinates array is formatted as geoJSON (longitude first,
     * then latitude).
     */
    @SerializedName("coordinates")
    public final Coordinates coordinates;

    /**
     * UTC time when this Tweet was created.
     */
    @SerializedName("created_at")
    public final String createdAt;

    /**
     * Perspectival. Only surfaces on methods supporting the include_my_retweet parameter, when set
     * to true. Details the Tweet ID of the user's own retweet (if existent) of this Tweet.
     */
    //@SerializedName("current_user_retweet")
    //  public final Object currentUserRetweet;

    /**
     * Entities which have been parsed out of the text of the Tweet.
     */
    @SerializedName("entities")
    public final TweetEntities entities;

    /**
     * Additional entities such as multi photos, animated gifs and video.
     */
    @SerializedName("extended_entities")
    public final TweetEntities extendedEtities;

    /**
     * Nullable. Indicates approximately how many times this Tweet has been "favorited" by Twitter
     * users.
     */
    @SerializedName("favorite_count")
    public final Integer favoriteCount;

    /**
     * Nullable. Perspectival. Indicates whether this Tweet has been favorited by the authenticating
     * user.
     */
    @SerializedName("favorited")
    public final boolean favorited;

    /**
     * Indicates the maximum value of the filter_level parameter which may be used and still stream
     * this Tweet. So a value of medium will be streamed on none, low, and medium streams.
     */
    @SerializedName("filter_level")
    public final String filterLevel;

    /**
     * The integer representation of the unique identifier for this Tweet. This number is greater
     * than 53 bits and some programming languages may have difficulty/silent defects in
     * interpreting it. Using a signed 64 bit integer for storing this identifier is safe. Use
     * id_str for fetching the identifier to stay on the safe side. See Twitter IDs, JSON and
     * Snowflake.
     */
    @SerializedName("id")
    public final long id;

    /**
     * The string representation of the unique identifier for this Tweet. Implementations should use
     * this rather than the large integer in id
     */
    @SerializedName("id_str")
    public final String idStr;

    /**
     * Nullable. If the represented Tweet is a reply, this field will contain the screen name of
     * the original Tweet's author.
     */
    @SerializedName("in_reply_to_screen_name")
    public final String inReplyToScreenName;

    /**
     * Nullable. If the represented Tweet is a reply, this field will contain the integer
     * representation of the original Tweet's ID.
     */
    @SerializedName("in_reply_to_status_id")
    public final long inReplyToStatusId;

    /**
     * Nullable. If the represented Tweet is a reply, this field will contain the string
     * representation of the original Tweet's ID.
     */
    @SerializedName("in_reply_to_status_id_str")
    public final String inReplyToStatusIdStr;

    /**
     * Nullable. If the represented Tweet is a reply, this field will contain the integer
     * representation of the original Tweet's author ID. This will not necessarily always be the
     * user directly mentioned in the Tweet.
     */
    @SerializedName("in_reply_to_user_id")
    public final long inReplyToUserId;

    /**
     * Nullable. If the represented Tweet is a reply, this field will contain the string
     * representation of the original Tweet's author ID. This will not necessarily always be the
     * user directly mentioned in the Tweet.
     */
    @SerializedName("in_reply_to_user_id_str")
    public final String inReplyToUserIdStr;

    /**
     * Nullable. When present, indicates a BCP 47 language identifier corresponding to the
     * machine-detected language of the Tweet text, or "und" if no language could be detected.
     */
    @SerializedName("lang")
    public final String lang;

    /**
     * Nullable. When present, indicates that the tweet is associated (but not necessarily
     * originating from) a Place.
     */
    @SerializedName("place")
    public final Place place;

    /**
     * Nullable. This field only surfaces when a tweet contains a link. The meaning of the field
     * doesn't pertain to the tweet content itself, but instead it is an indicator that the URL
     * contained in the tweet may contain content or media identified as sensitive content.
     */
    @SerializedName("possibly_sensitive")
    public final boolean possiblySensitive;

    /**
     * A set of key-value pairs indicating the intended contextual delivery of the containing Tweet.
     * Currently used by Twitter's Promoted Products.
     */
   // @SerializedName("scopes")
    //public final Object scopes;

    /**
     * Number of times this Tweet has been retweeted. This field is no longer capped at 99 and will
     * not turn into a String for "100+"
     */
    @SerializedName("retweet_count")
    public final int retweetCount;

    /**
     * Perspectival. Indicates whether this Tweet has been retweeted by the authenticating user.
     */
    @SerializedName("retweeted")
    public final boolean retweeted;

    /**
     * Users can amplify the broadcast of tweets authored by other users by retweeting. Retweets can
     * be distinguished from typical Tweets by the existence of a retweeted_status attribute. This
     * attribute contains a representation of the original Tweet that was retweeted. Note that
     * retweets of retweets do not show representations of the intermediary retweet, but only the
     * original tweet. (Users can also unretweet a retweet they created by deleting their retweet.)
     */
    @SerializedName("retweeted_status")

    public final Tweet retweetedStatus;

    /**
     * Utility used to post the Tweet, as an HTML-formatted string. Tweets from the Twitter website
     * have a source value of web.
     */
    @SerializedName("source")
    public final String source;

    /**
     * The actual UTF-8 text of the status update. See twitter-text for details on what is currently
     * considered valid characters.
     */
    @SerializedName("text")
    public final String text;

    /**
     * Indicates whether the value of the text parameter was truncated, for example, as a result of
     * a retweet exceeding the 140 character Tweet length. Truncated text will end in ellipsis, like
     * this ... Since Twitter now rejects long Tweets vs truncating them, the large majority of
     * Tweets will have this set to false.
     * Note that while native retweets may have their toplevel text property shortened, the original
     * text will be available under the retweeted_status object and the truncated parameter will be
     * set to the value of the original status (in most cases, false).
     */
    @SerializedName("truncated")
    public final boolean truncated;

    /**
     * The user who posted this Tweet. Perspectival attributes embedded within this object are
     * unreliable. See Why are embedded objects stale or inaccurate?.
     */
    @SerializedName("user")
    public final User user;

    /**
     * When present and set to "true", it indicates that this piece of content has been withheld due
     * to a DMCA complaint.
     */
    @SerializedName("withheld_copyright")
    public final boolean withheldCopyright;

    /**
     * When present, indicates a list of uppercase two-letter country codes this content is withheld
     * from. Twitter supports the following non-country values for this field:
     * "XX" - Content is withheld in all countries
     * "XY" - Content is withheld due to a DMCA request.
     */
    @SerializedName("withheld_in_countries")
    public final List<String> withheldInCountries;

    /**
     * When present, indicates whether the content being withheld is the "status" or a "user."
     */
    @SerializedName("withheld_scope")
    public final String withheldScope;



    public Tweet(Coordinates coordinates, String createdAt, Object currentUserRetweet,
                 TweetEntities entities, TweetEntities extendedEtities, Integer favoriteCount,
                 boolean favorited, String filterLevel, long id, String idStr,
                 String inReplyToScreenName, long inReplyToStatusId, String inReplyToStatusIdStr,
                 long inReplyToUserId, String inReplyToUserIdStr, String lang, Place place,
                 boolean possiblySensitive, Object scopes, int retweetCount, boolean retweeted,
                 Tweet retweetedStatus, String source, String text, boolean truncated, User user,
                 boolean withheldCopyright, List<String> withheldInCountries, String withheldScope) {
        this.coordinates = coordinates;
        this.createdAt = createdAt;
       // this.currentUserRetweet = currentUserRetweet;
        this.entities = entities;
        this.extendedEtities = extendedEtities;
        this.favoriteCount = favoriteCount;
        this.favorited = favorited;
        this.filterLevel = filterLevel;
        this.id = id;
        this.idStr = idStr;
        this.inReplyToScreenName = inReplyToScreenName;
        this.inReplyToStatusId = inReplyToStatusId;
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
        this.inReplyToUserId = inReplyToUserId;
        this.inReplyToUserIdStr = inReplyToUserIdStr;
        this.lang = lang;
        this.place = place;
        this.possiblySensitive = possiblySensitive;
       // this.scopes = scopes;
        this.retweetCount = retweetCount;
        this.retweeted = retweeted;
        this.retweetedStatus = retweetedStatus;
        this.source = source;
        this.text = text;
        this.truncated = truncated;
        this.user = user;
        this.withheldCopyright = withheldCopyright;
        this.withheldInCountries = withheldInCountries;
        this.withheldScope = withheldScope;
    }

// /*
    public Tweet(){
        this.coordinates = null;
        this.createdAt = null;
       // this.currentUserRetweet = null;
        this.entities = null;
        this.extendedEtities = null;
        this.favoriteCount = null;
        this.favorited =false;
        this.filterLevel = null;
        this.id = -1;
        this.idStr = null;
        this.inReplyToScreenName = null;
        this.inReplyToStatusId =-1;
        this.inReplyToStatusIdStr = null;
        this.inReplyToUserId = -1;
        this.inReplyToUserIdStr = null;
        this.lang = null;
        this.place = null;
        this.possiblySensitive = false;
     //   this.scopes = null;
        this.retweetCount = -1;
        this.retweeted = false;
        this.retweetedStatus = null;
        this.source = null;
        this.text = null;
        this.truncated = false;
        this.user = null;
        this.withheldCopyright =false;
        this.withheldInCountries = null;
        this.withheldScope = null;
    }

// */
    public long getTweetId() {
        return this.id;
    }

    public long getId() {
        return this.id;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Tweet)) return false;
        final Tweet other = (Tweet) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }


    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Object getCurrentUserRetweet() {
        return null;
    }

    public TweetEntities getEntities() {
        return entities;
    }

    public TweetEntities getExtendedEtities() {
        return extendedEtities;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public String getFilterLevel() {
        return filterLevel;
    }

    public String getIdStr() {
        return idStr;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public String getInReplyToStatusIdStr() {
        return inReplyToStatusIdStr;
    }

    public long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public String getInReplyToUserIdStr() {
        return inReplyToUserIdStr;
    }

    public String getLang() {
        return lang;
    }

    public Place getPlace() {
        return place;
    }

    public boolean isPossiblySensitive() {
        return possiblySensitive;
    }

    public Object getScopes() {
        return null; // scopes;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public Tweet getRetweetedStatus() {
        return retweetedStatus;
    }

    public String getSource() {
        return source;
    }

    public String getText() {
        return text;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "coordinates=" + coordinates +
                ", createdAt='" + createdAt + '\'' +
                ", currentUserRetweet="  +
                ", entities=" + entities +
                ", extendedEtities=" + extendedEtities +
                ", favoriteCount=" + favoriteCount +
                ", favorited=" + favorited +
                ", filterLevel='" + filterLevel + '\'' +
                ", id=" + id +
                ", idStr='" + idStr + '\'' +
                ", inReplyToScreenName='" + inReplyToScreenName + '\'' +
                ", inReplyToStatusId=" + inReplyToStatusId +
                ", inReplyToStatusIdStr='" + inReplyToStatusIdStr + '\'' +
                ", inReplyToUserId=" + inReplyToUserId +
                ", inReplyToUserIdStr='" + inReplyToUserIdStr + '\'' +
                ", lang='" + lang + '\'' +
                ", place=" + place +
                ", possiblySensitive=" + possiblySensitive +
                ", scopes="  +
                ", retweetCount=" + retweetCount +
                ", retweeted=" + retweeted +
                ", retweetedStatus=" + retweetedStatus +
                ", source='" + source + '\'' +
                ", text='" + text + '\'' +
                ", truncated=" + truncated +
                ", user=" + user +
                ", withheldCopyright=" + withheldCopyright +
                ", withheldInCountries=" + withheldInCountries +
                ", withheldScope='" + withheldScope + '\'' +
                '}';
    }

}