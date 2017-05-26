package at.micsti.mymusic;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;

public class Favourites extends Activity {
	
	private ListView chartsTop2020Songs;
	private ListView chartsTop2020Artists;
	private ListView chartsAllTimeSongs;
	private ListView chartsAllTimeArtists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		
		// get references to list view
		chartsTop2020Songs = (ListView) findViewById(R.id.charts_top2020_songs);
		chartsTop2020Artists = (ListView) findViewById(R.id.charts_top2020_artists);
		chartsAllTimeSongs = (ListView) findViewById(R.id.charts_alltime_songs);
		chartsAllTimeArtists = (ListView) findViewById(R.id.charts_alltime_artists);
		
		// onclick handlers for tabs
		TextView tabTop2020 = (TextView) findViewById(R.id.tab_top2020);
		
		tabTop2020.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		} );
		
		TextView tabAllTime = (TextView) findViewById(R.id.tab_alltime);
		
		tabAllTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		} );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_favourites, menu);
		return true;
	}
	
	public void setAdapters() {
		// get charts
		ArrayList<ChartSong> top2020Songs = new ArrayList<ChartSong>();
		ArrayList<ChartArtist> top2020Artists = new ArrayList<ChartArtist>();
		
		ArrayList<ChartSong> allTimeSongs = new ArrayList<ChartSong>();
		ArrayList<ChartArtist> allTimeArtists = new ArrayList<ChartArtist>();
		
		// create adapters
		ChartSongAdapter top2020SongCharts = new ChartSongAdapter(this, top2020Songs);
		ChartArtistAdapter top2020ArtistCharts = new ChartArtistAdapter(this, top2020Artists);
		
		ChartSongAdapter allTimeSongCharts = new ChartSongAdapter(this, allTimeSongs);
		ChartArtistAdapter allTimeArtistCharts = new ChartArtistAdapter(this, allTimeArtists);
		
		// set adapters
		chartsTop2020Songs.setAdapter(top2020SongCharts);
		chartsTop2020Artists.setAdapter(top2020ArtistCharts);
		
		chartsAllTimeSongs.setAdapter(allTimeSongCharts);
		chartsAllTimeArtists.setAdapter(allTimeArtistCharts);
	}

}
