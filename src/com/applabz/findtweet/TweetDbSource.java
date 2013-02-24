package com.applabz.findtweet;

/* TweetDbSource
 *   - stores tweets in a sqlite3 database.
 *   - "add" and "retrieve" operations

 */

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TweetDbSource extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "tweet.db";
	private static final String TABLE_NAME = "Tweet";

	// Tweets Table Columns Names
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

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
				+ KEY_TW_ID + " INTEGER PRIMARY KEY," 
				+ KEY_TW_USERID	+ " TEXT," 
				+ KEY_TW_USER + " TEXT," 
				+ KEY_TW_NAME + " TEXT," 
				+ KEY_TW_TWEET + " TEXT,"
				+ KEY_TW_CREATED + " TEXT," 
				+ KEY_TW_RTCOUNT + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading Database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		// create tables again
		onCreate(db);
	}

	// Adding new contact
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
		db.close(); // Closing database connection
	}

	// Getting single contact
	public Tweet getTweet(int id) throws NumberFormatException, ParseException {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(
				TABLE_NAME, 
				new String[] { KEY_TW_ID, KEY_TW_USERID, KEY_TW_USER, KEY_TW_NAME, KEY_TW_TWEET, KEY_TW_CREATED, KEY_TW_RTCOUNT }, 
				KEY_TW_ID + "=?",
				new String[] { String.valueOf(id) }, 
				null, null, null, null);
		
		if (cursor != null)
			cursor.moveToFirst();

		Tweet tweet = new Tweet(
				Long.parseLong(cursor.getString(0)),
				Long.parseLong(cursor.getString(1)), 
				cursor.getString(2),
				cursor.getString(3), 
				cursor.getString(4), 
				cursor.getString(5));
		// return contact
		return tweet;
	}

	// Getting All Contacts
	public List<Tweet> getAllTweets() throws NumberFormatException,
			ParseException {
		List<Tweet> tweetList = new ArrayList<Tweet>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Tweet tweet = new Tweet(
						Long.parseLong(cursor.getString(0)),
						Long.parseLong(cursor.getString(1)),
						cursor.getString(2), 
						cursor.getString(3),
						cursor.getString(4),
						cursor.getString(5));
				// Adding contact to list
				tweetList.add(tweet);
			} while (cursor.moveToNext());
		}

		// return contact list
		return tweetList;

	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Updating single contact
	public int updateContact(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TW_ID, tweet.getTweetId());
		values.put(KEY_TW_USERID, tweet.getUserId());
		values.put(KEY_TW_USER, tweet.getUser());
		values.put(KEY_TW_NAME, tweet.getName());
		values.put(KEY_TW_TWEET, tweet.getTweet());
		values.put(KEY_TW_CREATED, tweet.getCreatedAsString());
		values.put(KEY_TW_RTCOUNT, tweet.getRetweetCount());

		// updating row
		return db.update(TABLE_NAME, values, KEY_TW_ID + " = ?",
				new String[] { String.valueOf(tweet.getTweetId()) });
	}

	// Deleting single contact
	public void deleteContact(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_NAME, KEY_TW_ID + " = ?",
	            new String[] { String.valueOf(tweet.getTweetId()) });
	    db.close();
	}

}
