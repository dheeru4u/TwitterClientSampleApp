package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkthaku on 6/9/16.
 */
public class Friend {

    @SerializedName("users")
    @Expose
    private List<User> users = new ArrayList<User>();
    @SerializedName("next_cursor")
    @Expose
    private Long nextCursor;
    @SerializedName("next_cursor_str")
    @Expose
    private String nextCursorStr;
    @SerializedName("previous_cursor")
    @Expose
    private Long previousCursor;
    @SerializedName("previous_cursor_str")
    @Expose
    private String previousCursorStr;

    /**
     *
     * @return
     *     The users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     *
     * @param users
     *     The users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     *
     * @return
     *     The nextCursor
     */
    public Long getNextCursor() {
        return nextCursor;
    }

    /**
     *
     * @param nextCursor
     *     The next_cursor
     */
    public void setNextCursor(Long nextCursor) {
        this.nextCursor = nextCursor;
    }

    /**
     *
     * @return
     *     The nextCursorStr
     */
    public String getNextCursorStr() {
        return nextCursorStr;
    }

    /**
     *
     * @param nextCursorStr
     *     The next_cursor_str
     */
    public void setNextCursorStr(String nextCursorStr) {
        this.nextCursorStr = nextCursorStr;
    }

    /**
     *
     * @return
     *     The previousCursor
     */
    public Long getPreviousCursor() {
        return previousCursor;
    }

    /**
     *
     * @param previousCursor
     *     The previous_cursor
     */
    public void setPreviousCursor(Long previousCursor) {
        this.previousCursor = previousCursor;
    }

    /**
     *
     * @return
     *     The previousCursorStr
     */
    public String getPreviousCursorStr() {
        return previousCursorStr;
    }

    /**
     *
     * @param previousCursorStr
     *     The previous_cursor_str
     */
    public void setPreviousCursorStr(String previousCursorStr) {
        this.previousCursorStr = previousCursorStr;
    }

}
