package com.dheeru.apps.twitter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.dheeru.apps.twitter.utility.CommonUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "J4INafyUeGPvZUFY5G5F9MevZ";
	public static final String REST_CONSUMER_SECRET = "Hd2UaApdhofFuWR63bVaBmQs1K5oTkxrkvvgavOmVvGIsBXbqU";
	public static final String REST_CALLBACK_URL = "oauth://twtClientApp";

	public TwitterRestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public boolean getNewTweets(Activity activity, AsyncHttpResponseHandler handler, long sinceId) {
		if(!CommonUtils.isNetworkAvailable(activity) || !CommonUtils.isOnline(activity)){
			Log.d(this.getClass().getSimpleName(), "getNewTweets: isNetworkAvailable false");
			return false;
		}
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 10);
		params.put("since_id", sinceId);
		client.get(apiUrl, params, handler);
		return true;
	}

	public boolean getOldTweets(Activity activity, AsyncHttpResponseHandler handler, long maxId) {
		if(!CommonUtils.isNetworkAvailable(activity) || !CommonUtils.isOnline(activity)){
			return false;
		}
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 10);
		params.put("max_id", maxId - 1);
		client.get(apiUrl, params, handler);
		return true;
	}

	public boolean postMessage(Activity activity, AsyncHttpResponseHandler handler, long tweetId, String message){
		if(!CommonUtils.isNetworkAvailable(activity) || !CommonUtils.isOnline(activity)){
			return false;
		}
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", message);
		if(tweetId != -1)
			params.put("in_reply_to_status_id", tweetId);
		client.post(apiUrl, params, handler);
		return true;
	}

	public boolean getUserTimeline(Activity activity, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 10);
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
		return true;
	}
}