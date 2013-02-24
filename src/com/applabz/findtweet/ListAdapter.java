package com.applabz.findtweet;

import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListAdapter extends ListActivity implements LoaderInterface {

	Context context;
	int layoutResourceId;
	Tweet data[] = null;

	private DataSetObservable dso = null;

	public ListAdapter(Context context, int layoutResourceId, Tweet[] data) {
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;

		dso = new DataSetObservable();
	}
/*
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WeatherHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new WeatherHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

			row.setTag(holder);
		} else {
			holder = (WeatherHolder) row.getTag();
		}

		Weather weather = data[position];
		holder.txtTitle.setText(weather.title);
		holder.imgIcon.setImageResource(weather.icon);

		return row;
	}

	static class WeatherHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}
	
	*/


	public void getView(int position, View convertView, ViewGroup parent) {
		
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	public void registerDataSetObserver(DataSetObserver observer) {

	}

	public boolean isEmpty() {
		return false;
	}

	public boolean hasStableIds() {
		return false;
	}

	public int getViewTypeCount() {
		return 0;
	}


	public int getItemViewType(int position) {
		return 0;
	}

	public long getItemId(int position) {
		return 0;
	}

	public Object getItem(int position) {
		return null;
	}

	public int getCount() {
		return 0;
	}

	public boolean isEnabled(int position) {
		return false;
	}

	public boolean areAllItemsEnabled() {
		return false;
	}

}
