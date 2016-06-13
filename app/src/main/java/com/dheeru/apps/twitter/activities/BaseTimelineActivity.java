package com.dheeru.apps.twitter.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dheeru.apps.twitter.adapters.SmartFragmentStatePagerAdapter;
import com.dheeru.apps.twitter.fragments.ComposeFragment;
import com.dheeru.apps.twitter.fragments.TimelineFrag;
import com.dheeru.apps.twitter.models.Tweet;
import com.dheeru.apps.twitter.utility.TwitterRestClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/9/16.
 */
public class BaseTimelineActivity extends AppCompatActivity implements ComposeFragment.OnFragmentInteractionListener, TimelineFrag.OnTimelineFragmentInteractionListener {

    public TwitterRestClient mTwitterClient;
    public SmartFragmentStatePagerAdapter mSmartFragmentStatePagerAdapter;


    public void renderComposeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        composeFragment.show(fm, "compose");
    }

    public void renderReplyFragment(Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(tweet);
        composeFragment.show(fm, "reply");
    }

    @Override
    public void postMessage(long id, String message) {
        final String name;
        if (id == -1) {
            name = "Tweet";
        } else {
            name = "Reply";
        }
        mTwitterClient.postMessage(this, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Toast.makeText(BaseTimelineActivity.this, name + " sent Successfully!", Toast.LENGTH_LONG).show();
                response = "[" + response + "]";
                TimelineFrag timelineFragment = (TimelineFrag) mSmartFragmentStatePagerAdapter.getRegisteredFragment(0);
                timelineFragment.loadTweets(response, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(BaseTimelineActivity.this, "Sorry, " + name + " wasn't sent. Please try again.", Toast.LENGTH_LONG).show();
            }
        }, id, message);
    }


}

