package com.applabz.findtweet;

import java.util.Observer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public abstract class SearchActivity extends MainActivity implements Observer {

	private String searchString = null;
	private ProgressDialog loadingDialog;
	
	private ListAdapter listAdapter;
	private ListView listView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_search);
    	handleIntent(getIntent());
    	
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
        
        loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage(this.getString(R.string.loading));
		
		
		/*
		listAdapter = new ListAdapter(this, R.layout.tweet, null);		
		
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);
*/
	      
    }

    @Override
    protected void onNewIntent(Intent intent) {        
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	setSearchString( intent.getStringExtra(SearchManager.QUERY));
            
            
            //Toast.makeText(getApplicationContext(), getSearchString(), Toast.LENGTH_LONG).show();//makeToast(query);            
        }
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
				
	    

    	SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView();
	    searchView.setQuery(getSearchString(),true);
	    searchView.setFocusable(true);
	    searchView.setIconified(false);
	    searchView.requestFocusFromTouch();
    	
	    //ActionBar actionBar = getActionBar();
	    
	    //actionBar.setCustomView(searchView);
	    //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    	
	    
		
		return true;
    }
    
    private String getSearchString() {
		return searchString;
	}

	private void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}
