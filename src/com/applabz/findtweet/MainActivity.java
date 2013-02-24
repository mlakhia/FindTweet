package com.applabz.findtweet;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

	private TwitterSource twitter;
	private TweetDbSource saved;
	
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
	    //actionBar.setDisplayHomeAsUpEnabled(true);
		
		MainActivity.context = getApplicationContext();
		
		setContentView(R.layout.activity_main);
		
        TweetDbSource db = new TweetDbSource(this);
        
        
        //testDB(db);
	}
	/*
	public static Context getAppContext() {
        return MainActivity.context;
    }
	
	public static void makeToast(String string){		
		Toast.makeText(getAppContext(), string, Toast.LENGTH_LONG).show();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
	    
		// Associate searchable configuration with the SearchView
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);	    
	    // get search view
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView();
		// 
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    
		return true;//super.onCreateOptionsMenu(menu);
		//return true;
	}	
	
	public void goHome(){
		Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
	}
	
	public void goSearch(){
		// before sending intent
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				goHome();
				return true;
			case R.id.menu_find:
				goSearch();
				return true;
			case R.id.menu_saved:
				//TODO
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}	

	/*
	 * Present the user with a simple dialog to enter search string
	 */
	private void acquireSearchString(final String currentSearch) {
		final EditText input = new EditText(this);
		input.setText(currentSearch);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Search Twitter")
			 .setMessage("Enter search")
			 .setView(input)
			 .setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {
					//TODO
				}
			 })
			 .setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {} // cancel
			 })
			 .show();
	}

	/*
	 * Present the user with a simple dialog to confirm a tweet deletion
	 */
	private void confirmDeleteTweet(final Tweet t) {
	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Delete tweet?")
			 .setMessage(t.getTweet())
			 .setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {
					//TODO
				}
			 })
			 .setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {} // cancel
			 })
			 .show();
	}
	
	/*
	 * Test the database 
	 */
	public void testDB(TweetDbSource db){
		
		// Inserting Tweet
        Log.d("Insert: ", "Inserting ..");        
        try {
			db.addTweet(new Tweet(
					123, 
					321, 
					"test user", 
					"test name", 
					"DAT TWEET #LULZ", 
					"20121216063056", 
					0));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        // Reading test tweet 1
        Log.d("Reading: ", "Reading test tweet 1");
        Tweet testTweet = null;
		try {
			testTweet = db.getTweet(123);
	        logTweet(testTweet);
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}	
        
        // Reading all tweets
        Log.d("Reading: ", "Reading all tweets..");        
        List<Tweet> tweets = null;
		try {
			tweets = db.getAllTweets();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		
        for (Tweet tw : tweets)
            logTweet(tw);
	}
	
	/*
	 * Log out a Tweet
	 */
	public void logTweet(Tweet tw){
		String log = 
        		"tweetId: "+tw.getTweetId()+ " , \n"+
        		"userId: " + tw.getUserId() + " , \n"+
        		"user: " + tw.getUser() + " , \n"+
        		"name: " + tw.getName() + " , \n"+
        		"tweet: " + tw.getTweet() + " , \n"+
        		"created: " + tw.getCreatedAsString() + " , \n"+
        		"rtCount: " + tw.getRetweetCount() + " , \n";
        // Writing Contacts to log
        Log.d("Tweet", log);
	}
	
}
