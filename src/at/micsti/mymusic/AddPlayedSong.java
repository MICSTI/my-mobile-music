package at.micsti.mymusic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AddPlayedSong extends Activity {
	
	public MyMusicDatabaseHelper myDBHelper;
	
	private EditText text_search;
	
	private ListView result_list;
	
	private TextView search_message;
	
	private SongsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_played_song);
		
		// Locate text view
		this.text_search = (EditText) findViewById(R.id.search_text);
		
		// Locate list view
		this.result_list = (ListView) findViewById(R.id.result_list);
		
		this.result_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				
				Song song = (Song) parent.getItemAtPosition(position);
				
				setPlayedDetail(song);
			}
		});
		
		// Locate search result message
		this.search_message = (TextView) findViewById(R.id.search_message);
		
		// Init database
		this.myDBHelper = MyMusicDatabaseHelper.getHelper(this);
		
		this.text_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				String query = s.toString();
				
				if (query.length() >= 4) {
					// Get query result
					ArrayList<Song> songs = myDBHelper.searchSongs(s.toString());
					
					if (songs.size() > 0) {
						// Create array adapter
					    adapter = new SongsAdapter(AddPlayedSong.this, songs);
					      
					    result_list.setAdapter(adapter);
					    
					    AddPlayedSong.this.result_list.setVisibility(View.VISIBLE);
					    AddPlayedSong.this.search_message.setVisibility(View.GONE);
					} else {
						AddPlayedSong.this.result_list.setVisibility(View.GONE);
						AddPlayedSong.this.search_message.setText(R.string.no_records);
						AddPlayedSong.this.search_message.setVisibility(View.VISIBLE);
					}
				} else {
					AddPlayedSong.this.result_list.setVisibility(View.GONE);
					AddPlayedSong.this.search_message.setText(R.string.not_enough_characters);
					AddPlayedSong.this.search_message.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_played_song, menu);
		return true;
	}
	
	public void setPlayedDetail (Song song) {
		Intent playedDetailIntent = new Intent(AddPlayedSong.this, PlayedDetail.class);
		playedDetailIntent.putExtra("song", song);
		AddPlayedSong.this.startActivity(playedDetailIntent);
	}
}
