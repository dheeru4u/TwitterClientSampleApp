package com.dheeru.apps.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.dheeru.apps.twitter.activities.TimelineActivity;
import com.dheeru.apps.twitter.utility.CommonUtils;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterRestClient> {

	final static String TAG=LoginActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().hide();
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Toast.makeText(this, "onLoginSuccess", Toast.LENGTH_LONG);
		Log.d(TAG, "onLoginSuccess: ");
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		Log.d(TAG, "loginToRest: @@@@ ");
		getClient().connect();
		if(!CommonUtils.isNetworkAvailable(this) || !CommonUtils.isOnline(this)) {
			Intent i = new Intent(this, TimelineActivity.class);
			startActivity(i);
		} else {
			getClient().connect();
		}
	}

}