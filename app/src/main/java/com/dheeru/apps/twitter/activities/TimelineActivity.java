package com.dheeru.apps.twitter.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.util.Log;
import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.utility.TwitterRestApplication;
import com.dheeru.apps.twitter.utility.TwitterRestClient;
import com.dheeru.apps.twitter.adapters.TweetsTimelineRecylVwAdapter;
import com.dheeru.apps.twitter.fragments.ComposeFragment;
import com.dheeru.apps.twitter.listeners.TweetsEndlessRecyScrollListener;
import com.dheeru.apps.twitter.models.Tweet;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.dheeru.apps.twitter.views.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/2/16.
 */
public class TimelineActivity extends AppCompatActivity implements ComposeFragment.OnFragmentInteractionListener {

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;
    static final String TAG = TimelineActivity.class.getSimpleName();
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Bind(R.id.tvOfflineMode)
    TextView tvOfflineMode;

    private static TwitterRestClient mTwitterClient;
    private static TweetsTimelineRecylVwAdapter mTweetsRecyclerViewAdapter;
    private static ArrayList<Tweet> mTweets;

    private static long mTwitterMaxId;
    private long mTwitterSinceId;

    private static HashSet<Long> mTweetIdsHashSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_twitter_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 renderComposeFragment();
            }
        });


        mTwitterClient = TwitterRestApplication.getRestClient();

        mTweets = new ArrayList<>();
        mTweetsRecyclerViewAdapter = new TweetsTimelineRecylVwAdapter(this, mTweets);
        rvTweets.setAdapter(mTweetsRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        rvTweets.addOnScrollListener(new TweetsEndlessRecyScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                getOldTweets();
            }
        });

        rvTweets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewTweets();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);


        mTwitterMaxId = 0;
        mTwitterSinceId = 1;

        mTweetIdsHashSet = new HashSet<>();

        if (!CommonUtils.isNetworkAvailable(this) || !CommonUtils.isOnline(this)) {
          //  loadTweetsFromDB();
            tvOfflineMode.setVisibility(View.VISIBLE);
        } else {
            tvOfflineMode.setVisibility(View.GONE);
            getNewTweets();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mTwitterClient.clearAccessToken();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_compose:
                renderComposeFragment();
                return true;
            case R.id.myAccount:
                getUserTimeline();
                return true;
            case R.id.menu_user_timeline:
                getUserTimeline();
                return true;
            case R.id.menu_home_timeline:
                getHomeTimeline();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNewTweets() {
        android.util.Log.d(TAG, "getNewTweets:  ");
        boolean requestSent = mTwitterClient.getNewTweets(this, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                android.util.Log.d(TAG, "getNewTweets() onSuccess: ");
               loadTweets(response, 0);
                if (mTweets.size() > 0) {
                    mTwitterSinceId = mTweets.get(0).getTweetId();
                   // clearDB();
                   // saveTweetsToDB(mTweets);
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                android.util.Log.d(TAG, "onFailure: statusCode $$$$$$$$$$$$$$$$$$$  "+String.valueOf(statusCode));
                swipeContainer.setRefreshing(false);
                Toast.makeText(TimelineActivity.this, "Sorry, unable to get Tweets. Please try again later. ", Toast.LENGTH_LONG).show();
            }
        }, mTwitterSinceId);

        if (!requestSent) {
            swipeContainer.setRefreshing(false);
        }

    }

    private void getOldTweets() {
        mTwitterClient.getOldTweets(this, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadTweets(response, mTweets.size());
                if (mTweets.size() > 0) {
                    mTwitterMaxId = mTweets.get(mTweets.size() - 1).getTweetId();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                Toast.makeText(TimelineActivity.this, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }, mTwitterMaxId);

    }

    private void getUserTimeline(){
        mTwitterClient.getUserTimeline(this, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mTweetsRecyclerViewAdapter.notifyItemRangeRemoved(0, mTweets.size());
                mTweets.clear();
                mTweetIdsHashSet.clear();
                loadTweets(responseString, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(TimelineActivity.this, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
            }


        });
    }

    private void getHomeTimeline(){
        mTweetsRecyclerViewAdapter.notifyItemRangeRemoved(0, mTweets.size());
        mTweets.clear();
        mTweetIdsHashSet.clear();
        getNewTweets();
    }

    public static void loadTweets(String response, int positionStart) {

        try {
            android.util.Log.d(TAG, "loadTweets: response "+response);
            Type collectionType = new TypeToken<ArrayList<Tweet>>() {
            }.getType();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(CommonUtils.DATE_FORMAT);
            Gson gson = gsonBuilder.create();
            ArrayList<Tweet> tweets = gson.fromJson(response, collectionType);
            android.util.Log.d(TAG, "loadTweets: tweets " + tweets);
            Iterator<Tweet> iterator = tweets.iterator();
            while (iterator.hasNext()) {
                Tweet tweet = iterator.next();
                if (mTweetIdsHashSet.contains(tweet.getTweetId())) {
                    iterator.remove();
                } else {
                    mTweetIdsHashSet.add(tweet.getTweetId());
                }
            }
            if (mTweets.size() == 0 & tweets.size() > 0) {
                mTwitterMaxId = tweets.get(tweets.size() - 1).getTweetId();
            }
            mTweets.addAll(positionStart, tweets);
            mTweetsRecyclerViewAdapter.notifyItemRangeInserted(positionStart, tweets.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearDB(){
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String dbName = bundle.getString("AA_DB_NAME");
            ActiveAndroid.dispose();
            this.deleteDatabase(dbName);
            Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName(dbName).create();
            ActiveAndroid.initialize(dbConfiguration);
            ActiveAndroid.setLoggingEnabled(true);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void saveTweetsToDB(List<Tweet> tweets) {

    }

    private void saveToDBHelper(Tweet tweet) {

    }

    public void loadTweetsFromDB() {


    }


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
        if(id == -1){
            name = "Tweet";
        } else {
            name = "Reply";
        }
        mTwitterClient.postMessage(this, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Toast.makeText(TimelineActivity.this, name + " sent Successfully!", Toast.LENGTH_LONG).show();
                response = "[" + response + "]";
                loadTweets(response, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(TimelineActivity.this, "Sorry, " + name + " wasn't sent. Please try again.", Toast.LENGTH_LONG).show();
            }
        }, id, message);


    }


}
