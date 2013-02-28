package com.applabz.findtweet;

import android.os.Bundle;
import android.os.Handler;
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
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("unused")
public class MainActivity extends Activity {
	
	class TweetComparator implements Comparator<Tweet> {
    	public int compare(Tweet lhs, Tweet rhs) {
    	    if (lhs.getTweetId() == rhs.getTweetId()) {
    	      return 0;
    	    } else {
    	      return lhs.getTweetId() < rhs.getTweetId() ? -1 : 1;
    	    }
    	}
    }

	public static TweetDbSource db;
	//protected static TwitterSource TS;
	
	static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.context = getApplicationContext();
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
	    //actionBar.setDisplayHomeAsUpEnabled(true);
		
		// start db
		
        db = new TweetDbSource(this);        
        
        //add3Tweets(); // to database
        //testDB();
	}
	
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
	    searchView.setIconifiedByDefault(false);
	    
		return true;//super.onCreateOptionsMenu(menu);
		//return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		switch(item.getItemId()) {
			case android.R.id.home:
				goHome();
				return true;
			case R.id.menu_find:
				if(!goSearch()) break;
				return true;
			case R.id.menu_saved:
				goSaved();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		//invalidateOptionsMenu();
		return false;
	}	
	
	public void goHome(){
		Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, 400);//startActivity(intent);
    	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public void goSaved(){

    	Intent intent = new Intent(this, SavedActivity.class);
    	startActivityForResult(intent, 400);//startActivity(intent);
    	overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public boolean goSearch(){
		// before sending intent, can return false to not send intent		
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());		 
		
		if(cd.isConnectedToInternet()){
			Log.d("Network", "Connected");
			return true;
		}else{
			showAlertDialog(context, null, getString(R.string.nointernet), true);
			return false;
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
	 * Test the database 
	 
	public void testDB(){
		
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
		}	
        
        // Reading all tweets
        Log.d("Reading: ", "Reading all tweets..");        
        List<Tweet> tweets = null;
		try {
			tweets = db.getAllTweets();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
        for (Tweet tw : tweets)
            logTweet(tw);
	}
	
	/*
	 * Log out a Tweet
	 
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
	}*/
	
	
	/**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
	public void showAlertDialog(Context context, String title, String message, Boolean status) {    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 
    	alert.setTitle(title)
		 .setMessage(message)
		 .setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int button) {				
				return;
			}
		 })
		 .setIcon(android.R.drawable.ic_dialog_alert)		 
		 .show();
    }

	
}
