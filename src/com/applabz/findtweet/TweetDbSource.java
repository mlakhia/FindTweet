package com.applabz.findtweet;

/* TweetDbSource
 *   - stores tweets in a sqlite3 database.
 *   - "add" and "retrieve" operations

 */

import java.text.ParseException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TweetDbSource extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "tweet.db";
	private static final String TABLE_NAME = "Tweet";

	// Tweets Table Column Names
	private static final String KEY_TW_ID = "tweet_id";
	private static final String KEY_TW_USERID = "user_id";
	private static final String KEY_TW_USER = "user";
	private static final String KEY_TW_NAME = "name";
	private static final String KEY_TW_TWEET = "tweet";
	private static final String KEY_TW_CREATED = "date_created";
	private static final String KEY_TW_RTCOUNT = "retweet_count";

	public TweetDbSource(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}
	
	private DataSetObservable observers;

	public void setObservers(DataSetObservable observers) {
		this.observers = observers;
	}
	
	private void onDBUpdated(){
		if(observers != null)
			observers.notifyChanged();
	}

	// Create Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
				+ KEY_TW_ID + " LONG PRIMARY KEY," 
				+ KEY_TW_USERID	+ " LONG," 
				+ KEY_TW_USER + " TEXT," 
				+ KEY_TW_NAME + " TEXT," 
				+ KEY_TW_TWEET + " TEXT,"
				+ KEY_TW_CREATED + " TEXT," 
				+ KEY_TW_RTCOUNT + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrade Database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		// create tables again
		onCreate(db);
		onDBUpdated();
	}

	// Add New Tweet
	public void addTweet(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TW_ID, tweet.getTweetId());
		values.put(KEY_TW_USERID, tweet.getUserId());
		values.put(KEY_TW_USER, tweet.getUser());
		values.put(KEY_TW_NAME, tweet.getName());
		values.put(KEY_TW_TWEET, tweet.getName());
		values.put(KEY_TW_CREATED, tweet.getCreatedAsString());
		values.put(KEY_TW_RTCOUNT, tweet.getRetweetCount());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		//db.close(); // Closing database connection
		onDBUpdated();
	}
	
	public boolean isTweetSaved(Tweet tweet){		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT count(*) FROM " + TABLE_NAME + " WHERE " + KEY_TW_ID + "='"+tweet.getTweetId()+"'", null);
		cursor.moveToFirst();
		int savedTweets = cursor.getInt(0);
		cursor.close();
		
		return (savedTweets > 0);
	}

	// Get Single Tweet
	public Tweet getTweet(long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(
				TABLE_NAME, 
				new String[] { KEY_TW_ID, KEY_TW_USERID, KEY_TW_USER, KEY_TW_NAME, KEY_TW_TWEET, KEY_TW_CREATED, KEY_TW_RTCOUNT }, 
				KEY_TW_ID + "=?",
				new String[] { String.valueOf(id) }, 
				null, null, null, null);
		
		if (cursor != null)
			cursor.moveToFirst();

		Tweet tweet = null;
		try {			
			tweet = new Tweet(
					Long.parseLong(cursor.getString(1)),
					Long.parseLong(cursor.getString(2)), 
					cursor.getString(2),
					cursor.getString(3), 
					cursor.getString(4), 
					cursor.getString(5));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tweet;
	}

	// Get All Tweets
	public ArrayList<Tweet> getAllTweets() {
		ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// loop through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Tweet tweet = null;
				try {
					tweet = new Tweet(
							Long.parseLong(cursor.getString(0)),
							Long.parseLong(cursor.getString(1)),
							cursor.getString(2), 
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				tweetList.add(tweet);
			} while (cursor.moveToNext());
		}

		return tweetList;
	}

	// Get All Tweets Count
	public int size() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cursorSize = cursor.getCount();
		//cursor.close();
		return cursorSize;
	}

	// Update Single Tweet
	public int updateTweet(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TW_ID, tweet.getTweetId());
		values.put(KEY_TW_USERID, tweet.getUserId());
		values.put(KEY_TW_USER, tweet.getUser());
		values.put(KEY_TW_NAME, tweet.getName());
		values.put(KEY_TW_TWEET, tweet.getTweet());
		values.put(KEY_TW_CREATED, tweet.getCreatedAsString());
		values.put(KEY_TW_RTCOUNT, tweet.getRetweetCount());

		onDBUpdated();
		
		return db.update(TABLE_NAME, values, KEY_TW_ID + " = ?", new String[] { String.valueOf(tweet.getTweetId()) });
	}

	// Delete Single Tweet
	public void deleteTweet(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_NAME, KEY_TW_ID + " = ?", new String[] { String.valueOf(tweet.getTweetId()) });
	    //db.close();
		onDBUpdated();
	}

}
