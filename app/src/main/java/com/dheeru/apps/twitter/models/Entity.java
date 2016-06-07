package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dkthaku on 6/3/16.
 */

//@Parcel(value = Parcel.Serialization.BEAN, analyze = {Entity.class})
@Parcel
public class Entity   {
    private static final int START_INDEX = 0;
    private static final int END_INDEX = 1;

    /**
     * An array of integers indicating the offsets.
     */
    @SerializedName("indices")
    public final List<Integer> indices;

    public Entity(int start, int end) {
        final List<Integer> temp = new ArrayList<>(2);
        temp.add(START_INDEX, start);
        temp.add(END_INDEX, end);

        indices = Collections.unmodifiableList(temp);
    }

    public int getStart() {
        return indices.get(START_INDEX);
    }

    public int getEnd() {
        return indices.get(END_INDEX);
    }

    public Entity(List<Integer> indices) {
        this.indices = indices;
    }
// /*
    public Entity() {
        this.indices = null;
    }
   // */
}
