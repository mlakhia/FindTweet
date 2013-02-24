package com.applabz.findtweet;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class ListArrayAdapter extends ArrayAdapter<Tweet> implements LoaderInterface {
	
	private Context context;
	private int layoutResourceId;	
	private ArrayList<Tweet> tweets;
	
	private final DataSetObservable dataSetObservable;
	private ArrayList<DataSetObserver> observers;

	public ListArrayAdapter(Context context, int layoutResourceId, ArrayList<Tweet> tweets) {
		super(context, layoutResourceId);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.tweets = tweets;
		
		dataSetObservable = new DataSetObservable();
		observers = new ArrayList<DataSetObserver>();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.list_tweet, parent, false);
		TextView textUser = (TextView) rowView.findViewById(R.id.textUser);
		TextView textDate = (TextView) rowView.findViewById(R.id.textDate);
		TextView textTweet = (TextView) rowView.findViewById(R.id.textTweet);	
		
		//textUser.setText(tweets.);
		// TODO : setText with values

		return rowView;
	}
	
	 public void notifyDataSetChanged() {
        this.getDataSetObservable().notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        this.getDataSetObservable().notifyInvalidated();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.getDataSetObservable().registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.getDataSetObservable().unregisterObserver(observer);
    }
    
    protected DataSetObservable getDataSetObservable() {
        return dataSetObservable;
    }

}
