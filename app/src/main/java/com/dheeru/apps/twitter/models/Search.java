package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dkthaku on 6/8/16.
 */
public class Search{
    @SerializedName("statuses")
    public final List<Tweet> tweets;

    @SerializedName("search_metadata")
    public final SearchMetadata searchMetadata;

    public Search(List<Tweet> tweets, SearchMetadata searchMetadata) {
        this.tweets = tweets;
        this.searchMetadata = searchMetadata;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public SearchMetadata getSearchMetadata() {
        return searchMetadata;
    }
}
