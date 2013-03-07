package com.applabz.findtweet;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends MainActivity {

	private String searchString = null;
	private ProgressDialog progressDialog;
	private ListAdapter listAdapter;
	private ListView listView;
	private DataSetObservable DSO_SA = new DataSetObservable();

	static TwitterSource TS;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	handleIntent(getIntent()); // sets searchString field
    	setContentView(R.layout.activity_search);
    	
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon
	    
	    String title = new StringBuilder(getResources().getString(R.string.title_tag)).append(" ").append(searchString).toString();
	    actionBar.setTitle(title);
	    
	    TS = new TwitterSource(new TweetComparator());
	    TS.setSearchString(this.searchString);
	    
	    listAdapter = new ListArrayAdapter(this, R.layout.list_tweet, TS.getAllTweets());
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);
		
		DataSetObserver observer = new DataSetObserver() {  
			@Override  
			public void onChanged() {
				((BaseAdapter) listAdapter).notifyDataSetChanged();
				if(progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}
			}  
		};		
		DSO_SA.registerObserver(observer);
		DSO_SA.registerObserver( ((ListArrayAdapter) listAdapter).getDataSetObserver() );
		TS.setObservers(DSO_SA);		
		
    	progressDialog = ProgressDialog.show(this, "", this.getString(R.string.loading));
    	
    	listView.setOnScrollListener(new EndlessScrollListener());
    	
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Log.v("", parent.toString());
            	Log.v("", ""+position);
            	Log.v("", v.toString());
            	Log.v("", ""+id);
            	
            	Tweet tweet = (Tweet) listAdapter.getItem(position);
            	MainActivity.db.addTweet(tweet);
            	
            	
            	
            	View layout = getLayoutInflater().inflate(R.layout.toast_layout, (ViewGroup)findViewById(R.id.toast_layout_root));
            	TextView text = (TextView) layout.findViewById(R.id.text);
            	text.setText(R.string.added_favorites);

            	Toast toast = new Toast(getApplicationContext());
            	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            	toast.setDuration(Toast.LENGTH_SHORT);
            	toast.setView(layout);
            	toast.show();
            	//Toast.makeText(context, R.string.added_favorites, Toast.LENGTH_LONG).show();
			}
        });
		
    }
    
    
    @Override
	public void onBackPressed() {
	    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}

	@Override
	public void startActivity(Intent intent) {
	    super.startActivity(intent);
	    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}
	
    
    @Override
    protected void onNewIntent(Intent intent) {
    	setIntent(intent);
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	this.searchString = intent.getStringExtra(SearchManager.QUERY);
        	if(listAdapter != null)
        		((BaseAdapter) listAdapter).notifyDataSetChanged();
        }
    }    
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView();
	    searchView.setQuery(this.searchString, false);
	    searchView.setFocusable(true);
	    searchView.setIconified(true);
	    searchView.requestFocusFromTouch();
    	
		return true;
    }
	


	public class EndlessScrollListener implements OnScrollListener {
	
	    private int visibleThreshold = 5;
	    private int currentPage = 0;
	    private int previousTotal = 0;
	    private boolean loading = true;
	
	    public EndlessScrollListener() {
	    }
	    public EndlessScrollListener(int visibleThreshold) {
	        this.visibleThreshold = visibleThreshold;
	    }
	
	    @Override
	    public void onScroll(AbsListView view, int firstVisibleItem,
	            int visibleItemCount, int totalItemCount) {
	        if (loading) {
	            if (totalItemCount > previousTotal) {
	                loading = false;
	                previousTotal = totalItemCount;
	                currentPage++;
	            }
	        }
	        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
	            // I load the next page of gigs using a background task,
	            // but you can call any function here.
	            //new LoadGigsTask().execute(currentPage + 1);
	            
	        	TS.refresh();
	        	
	        	loading = true;
	        }
	    }
	
	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) {
	    }
	}

}