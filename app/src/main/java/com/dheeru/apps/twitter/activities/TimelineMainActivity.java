package com.dheeru.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.adapters.TimelineFragmentPagerAdapter;
import com.dheeru.apps.twitter.fragments.TimelineFrag;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.dheeru.apps.twitter.utility.TwitterRestApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 6/9/16.
 */
public class TimelineMainActivity extends BaseTimelineActivity {

    @Bind(R.id.tvOfflineMode)
    TextView tvOfflineMode;

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Bind(R.id.sliding_tabs)
    TabLayout slidingTabs;

    private TimelineFragmentPagerAdapter mTimelineFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setLogo(R.drawable.ic_twitter_bird);
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

        mTimelineFragmentPagerAdapter = new TimelineFragmentPagerAdapter(getSupportFragmentManager(), TimelineMainActivity.this);
        mSmartFragmentStatePagerAdapter = mTimelineFragmentPagerAdapter;

        viewpager.setAdapter(mTimelineFragmentPagerAdapter);
        slidingTabs.setupWithViewPager(viewpager);


        mTwitterClient = TwitterRestApplication.getRestClient();


        if (!CommonUtils.isNetworkAvailable(this) || !CommonUtils.isOnline(this)) {
            tvOfflineMode.setVisibility(View.VISIBLE);
        } else {
            tvOfflineMode.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout:
                mTwitterClient.clearAccessToken();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                return true;
            case R.id.menu_user_profile:
                Intent profileIntent = new Intent(this, UserProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.menu_search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.menu_dm:
                Intent dmIntent = new Intent(this, DirectMessageActivity.class);
                startActivity(dmIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        TimelineFrag timelineFragement = (TimelineFrag) mSmartFragmentStatePagerAdapter.getRegisteredFragment(viewpager.getCurrentItem());
        if(timelineFragement != null){
            timelineFragement.reload();
        }
    }
}

