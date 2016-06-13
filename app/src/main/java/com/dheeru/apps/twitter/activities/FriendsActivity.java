package com.dheeru.apps.twitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.adapters.FriendsAdapter;
import com.dheeru.apps.twitter.listeners.FriendListViewEndlessScrollListener;
import com.dheeru.apps.twitter.models.Friend;
import com.dheeru.apps.twitter.models.User;
import com.dheeru.apps.twitter.utility.TwitterRestApplication;
import com.dheeru.apps.twitter.utility.TwitterRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/9/16.
 */
public class FriendsActivity extends AppCompatActivity {

    @Bind(R.id.lvFriends)
    ListView lvFriends;

    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;

    ArrayList<User> mFriends;

    FriendsAdapter mFriendsAdapter;

    TwitterRestClient mTwitterClient;

    long mNextCursorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_bird);

        mFriends = new ArrayList<>();
        mFriendsAdapter = new FriendsAdapter(this, mFriends);
        lvFriends.setAdapter(mFriendsAdapter);

        final Long userId = getIntent().getLongExtra("userId", -1);

        mTwitterClient = TwitterRestApplication.getRestClient();
        mNextCursorId = -1;

        lvFriends.setOnScrollListener(new FriendListViewEndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadFriends(userId, mNextCursorId);
                return true;
            }
        });

        loadFriends(userId, mNextCursorId);
    }

    public void loadFriends(Long userId, long nextCursorId) {
        pbLoading.setVisibility(View.VISIBLE);
        boolean requestSent = mTwitterClient.getFriends(this, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(FriendsActivity.this, "Sorry, unable to get Friends Please try again later.", Toast.LENGTH_LONG).show();
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

