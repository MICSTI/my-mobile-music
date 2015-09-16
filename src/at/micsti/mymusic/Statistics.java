package at.micsti.mymusic;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class Statistics extends Activity {
	
	private ListView played_history;
	
	private PlayedAdapter adapter;
	
	private MyMusicDatabaseHelper myDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		// Database helper
		this.myDBHelper = MyMusicDatabaseHelper.getHelper(this);
		
		// Locate list view
		this.played_history = (ListView) findViewById(R.id.played_history);
		
		// Get playeds
		ArrayList<Played> playeds = myDBHelper.getPlayedEntries();
		
		// Create array adapter
	    this.adapter = new PlayedAdapter(this, playeds);
	    
	    // Set adapter
	    this.played_history.setAdapter(this.adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_statistics, menu);
		return true;
	}

}
