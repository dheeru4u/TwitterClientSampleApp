package com.dheeru.apps.twitter.activities;

import android.view.View;
import android.widget.Toast;

import com.dheeru.apps.twitter.models.Friend;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/9/16.
 */
public class FollowersActivity extends FriendsActivity {

    @Override
    public void loadFriends(Long userId, long nextCursorId) {
        pbLoading.setVisibility(View.VISIBLE);
        boolean requestSent = mTwitterClient.getFollowers(this, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(FollowersActivity.this, "Sorry, unable to get Followers Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Friend friend = gson.fromJson(responseString, Friend.class);
                mNextCursorId = friend.getNextCursor();
                mFriends.addAll(friend.getUsers());
                mFriendsAdapter.notifyDataSetChanged();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, userId, nextCursorId);

        if (!requestSent) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }

}

