package com.dheeru.apps.twitter.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.adapters.TweetsTimelineRecylVwAdapter;
import com.dheeru.apps.twitter.listeners.TweetsEndlessRecyScrollListener;
import com.dheeru.apps.twitter.models.Search;
import com.dheeru.apps.twitter.models.Tweet;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.dheeru.apps.twitter.utility.TwitterRestApplication;
import com.dheeru.apps.twitter.utility.TwitterRestClient;
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
 * Created by dkthaku on 6/8/16.
 */
public class TimelineFrag extends Fragment {

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;

    private String mTimeline;
    private OnTimelineFragmentInteractionListener mListener;
    private Activity mActivity;
    private TwitterRestClient mTwitterClient;
    private TweetsTimelineRecylVwAdapter mTweetsRecyclerViewAdapter;
    private ArrayList<Tweet> mTweets;
    private HashSet<Long> mTweetIdsHashSet;
    private long mUserId = -1;
    private String mSearchText;
    private long mSearchSinceId;
    private long mSearchMaxId;


    private static final String TIMELINE_ARG = "timeline";
    private static final String USER_ID_ARG = "userId";

    public static TimelineFrag newInstance(String timeline, long userId) {
        Bundle args = new Bundle();
        args.putString(TIMELINE_ARG, timeline);
        args.putLong(USER_ID_ARG, userId);
        TimelineFrag fragment = new TimelineFrag();
        fragment.setArguments(args);
        return fragment;
    }

