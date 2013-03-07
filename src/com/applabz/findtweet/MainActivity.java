package com.applabz.findtweet;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

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

	private SpeechRecognizer speechRecognizer;
	private final int SPEECHTOTEXT = 1;
	private String voiceQuery;
	
	public static TweetDbSource db;
	
	static Context context;
	Menu menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.context = this;
		setContentView(R.layout.activity_main);
		
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getBaseContext());
		
		ActionBar actionBar = getActionBar();
	    //actionBar.setDisplayHomeAsUpEnabled(true);
		
		// start db		
        db = new TweetDbSource(this);
        
        
        TextView tvWelcome = (TextView) findViewById(R.id.welcome);
        /*
        tvWelcome.setOnTouchListener(new View.OnTouchListener () {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View layout = getLayoutInflater().inflate(R.layout.toast_layout, (ViewGroup)findViewById(R.id.toast_layout_root));
            	TextView text = (TextView) layout.findViewById(R.id.text);
            	text.setText(R.string.added_favorites);

            	Toast toast = new Toast(getApplicationContext());
            	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            	toast.setDuration(Toast.LENGTH_SHORT);
            	toast.setView(layout);
            	toast.show();
				return true;
			}
        });
        */
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_main, menu);	
		this.menu = menu;
		getMenuInflater().inflate(R.menu.options_menu, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView();
		
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    
	    searchView.setFocusable(true);
	    searchView.setIconified(false);
	    searchView.setIconifiedByDefault(false);
	    searchView.setSubmitButtonEnabled(true);
	    searchView.requestFocusFromTouch();
	    
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener (){
			@Override
			public boolean onQueryTextSubmit(String query) {
				if(!internetReady())
					return true; // returning true notifys that you've handled, return false lets query pass
				return false;
			}
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});	    
		return super.onCreateOptionsMenu(menu);
	}
	/*
	//@Override
	public boolean onSearch() {
		if(internetReady())
			return true;
	
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchManager.stopSearch();
	    return false;
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		switch(item.getItemId()) {
			case android.R.id.home:
				goHome();
				return true;
			case R.id.menu_find:
				return internetReady();		
			case R.id.menu_favorites:
				goSaved();
				return true;
			case R.id.menu_settings:
				goSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}	
	/*
	private void goVoiceSearch() {
		
		 Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		 
         // Getting an instance of PackageManager
         PackageManager pm = getPackageManager();

         // Querying Package Manager
         List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

         if(activities.size()<=0){
             Toast.makeText(getBaseContext(),
                 "No Activity found to handle the action ACTION_RECOGNIZE_SPEECH",
                 Toast.LENGTH_SHORT).show();
                 return;
         }

         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
             RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
             intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
             startActivityForResult(intent, SPEECHTOTEXT);
		
	}	
	*/
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        /*
        //EditText etText = (EditText) findViewById(R.id.et_text);
        ListView lvText = (ListView) findViewById(R.id.listView);
 	
        switch (requestCode) {
            case SPEECHTOTEXT:
                if (resultCode == RESULT_OK && null != data) {
                     //this.voiceQuery = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                     
                     //SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView(); 
             	    //searchView.setQuery(this.voiceQuery, true);
                }
                break;
        }*/
    }

	public void goHome(){
		Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivityForResult(intent, 400);
        startActivity(intent);
    	//overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public void goSaved(){
    	Intent intent = new Intent(this, FavoritesActivity.class);
    	//startActivityForResult(intent, 400);
    	startActivity(intent);
    	//overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}
	
	public void goSettings(){
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this)
				.setIcon(drawable.ic_dialog_alert)
				.setTitle(context.getString(R.string.quit_title))
				.setMessage(context.getString(R.string.quit_text))
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								moveTaskToBack(true);
								MainActivity.this.finish();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();

	}


	@Override
	public void startActivity(Intent intent) {
	    super.startActivity(intent);
	    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}

	@Override
	public void finish() {
	    super.finish();
	    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}
	
	
	
	public void closeKeyboard(){
		//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	
	
	public boolean internetReady(){
		// before sending intent, can return false to not send intent		
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		if(cd.isConnectedToInternet()){
			Log.d("Network", "Connected");
			return true;
		}
		showAlertDialog(context, getString(R.string.nointernet_title), getString(R.string.nointernet_text), true);
		return false;
	}
	
	/**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
	public void showAlertDialog(Context context, final String title, final String message, Boolean status) {    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 
    	alert
    	 .setTitle(title)
		 .setMessage(message)
		 .setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int button) {	
				if(title.equalsIgnoreCase(getString(R.string.nointernet_title)) )
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				return;
			}
		 })
		 .setIcon(drawable.ic_dialog_alert)		 
		 .show();
    }		
}
