package ca.qc.johnabbott.cs603.asg2;

/* TweetDbSource
 *   - stores tweets in a sqlite3 database.
 *   - "add" and "retrieve" operations
 
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TweetDbSource extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "tweet.db";
	private static final String TABLE_NAME = "Tweet";
	

	public TweetDbSource(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
   
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		/* generate and execute CREATE TABLE command */
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);	
    }
	

	/* TODO: insert, delete and select commands */

}
