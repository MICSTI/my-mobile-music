package at.micsti.mymusic;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private MyMusicDatabaseHelper myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Add played song menu
		TextView tvId = (TextView) findViewById(R.id.add_played_song);
		
		tvId.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addPlayedSong(v);
			}
		} );
		
		// Statistics menu
		TextView tvStatisticsId = (TextView) findViewById(R.id.statistics);
		
		tvStatisticsId.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showStatistics(v);
			}
		} );
		
		// Favourites menu
		TextView tvFavouritesId = (TextView) findViewById(R.id.favourites);
		
		tvFavouritesId.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showFavourites(v);
			}
		} );
		
		// Settings menu
		TextView tvSettingsId = (TextView) findViewById(R.id.settings);
		
		tvSettingsId.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				manageSettings(v);
			}
		} );
		
		// Update menu
		TextView tvUpdateId = (TextView) findViewById(R.id.update);
		
		tvUpdateId.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateDatabase(v);
			}
		} );
		
		// Database
		this.myDbHelper = MyMusicDatabaseHelper.getHelper(this);
		
		this.myDbHelper.checkLocalDatabase();
		
		/*try {
			this.myDbHelper.copyDatabaseFromAssetsFolder();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		this.myDbHelper.openDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void addPlayedSong(View view) {
		Intent myIntent = new Intent(MainActivity.this, AddPlayedSong.class);
		MainActivity.this.startActivity(myIntent);
	}
	
	public void showStatistics(View view) {
		Intent statisticsIntent = new Intent(MainActivity.this, Statistics.class);
		MainActivity.this.startActivity(statisticsIntent);
	}
	
	public void showFavourites(View view) {
		Intent favouritesIntent = new Intent(MainActivity.this, Favourites.class);
		MainActivity.this.startActivity(favouritesIntent);
	}
	
	public void updateDatabase(View view) {
		Intent updateIntent = new Intent(MainActivity.this, Update.class);
		MainActivity.this.startActivity(updateIntent);
	}
	
	public void manageSettings(View view) {
		Intent settingsIntent = new Intent(MainActivity.this, ManageSettings.class);
		MainActivity.this.startActivity(settingsIntent);
	}

}
