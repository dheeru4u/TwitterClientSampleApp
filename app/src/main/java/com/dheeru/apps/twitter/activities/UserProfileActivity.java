package com.dheeru.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.adapters.ProfileFragmentPagerAdapter;
import com.dheeru.apps.twitter.fragments.TimelineFrag;
import com.dheeru.apps.twitter.models.UrlEntity;
import com.dheeru.apps.twitter.models.User;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.dheeru.apps.twitter.utility.TwitterRestApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 6/9/16.
 */
public class UserProfileActivity extends BaseTimelineActivity {

    @Bind(R.id.ivBanner)
    ImageView ivBanner;

    @Bind(R.id.ivUserProfileImage)
    ImageView ivUserProfileImage;

    @Bind(R.id.tvUserName)
    TextView tvUserName;

    @Bind(R.id.tvScreenName)
    TextView tvScreenName;

    @Bind(R.id.tvLocation)
    TextView tvLocation;

    @Bind(R.id.tvDisplayUrl)
    TextView tvDisplayUrl;

    @Bind(R.id.tvDescription)
    TextView tvDescription;

    @Bind(R.id.tvFollowingCount)
    TextView tvFollowingCount;

    @Bind(R.id.tvFollowingLabel)
    TextView tvFollowingLabel;

    @Bind(R.id.tvFollowersCount)
    TextView tvFollowersCount;

    @Bind(R.id.tvFollowersLabel)
    TextView tvFollowersLabel;

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Bind(R.id.sliding_tabs)
    TabLayout slidingTabs;

    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_bird_white);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTwitterClient = TwitterRestApplication.getRestClient();

        ProfileFragmentPagerAdapter profileFragmentPagerAdapter;
        if (getIntent().getExtras() != null && getIntent().getLongExtra("userId", -1) != -1) {
            long userId = getIntent().getLongExtra("userId", -1);
            loadUserProfile(userId);
            profileFragmentPagerAdapter = new ProfileFragmentPagerAdapter(getSupportFragmentManager(), UserProfileActivity.this, userId);
        } else {
            loadUserProfile();
            profileFragmentPagerAdapter = new ProfileFragmentPagerAdapter(getSupportFragmentManager(), UserProfileActivity.this);
        }
        viewpager.setAdapter(profileFragmentPagerAdapter);
        slidingTabs.setupWithViewPager(viewpager);
        mSmartFragmentStatePagerAdapter = profileFragmentPagerAdapter;
    }

    private void loadUserProfile() {
        pbLoading.setVisibility(View.VISIBLE);

        boolean requestSent = mTwitterClient.getUserProfile(this, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                User user = gson.fromJson(responseString, User.class);
                render(user);
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                Toast.makeText(UserProfileActivity.this, "Sorry, unable to get Profile. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });

        if (!requestSent) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }


    private void loadUserProfile(Long userId) {
        pbLoading.setVisibility(View.VISIBLE);

        boolean requestSent = mTwitterClient.getUserProfile(this, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                User user = gson.fromJson(responseString, User.class);
                render(user);
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), String.valueOf(statusCode));
                Toast.makeText(UserProfileActivity.this, "Sorry, unable to get Profile. Please try again later.", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        }, userId);

        if (!requestSent) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }


    private void render(final User user) {
        ivBanner.setImageResource(0);
        ivUserProfileImage.setImageResource(0);
        Glide.with(this).load(user.getProfileBannerUrl()).error(R.drawable.shape_banner_placeholder).placeholder(R.drawable.shape_banner_placeholder).into(ivBanner);
        Glide.with(this).load(user.getProfileImageUrl()).error(R.drawable.photo_placeholder).placeholder(R.drawable.photo_placeholder).dontAnimate().into(ivUserProfileImage);
        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());

        String description = "";
        if (user.getDescription() != null) {
            description = user.getDescription();
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        if (user.getLocation() != null && !user.getLocation().equals("")) {
            tvLocation.setText(user.getLocation());
        } else {
            tvLocation.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvFollowingCount.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.tvDisplayUrl);
            tvFollowingCount.setLayoutParams(layoutParams);
        }

        if (user.getEntities() != null && user.getEntities().getUrl().urls != null && user.getEntities().getUrl().urls.size() > 0) {
            tvDisplayUrl.setText(" " + user.getEntities().getUrl().urls.get(0).getDisplayUrl());

        } else {
            tvDisplayUrl.setVisibility(View.GONE);
        }

        if (user.getStatus() != null && user.getStatus().getEntities() != null && user.getStatus().getEntities().getUrls() != null) {
            for (UrlEntity url : user.getStatus().getEntities().getUrls()) {
                String wrapperUrl = url.getUrl();
                String displayUrl = url.getDisplayUrl();
                description = description.replace(wrapperUrl, displayUrl);
            }
        }

        tvDescription.setText(CommonUtils.formatTwitterText(this, description));

        tvFollowingCount.setText(CommonUtils.formatNumber(user.getFriendsCount()));
        tvFollowersCount.setText(CommonUtils.formatNumber(user.getFollowersCount()));

        tvFollowingLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, FriendsActivity.class);
                intent.putExtra("userId", user.getId());
                startActivity(intent);
            }
        });

        tvFollowersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, FollowersActivity.class);
                intent.putExtra("userId", user.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        TimelineFrag timelineFragement = (TimelineFrag) mSmartFragmentStatePagerAdapter.getRegisteredFragment(viewpager.getCurrentItem());
        if (timelineFragement != null) {
            timelineFragement.reload();
        }
    }

}
