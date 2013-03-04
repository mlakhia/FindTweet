package com.applabz.findtweet;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class ListArrayAdapter extends ArrayAdapter<Tweet> implements ListAdapter {
		
	private Context context;
	private int layoutResourceId;	
	private ArrayList<Tweet> tweets;
	DataSetObserver observer;
	
	public ListArrayAdapter(Context context, int layoutResourceId, ArrayList<Tweet> tweets) {
		super(context, layoutResourceId);
		//super(context, layoutResourceId);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.tweets = tweets;
		
		observer = new DataSetObserver() {  
			@Override  
			public void onChanged() {  
				//notifyDataSetChanged();
				updateData();
			}
		};		
	}
	
	public void updateData(){
		this.tweets = SearchActivity.TS.getAllTweets();
	}
	
	@Override	
	public View getView(int position, View convertView, ViewGroup parent) {
		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(layoutResourceId, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Tweet i = tweets.get(position);		

		if (i != null) {
			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView textUser = (TextView) v.findViewById(R.id.textUser);
			TextView textDate = (TextView) v.findViewById(R.id.textDate);
			TextView textTweet = (TextView) v.findViewById(R.id.textTweet);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (textUser != null){
				textUser.setText(i.getUser());
			}
			if (textDate != null){
				textDate.setText(i.getCreatedAsString());
			}
			if (textTweet != null){
				textTweet.setText(i.getTweet());
			}
		}		

		return v;
	}
	
	public DataSetObserver getDataSetObserver(){
		return this.observer;
	}
	
	
	@Override
	public int getCount() { 
		return this.tweets.size(); 
	}	

	@Override
	public Tweet getItem(int position) {
		return this.tweets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.tweets.get(position).getTweetId();
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/*
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		
		return false;
	}
	*/
}
