package ca.qc.johnabbott.cs603.asg2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.util.Log;


/** TwittterSource
 *
 *	@author Ian Clement
 *	@version Feb 13th, 2013
 *
 *   - given a search string, retrieve matching tweets from twitter.com
 *   - uses twitter API 1.0
 *   - stores tweets in hashtable
 *   - sort tweets using a comparator
 *   
 *   TODO: include retweet count (API 1.1)
 *	 TODO: more parameters for generating query (ex: result_type, rpp/count)
 *
 */
public class TwitterSource {

		
	//TODO: android.text has a DateFormat.. might be better
	public static DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	/* API 1.0 */
	final private static String BASE_URL = "http://search.twitter.com/search.json";
	final private static String DEFAULT_PARAMS = "&rpp=100&include_entities=true&result_type=mixed";
	
	/* API 1.1 - requires OAuth */
	/*
	final private static String BASE_URL = "http://api.twitter.com/1.1/search/tweets.json";
	final private static String DEFAULT_PARAMS = "&count=100&include_entities=true&result_type=mixed";
    */
	
	private String search;
	private Hashtable<Long, Tweet> tweets;
	
	/* observers will be notified after a successful refresh (from a separate thread) */
	private DataSetObservable observers;
	
	private Comparator<Tweet> comparer;
	private long[] ids;
	
	/**
	 * Create a twitter source by supplying a search string and a comparator. It will initiate an immediate {@link #refresh()}
	 * of the source. The comparator will order the IDs returned from {@link #getIds()}.
	 * @param search    The initial search string
	 * @param comparer  The comparator used to order tweets
	 */
	public TwitterSource(String search, Comparator<Tweet> comparer) {
		this(comparer);	
		setSearchString(search);
	}
		
	/**
	 * Create a twitter source by supplying sa comparator. The comparator will order the IDs returned from {@link #getIds()}.
	 * @param comparer   The comparator used to order tweets
	 */
	public TwitterSource(Comparator<Tweet> comparer) {
		this.comparer = comparer;
		this.tweets = new Hashtable<Long, Tweet>(100);
	}

	/** Get the current search string.
	 * 
	 * @return  The current search string
	 */
	public String getSearchString() {
		return search;
	}

	/** Set the current search string. Initiates a refresh of the source.
	 * 
	 * @param search    The new search string
	 */
	public void setSearchString(String search) {
		if(!search.equals(this.search)) {
			this.search = search;
			refresh();
		}
	}
	
	/** Set the observers of the twitter source, that is, the set of "clients" to be notified 
	 *  when the source is updated (i.e. on a {@link #refresh()})
	 * 
	 * @param observers    The set of observers to be notified
	 */
	public void setObservers(DataSetObservable observers) {
		this.observers = observers;
	}

	/** A refresh initiates the fetching of tweets from twitter.com. This happens on a separate "thread"
	 *  after which all registered observers are notified of the changed data. Make sure that {@link #setObservers(DataSetObservable)} 
	 *  has been supplied with observers for all clients. 
	 */
	public void refresh() {
		if(!search.equals(""))
			new FetchTweetsTask().execute();
	}
	
	/* Ids are ordered after a successful refresh. either size() or null check before using */
	/** Get the ordered IDs. They are reset after each refresh according to the TwitterSource's supplied Comparator.
	 *  Be sure to check the {@link #size()} in case of no tweets.
	 * 
	 * @return An array of ordered tweet IDs.
	 */
	public long[] getIds() {
		return ids;
	}
	
	/** Get the number of tweets currently retrieved from twitter.com.
	 * 
	 * @return The number of tweets currently retrieved.
	 */
	public int size() {
		return tweets.size();
	}
	
	/** Get a tweet by its ID
	 *
	 * @param id   The tweet ID
	 * @return     The tweet correspoding to the supplied ID
	 */
	public Tweet getTweet(long id) {
		return tweets.get(id);
	}