    public static TimelineFrag newInstance() {
        TimelineFrag fragment = new TimelineFrag();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        if (context instanceof OnTimelineFragmentInteractionListener) {
            mListener = (OnTimelineFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTimelineFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            if (getArguments().containsKey(TIMELINE_ARG)) {
                mTimeline = getArguments().getString(TIMELINE_ARG).toLowerCase();
            }
            if (getArguments().containsKey(USER_ID_ARG)) {
                mUserId = getArguments().getLong(USER_ID_ARG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_frag, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTwitterClient = TwitterRestApplication.getRestClient();
        mTweets = new ArrayList<>();
        mTweetIdsHashSet = new HashSet<>();

        mTweetsRecyclerViewAdapter = new TweetsTimelineRecylVwAdapter(mActivity, mTweets);
        rvTweets.setAdapter(mTweetsRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        rvTweets.setLayoutManager(linearLayoutManager);

        rvTweets.addItemDecoration(new DividerItemDecoration((Context) mListener, DividerItemDecoration.VERTICAL_LIST));

        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);


        if (mTimeline != null) {
            rvTweets.addOnScrollListener(new TweetsEndlessRecyScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore() {
                    getOldTweets();
                }
            });


            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getNewTweets();
                }
            });

            if (!CommonUtils.isNetworkAvailable(mActivity) || !CommonUtils.isOnline(mActivity)) {
                //loadTweetsFromDB();
            } else {
                getNewTweets();
            }
        } else {
            rvTweets.addOnScrollListener(new TweetsEndlessRecyScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore() {
                    searchForOlderTweets();
                }
            });


            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    searchForNewerTweets(mSearchText);
                }
            });
        }
    }

    public void clearTweets(){
        mTweetsRecyclerViewAdapter.notifyItemRangeRemoved(0, mTweets.size());
        mTweets.clear();
        mTweetIdsHashSet.clear();
    }

    public void searchForNewerTweets(String searchText) {
        mSearchText = searchText;
        pbLoading.setVisibility(View.VISIBLE);
        long twitterSinceId = 1;

        if (mTweets.size() > 0) {
            twitterSinceId = mTweets.get(0).getId();
        }

        boolean requestSent = mTwitterClient.searchForNewerTweet(mActivity, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadSearchTweets(response, 0);

                if (mTweets.size() > 0) {
                }
                pbLoading.setVisibility(View.INVISIBLE);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), "in for new");
                Log.e(this.toString(), String.valueOf(responseString));
                swipeContainer.setRefreshing(false);
                Toast.makeText(mActivity, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, mSearchText, twitterSinceId);

        if (!requestSent) {
            swipeContainer.setRefreshing(false);
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void searchForOlderTweets() {
        pbLoading.setVisibility(View.VISIBLE);
        long twitterMaxId = 0;
        if (mTweets.size() > 0) {
            twitterMaxId = mTweets.get(mTweets.size() - 1).getId() - 1;
        }

        boolean requestSent = mTwitterClient.searchForOlderTweet(mActivity, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadSearchTweets(response, mTweets.size());
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), "in for old");
                Log.e(this.toString(), String.valueOf(responseString));
                Toast.makeText(mActivity, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, mSearchText, twitterMaxId);

        if (!requestSent) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }


    private void getNewTweets() {
        pbLoading.setVisibility(View.VISIBLE);
        long twitterSinceId = 1;

        if (mTweets.size() > 0) {
            twitterSinceId = mTweets.get(0).getId();
        }

        boolean requestSent = mTwitterClient.getNewTweets(mActivity, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadTweets(response, 0);

                if (mTweets.size() > 0) {
                    //clearDB();
                    //saveTweetsToDB(mTweets);
                }
                pbLoading.setVisibility(View.INVISIBLE);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                swipeContainer.setRefreshing(false);
                Toast.makeText(mActivity, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, twitterSinceId, mTimeline, mUserId);

        if (!requestSent) {
            swipeContainer.setRefreshing(false);
            pbLoading.setVisibility(View.INVISIBLE);
        }

    }

    private void getOldTweets() {
        pbLoading.setVisibility(View.VISIBLE);
        long twitterMaxId = 0;
        if (mTweets.size() > 0) {
            twitterMaxId = mTweets.get(mTweets.size() - 1).getId() - 1;
        }

        boolean requestSent = mTwitterClient.getOldTweets(mActivity, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                loadTweets(response, mTweets.size());
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                Toast.makeText(mActivity, "Sorry, unable to get Tweets. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, twitterMaxId, mTimeline, mUserId);

        if (!requestSent) {
            pbLoading.setVisibility(View.INVISIBLE);
        }

    }

    public void loadTweets(String response, int positionStart) {
        Type collectionType = new TypeToken<ArrayList<Tweet>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(CommonUtils.DATE_FORMAT);
        Gson gson = gsonBuilder.create();
        ArrayList<Tweet> tweets = gson.fromJson(response, collectionType);
        Iterator<Tweet> iterator = tweets.iterator();
        while (iterator.hasNext()) {
            Tweet tweet = iterator.next();
            if (mTweetIdsHashSet.contains(tweet.getId())) {
                iterator.remove();
            } else {
                mTweetIdsHashSet.add(tweet.getId());
            }
        }

        mTweets.addAll(positionStart, tweets);
        mTweetsRecyclerViewAdapter.notifyItemRangeInserted(positionStart, tweets.size());
    }

    public void loadSearchTweets(String response, int positionStart) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(CommonUtils.DATE_FORMAT);
        Gson gson = gsonBuilder.create();
        Search search = gson.fromJson(response, Search.class);
        List<Tweet> tweets = search.getTweets();
        if(tweets!=null) {
            mSearchSinceId = tweets.get(0).getId();
            int lastTweet=tweets.size()-1;
            mSearchMaxId = tweets.get(lastTweet).getId() - 1;
        }
        Iterator<Tweet> iterator = tweets.iterator();
        while (iterator.hasNext()) {
            Tweet tweet = iterator.next();
            if (mTweetIdsHashSet.contains(tweet.getId())) {
                iterator.remove();
            } else {
                mTweetIdsHashSet.add(tweet.getId());
            }
        }

        mTweets.addAll(positionStart, tweets);
        mTweetsRecyclerViewAdapter.notifyItemRangeInserted(positionStart, tweets.size());
    }

    public void reload() {
        getNewTweets();
    }

    public interface OnTimelineFragmentInteractionListener {

    }


}
