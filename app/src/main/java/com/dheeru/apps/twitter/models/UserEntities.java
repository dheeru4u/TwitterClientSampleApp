package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;

/**
 * Created by dkthaku on 6/3/16.
 */

//@Parcel(value = Parcel.Serialization.BEAN, analyze = {UserEntities.class})

@Parcel
public class UserEntities {

    @SerializedName("url")
    public final UrlEntities url;

    @SerializedName("description")
    public final UrlEntities description;

    public UserEntities(UrlEntities url, UrlEntities description) {
        this.url = url;
        this.description = description;
    }

    public UserEntities() {
        this.url = null;
        this.description = null;
    }

    @Parcel
    public static class UrlEntities {

        @SerializedName("urls")
        public final List<UrlEntity> urls;

        public UrlEntities(List<UrlEntity> urls) {
            this.urls = getSafeList(urls);
        }
//  /*
        public UrlEntities() {
            this.urls = null;
        }
//        */

        private <T> List<T> getSafeList(List<T> entities) {
            // Entities may be null if Gson does not find object to parse. When that happens, make
            // sure to return an empty list.
            if (entities == null) {
                return Collections.EMPTY_LIST;
            } else {
                return Collections.unmodifiableList(entities);
            }
        }
    }
}
