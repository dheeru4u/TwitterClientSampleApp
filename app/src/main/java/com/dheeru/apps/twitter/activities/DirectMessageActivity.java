package com.dheeru.apps.twitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.adapters.MessagesAdapter;
import com.dheeru.apps.twitter.fragments.DmFragment;
import com.dheeru.apps.twitter.models.Message;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.dheeru.apps.twitter.utility.TwitterRestApplication;
import com.dheeru.apps.twitter.utility.TwitterRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/9/16.
 */
public class DirectMessageActivity extends AppCompatActivity implements DmFragment.OnDmFragmentInteractionListener {

    public static final String TAG= DirectMessageActivity.class.getSimpleName();
    @Bind(R.id.lvMessages)
    ListView lvMessages;

    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;

    ArrayList<Message> mMessages;

    MessagesAdapter mMessageAdapter;

    TwitterRestClient mTwitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_message_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_bird);

        mMessages = new ArrayList<>();
        mMessageAdapter = new MessagesAdapter(this, mMessages);
        lvMessages.setAdapter(mMessageAdapter);

        mTwitterClient = TwitterRestApplication.getRestClient();

        getNewerMessages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_direct_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_message:
                renderDmFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getNewerMessages(){
        pbLoading.setVisibility(View.VISIBLE);
        long twitterSinceId = 1;

        if (mMessages.size() > 0) {
            twitterSinceId = mMessages.get(0).getId();
        }

        boolean requestSent = mTwitterClient.getNewerMessages(this, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), responseString);
                Log.d(TAG, "onFailure: "+headers);
                Log.d(TAG, "onFailure: statusCode = "+statusCode);
                Toast.makeText(DirectMessageActivity.this, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "onSuccess: "+responseString);
                loadMessages(responseString, 0);
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, twitterSinceId);

        if (!requestSent) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }

    public void loadMessages(String response, int positionStart) {
        Type collectionType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(CommonUtils.DATE_FORMAT);
        Gson gson = gsonBuilder.create();
        ArrayList<Message> messages = gson.fromJson(response, collectionType);
        mMessages.addAll(positionStart, messages);
        mMessageAdapter.notifyDataSetChanged();
    }

    public void renderDmFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DmFragment dmFragment = DmFragment.newInstance();
        dmFragment.show(fm, "dm");
    }

    @Override
    public void postMessage(String to, String message) {
        mTwitterClient.sendDirectMessage(this, to, message, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), responseString);
                Toast.makeText(DirectMessageActivity.this, "Sorry, unable to send DM. Please try again later.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                responseString = "[" + responseString + "]";
                loadMessages(responseString, 0);
            }
        });
    }
}
