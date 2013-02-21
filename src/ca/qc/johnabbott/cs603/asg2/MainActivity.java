package ca.qc.johnabbott.cs603.asg2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Comparator;

public class MainActivity extends Activity {

	private TwitterSource twitter;
	private TweetDbSource saved;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_twitter:
			//TODO
			return true;
		case R.id.menu_saved:
			//TODO
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/*
	 * Present the user with a simple dialog to enter search string
	 */
	private void acquireSearchString(final String currentSearch) {
		final EditText input = new EditText(this);
		input.setText(currentSearch);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Search Twitter")
			 .setMessage("Enter search")
			 .setView(input)
			 .setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {
					//TODO
				}
			 })
			 .setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {} // cancel
			 })
			 .show();
	}

	/*
	 * Present the user with a simple dialog to confirm a tweet deletion
	 */
	private void confirmDeleteTweet(final Tweet t) {
	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Delete tweet?")
			 .setMessage(t.getTweet())
			 .setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {
					//TODO
				}
			 })
			 .setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {} // cancel
			 })
			 .show();
	}
	
}
