package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkthaku on 6/3/16.
 */

// @Parcel(value = Parcel.Serialization.BEAN, analyze = {Coordinates.class})
@Parcel
public class Coordinates  {

    public static final int INDEX_LONGITUDE = 0;
    public static final int INDEX_LATITUDE = 1;

    /**
     * The longitude and latitude of the Tweet's location, as an collection in the form of
     * [longitude, latitude].
     */
    @SerializedName("coordinates")
    public final List<Double> coordinates;

    /**
     * The type of data encoded in the coordinates property. This will be "Point" for Tweet
     * coordinates fields.
     */
    @SerializedName("type")
    public final String type;

    public Coordinates(Double longitude, Double latitude, String type) {
        final List<Double> coords = new ArrayList<>(2);
        coords.add(INDEX_LONGITUDE, longitude);
        coords.add(INDEX_LATITUDE, latitude);

        this.coordinates = coords;
        this.type = type;
    }

    public Double getLongitude() {
        return coordinates.get(INDEX_LONGITUDE);
    }

    public Double getLatitude() {
        return coordinates.get(INDEX_LATITUDE);
    }

    public Coordinates(List<Double> coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }

  //  /*
    public Coordinates() {
        this.coordinates = null;
        this.type = null;

    }
  //  */
}
