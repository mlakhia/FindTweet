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
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class ListArrayAdapter extends ArrayAdapter<Tweet> implements Observer {
		
	private Context context;
	private int layoutResourceId;	
	private ArrayList<Tweet> tweets;

	public ListArrayAdapter(Context context, int layoutResourceId, ArrayList<Tweet> tweets) {
		super(context, layoutResourceId);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.tweets = tweets;
	}
	
	@Override	
	public View getView(int position, View convertView, ViewGroup parent) {
		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_tweet, null);
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
	
	@Override
	public int getCount() { 
		return tweets.size(); 
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
	public void update(Observable observable, Object data) {
		notifyDataSetChanged();		
	}	
	
}