	/* Asynchronous fetching of tweets from twitter.com */
	private class FetchTweetsTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... s) {
			return fetchTweets();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result && observers != null)
				observers.notifyChanged();
		}
	}
	
	private boolean fetchTweets() {
		
		String queryUrl;
		try {
			queryUrl = "?q=" + URLEncoder.encode(search, "UTF-8")  +  DEFAULT_PARAMS;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		
		InputStream is = null;
		try {
			URL url = new URL(BASE_URL + queryUrl);
			Log.d("TwitterSource", "GET " + url);
			URLConnection conn = url.openConnection();

			Log.d("TwitterSource","Printing headers");
			Map<String, List<String>> headers = conn.getHeaderFields();
			for(String key : headers.keySet())
				Log.d("TwitterSource", "\t" + key + ": " + headers.get(key));
			Log.d("TwitterSource","done.");

			is = conn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		/*
		 * Read an entire stream into a String
		 * See "Stupid Scanner Tricks" (http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html)
		 */	 
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String json = scanner.hasNext() ? scanner.next() : "";
		
		/* Sample JSON output. Fields marked with + are extracted below 
		 * and stored in a Tweet object
		 * 
		 * {
		 *   "completed_in":0.048,
		 *   "max_id":299199349446033411,
		 *   "max_id_str":"299199349446033411",
		 *   "next_page":"?page=2&max_id=299199349446033411&q=java",
		 *   "page":1,
		 *   "query":"java",
		 *   "refresh_url":"?since_id=299199349446033411&q=java",
		 *   "results":
		 *     [        
		 *       {
		 * +       "created_at":"Wed, 06 Feb 2013 16:54:18 +0000",
		 * +       "from_user":"slieeza",
		 * +       "from_user_id":615875491,
		 *         "from_user_id_str":"615875491",
		 * +       "from_user_name":"Ty.Firdausi\u2665",
		 *         "geo":null,
		 * +       "id":299199349446033411,
		 *         "id_str":"299199349446033411",
		 *         "iso_language_code":"en",
		 *         "metadata":{"result_type":"recent"},
		 *         "profile_image_url":"http:\/\/a0.twimg.com\/profile_images\/3214167165\/1592f3fdbaa4f8b64f8067ebbd75aad5_normal.jpeg",
		 *         "profile_image_url_https":"https:\/\/si0.twimg.com\/profile_images\/3214167165\/1592f3fdbaa4f8b64f8067ebbd75aad5_normal.jpeg",
		 *         "source":"&lt;a href=&quot;http:\/\/twitter.com\/download\/iphone&quot;&gt;Twitter for iPhone&lt;\/a&gt;",
		 * +       "text":"#NowPlaying Opera Van Java",
		 *         "to_user":null,
		 *         "to_user_id":0,
		 *         "to_user_id_str":"0",
		 *         "to_user_name":null
		 *       },
		 *       {
		 *         ...
		 */
		
		int count=0;
		try {	
			JSONObject results = new JSONObject(new JSONTokener(json));
			JSONArray tweetsRaw = results.getJSONArray("results");
					
			for(int i=0; i<tweetsRaw.length(); i++) {
				try {
				JSONObject tweetRaw = (JSONObject)tweetsRaw.get(i);
				Tweet t = new Tweet(tweetRaw.getLong("id"),
									tweetRaw.getLong("from_user_id"),
									tweetRaw.getString("from_user"),
									tweetRaw.getString("from_user_name"),
									tweetRaw.getString("text"),
									dateFormatter.parse(tweetRaw.getString("created_at"))
							        );
				tweets.put(t.getTweetId(), t);
				count++;
				} catch (ParseException e) {
					Log.d("TwitterSource", "Skipping tweet couldn't parse date: " + e.getMessage());
				} 
			}			
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} 
		
		Log.d("TwitterSource", "Added " + count + " to source.");
		if(count > 0)
			sortTweets();
		return true;
	}


	private void sortTweets()  {
		ArrayList<Tweet> list = new ArrayList<Tweet>(tweets.values());
		Collections.sort(list, comparer);
		ids = new long[list.size()];
		int i=0;
		for(Tweet t : list)
			ids[i++] = t.getTweetId();
	}
	
}
