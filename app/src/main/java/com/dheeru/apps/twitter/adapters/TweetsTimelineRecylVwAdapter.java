package com.dheeru.apps.twitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.activities.TimelineActivity;
import com.dheeru.apps.twitter.activities.TweetDetailActivity;
import com.dheeru.apps.twitter.models.Tweet;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.malmstein.fenster.controller.SimpleMediaFensterPlayerController;
import com.malmstein.fenster.view.FensterVideoView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 6/3/16.
 */
public class TweetsTimelineRecylVwAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> mTweets;
    private Context mContext;
    static final String TAG = TweetsTimelineRecylVwAdapter.class.getSimpleName();
    private final int WITH_RETWEET = 0, WITHOUT_RETWEET = 1;

    public abstract class ViewHolderCommon extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.ivUserProfileImage)
        RoundedImageView ivUserProfileImage;

        @Bind(R.id.tvUserName)
        TextView tvUserName;

        @Bind(R.id.tvRelativeTimestamp)
        TextView tvRelativeTimestamp;

        @Bind(R.id.tvScreenName)
        TextView tvScreenName;

        @Bind(R.id.tvText)
        TextView tvText;

        @Bind(R.id.tvLikes)
        TextView tvLikes;

        @Bind(R.id.tvRetweets)
        TextView tvRetweets;

        @Bind(R.id.ivMedia)
        ImageView ivMedia;

        @Bind(R.id.ivIconVideo)
        ImageView ivIconVideo;

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

        public ViewHolderCommon(View itemView) {
            super(itemView);
        }
    }


    public class ViewHolderWithRetweet extends ViewHolderCommon {

        @Bind(R.id.ivTopIconRetweet)
        ImageView ivTopIconRetweet;

        @Bind(R.id.tvRetweetUser)
        TextView tvRetweetUser;


        public ViewHolderWithRetweet(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Tweet tweet = mTweets.get(position);

            Intent intent = new Intent(mContext, TweetDetailActivity.class);

            intent.putExtra("tweet", Parcels.wrap(tweet));

            mContext.startActivity(intent);

        }
    }

    public class ViewHolderWithoutRetweet extends ViewHolderCommon {

        public ViewHolderWithoutRetweet(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Tweet tweet = mTweets.get(position);
            Log.d(TAG, "onClick: tweet "+tweet);
            Intent intent = new Intent(mContext, TweetDetailActivity.class);
           intent.putExtra("tweet", Parcels.wrap(tweet));
            intent.putExtra("position", position);
            Log.d(TAG, "onClick: position "+position);
            mContext.startActivity(intent);

        }
    }

    public TweetsTimelineRecylVwAdapter(Context context, List tweets) {
        mContext = context;
        mTweets = tweets;
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = mTweets.get(position);
        if (tweet.getRetweetedStatus() != null) {
            return WITH_RETWEET;
        } else {
            return WITHOUT_RETWEET;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case WITH_RETWEET:
                View withRetweet = inflater.inflate(R.layout.retweet_itemview, parent, false);
                viewHolder = new ViewHolderWithRetweet(withRetweet);
                break;

            case WITHOUT_RETWEET:
                View withouRetweet = inflater.inflate(R.layout.default_tweet, parent, false);
                viewHolder = new ViewHolderWithoutRetweet(withouRetweet);
                break;

            default:
                View defaultView = inflater.inflate(R.layout.default_tweet, parent, false);
                viewHolder = new ViewHolderWithoutRetweet(defaultView);
                break;
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case WITH_RETWEET:
                Log.d(TAG, "onBindViewHolder: WITH_RETWEET");
                ViewHolderWithRetweet viewHolderWithRetweet = (ViewHolderWithRetweet) viewHolder;
                configureViewHolderWithRetweet(viewHolderWithRetweet, position);
                break;
            case WITHOUT_RETWEET:
                Log.d(TAG, "onBindViewHolder: WITHOUT_RETWEET");
                ViewHolderWithoutRetweet viewHolderWithoutRetweet = (ViewHolderWithoutRetweet) viewHolder;
                configureViewHolderWithoutRetweet(viewHolderWithoutRetweet, position);
                break;
            default:
                Log.d(TAG, "onBindViewHolder: default");
                ViewHolderWithoutRetweet viewHolderDefault = (ViewHolderWithoutRetweet) viewHolder;
                configureViewHolderWithoutRetweet(viewHolderDefault, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    private void configureViewHolderWithRetweet(ViewHolderWithRetweet viewHolderWithRetweet, int position) {
        Tweet tweet = mTweets.get(position);

        viewHolderWithRetweet.tvRetweetUser.setText(tweet.getUser().getName() + " Retweeted");

        configureViewHolderCommon(viewHolderWithRetweet, tweet.getRetweetedStatus());
    }

    private void configureViewHolderWithoutRetweet(ViewHolderWithoutRetweet viewHolderWithoutRetweet, int position) {
        Tweet tweet = mTweets.get(position);

        configureViewHolderCommon(viewHolderWithoutRetweet, tweet);
    }


    private void configureViewHolderCommon(ViewHolderCommon viewHolder, final Tweet tweet) {

        if (tweet.getUser() != null) {
            viewHolder.tvUserName.setText(tweet.getUser().getName());
            viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());

            viewHolder.ivUserProfileImage.setImageResource(0);
            Glide.with(mContext).load(tweet.getUser().getProfileImageUrl().replace(".png", "_bigger.png")).error(R.mipmap.mypic).placeholder(R.drawable.ic_launcher).dontAnimate().into(viewHolder.ivUserProfileImage);
        }
        Log.d(TAG, "configureViewHolderCommon: tweet.getCreatedAt() "+tweet.getCreatedAt());
      // viewHolder.tvRelativeTimestamp.setText(CommonUtils.getFormattedRelativeTimestamp(tweet.getCreatedAt()));
        viewHolder.tvRelativeTimestamp.setText(CommonUtils.getFormattedRelativeTimestamp(tweet.getCreatedAt()));
        // viewHolder.tvRelativeTimestamp.setText(tweet.getCreatedAt().toString());

        if (tweet.getFavoriteCount() > 0) {
            viewHolder.tvLikes.setVisibility(View.VISIBLE);
            viewHolder.tvLikes.setText(String.valueOf(tweet.getFavoriteCount()));
        } else {
            viewHolder.tvLikes.setVisibility(View.GONE);
        }

        if (tweet.getRetweetCount() > 0) {
            viewHolder.tvRetweets.setVisibility(View.VISIBLE);
            viewHolder.tvRetweets.setText(String.valueOf(tweet.getRetweetCount()));
        } else {
            viewHolder.tvRetweets.setVisibility(View.GONE);
        }

        viewHolder.ivIconReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "setOnClickListener", Toast.LENGTH_LONG);
               ((TimelineActivity)mContext).renderReplyFragment(tweet);
            }
        });

       CommonUtils.unwrapAndRenderTweetTextLinks(mContext, tweet, viewHolder.ivMedia, viewHolder.fvvVideo, viewHolder.mfpcVideo, viewHolder.ivIconVideo, viewHolder.tvText);

    }

}
