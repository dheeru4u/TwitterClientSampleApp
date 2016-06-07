package com.dheeru.apps.twitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.TwitterRestApplication;
import com.dheeru.apps.twitter.TwitterRestClient;
import com.dheeru.apps.twitter.fragments.ComposeFragment;
import com.dheeru.apps.twitter.models.Tweet;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.malmstein.fenster.controller.SimpleMediaFensterPlayerController;
import com.malmstein.fenster.view.FensterVideoView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/3/16.
 */
public class TweetDetailActivity extends AppCompatActivity implements ComposeFragment.OnFragmentInteractionListener {
    TwitterRestClient mTwitterClient;
    static final String TAG=TweetDetailActivity.class.getSimpleName();
    @Bind(R.id.ivUserProfileImage)
    RoundedImageView ivUserProfileImage;

    @Bind(R.id.tvUserName)
    TextView tvUserName;

    @Bind(R.id.tvScreenName)
    TextView tvScreenName;

    @Bind(R.id.tvText)
    TextView tvText;

    @Bind(R.id.ivMedia)
    ImageView ivMedia;

    @Bind(R.id.ivIconVideo)
    ImageView ivIconVideo;

    @Bind(R.id.tvCreationTimestamp)
    TextView tvCreationTimestamp;

    @Bind(R.id.tvRetweetNumber)
    TextView tvRetweetNumber;

    @Bind(R.id.tvRetweetLabel)
    TextView tvRetweetLabel;

    @Bind(R.id.tvLikeNumber)
    TextView tvLikeNumber;

    @Bind(R.id.tvLikeLabel)
    TextView tvLikeLabel;

    @Bind(R.id.tvReplyPlaceholder)
    TextView tvReplyPlaceholder;

    @Bind(R.id.rlReplyPlaceholder)
    RelativeLayout rlReplyPlaceholder;

    @Bind(R.id.rlReplyBox)
    RelativeLayout rlReplyBox;

    @Bind(R.id.etReply)
    EditText etReply;

    @Bind(R.id.tvTweet)
    TextView tvTweet;

    @Bind(R.id.tvCharacterCount)
    TextView tvCharacterCount;

    @Bind(R.id.ivTopIconRetweet)
    ImageView ivTopIconRetweet;

    @Bind(R.id.tvRetweetUser)
    TextView tvRetweetUser;

    @Bind(R.id.fvvVideo)
    FensterVideoView fvvVideo;

    @Bind(R.id.mfpcVideo)
    SimpleMediaFensterPlayerController mfpcVideo;

    @Bind(R.id.ivIconReply)
    ImageView ivIconReply;

    @Bind(R.id.ivIconRetweet)
    ImageView ivIconRetweet;

    @Bind(R.id.ivIconLike)
    ImageView ivIconLike;

    @Bind(R.id.ivIconShare)
    ImageView ivIconShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.tweet_detail_activity);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ButterKnife.bind(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_twitter_bird);

            Tweet tweet =(Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
            Log.d(TAG, "onCreate: tweet : "+tweet);
           int position = getIntent().getIntExtra("position", 1);
            Log.d(TAG, "onCreate: position "+position);
            if (tweet.getRetweetedStatus() != null) {
                renderRetweet(tweet);
            } else {
                render(tweet);
            }

            mTwitterClient = TwitterRestApplication.getRestClient();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void renderRetweet(Tweet tweet) {
        ivTopIconRetweet.setVisibility(View.VISIBLE);
        tvRetweetUser.setVisibility(View.VISIBLE);
        tvRetweetUser.setText(tweet.getUser().getName() + " Retweeted");
        render(tweet.getRetweetedStatus());
    }

    private void render(final Tweet tweet) {
        Log.d(TAG, "render: tweet "+tweet);
        if (tweet.getUser() != null) {
            ivUserProfileImage.setImageResource(0);
            Glide.with(this).load(tweet.getUser().getProfileImageUrl()).error(R.drawable.photo_placeholder).placeholder(R.drawable.photo_placeholder).dontAnimate().into(ivUserProfileImage);
            tvUserName.setText(tweet.getUser().getName());
            tvScreenName.setText("@" + tweet.getUser().getScreenName());
            tvReplyPlaceholder.setText("Reply to " + tweet.getUser().getName());
        }
        CommonUtils.unwrapAndRenderTweetTextLinks(this, tweet, ivMedia, fvvVideo, mfpcVideo, ivIconVideo, tvText);
        tvCreationTimestamp.setText(CommonUtils.getFormattedTimestamp(tweet.getCreatedAt()));

        if (tweet.getRetweetCount() > 0) {
            if (tweet.getRetweetCount() > 1) {
                tvRetweetLabel.setText(getString(R.string.retweet) + "s");
            }
            tvRetweetNumber.setText(String.valueOf(tweet.getRetweetCount()));
        } else {
            tvRetweetLabel.setVisibility(View.INVISIBLE);
        }

        if (tweet.getFavoriteCount() > 0) {
            if (tweet.getFavoriteCount() > 1) {
                tvLikeLabel.setText(getString(R.string.like) + "s");
            }
            tvLikeNumber.setText(String.valueOf(tweet.getFavoriteCount()));
        } else {
            tvLikeLabel.setVisibility(View.INVISIBLE);
        }

        if (tweet.getFavoriteCount() == 0 && tweet.getRetweetCount() == 0) {
            tvRetweetLabel.setVisibility(View.GONE);
            tvLikeLabel.setVisibility(View.GONE);
        }


        rlReplyPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlReplyBox.setVisibility(View.VISIBLE);
                etReply.setText("@" + tweet.getUser().getScreenName() + " ");
                etReply.requestFocus();
            }
        });

        etReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int MAX_CHAR_COUNT = 140;
                String text = s.toString();
                int textLength = text.length();
                int diff = MAX_CHAR_COUNT - textLength;
                tvCharacterCount.setText(String.valueOf(diff));

                if (diff < 0) {
                    tvCharacterCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    tvTweet.setTextColor(getResources().getColor(R.color.colorGrey));
                    tvTweet.setOnClickListener(null);
                } else {
                    tvCharacterCount.setTextColor(getResources().getColor(R.color.colorGrey));
                    tvTweet.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvTweet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rlReplyBox.setVisibility(View.GONE);
                            postMessage(tweet.getTweetId(), etReply.getText().toString());
                        }
                    });
                }
            }
        });

        ivIconReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderReplyFragment(tweet);
            }
        });
    }

    public void renderReplyFragment(Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(tweet);
        composeFragment.show(fm, "reply");
    }

    @Override
    public void postMessage(long id, String message) {
        mTwitterClient.postMessage(TweetDetailActivity.this, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                response = "[" + response + "]";
                TimelineActivity.loadTweets(response, 0);
                Toast.makeText(TweetDetailActivity.this, "Reply sent Successfully!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Toast.makeText(TweetDetailActivity.this, "Sorry, Reply wasn't sent. Please try again.", Toast.LENGTH_LONG).show();
            }
        }, id, message);
    }

}
