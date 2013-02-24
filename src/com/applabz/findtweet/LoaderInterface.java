package com.applabz.findtweet;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public interface LoaderInterface {
	void registerDataSetObserver(DataSetObserver observer);
	void unregisterDataSetObserver(DataSetObserver observer);
	View getView(int position, View convertView, ViewGroup parent);
}
