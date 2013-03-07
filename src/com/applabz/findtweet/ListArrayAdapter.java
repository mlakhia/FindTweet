package com.applabz.findtweet;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ListArrayAdapter extends ArrayAdapter<Tweet> implements ListAdapter {
		
	private Context context;
	private int layoutResourceId;	
	private ArrayList<Tweet> tweets;
	DataSetObserver observer;
	
	public ListArrayAdapter(Context context, int layoutResourceId, ArrayList<Tweet> tweets) {
		super(context, layoutResourceId);
		
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.tweets = tweets;
		
		observer = new DataSetObserver() {  
			@Override  
			public void onChanged() {  
				updateData();
				notifyDataSetChanged();
			}
		};		
	}
	
	public void updateData(){
		this.tweets = SearchActivity.TS.getAllTweets();
	}
	
	@Override	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(layoutResourceId, null);
		}
		Tweet i = tweets.get(position);		

		if (i != null) {
			TextView textUser = (TextView) v.findViewById(R.id.textUser);
			TextView textDate = (TextView) v.findViewById(R.id.textDate);
			TextView textTweet = (TextView) v.findViewById(R.id.textTweet);

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
		return 0;
	}	
}
