package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by dkthaku on 6/3/16.
 */

@Parcel //(value = Parcel.Serialization.BEAN, analyze = {HashtagEntity.class})
public class HashtagEntity extends Entity  {

    /**
     * Name of the hashtag, minus the leading '#' character.
     */
    @SerializedName("text")
    public final String text;

    public HashtagEntity(String text, int start, int end) {
        super(start, end);
        this.text = text;
    }
//  /*
    public HashtagEntity() {

        this.text = null;
    }
 //   */
}
