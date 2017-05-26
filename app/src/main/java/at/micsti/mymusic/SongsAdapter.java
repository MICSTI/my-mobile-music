package at.micsti.mymusic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SongsAdapter extends ArrayAdapter<Song> {
	
	public SongsAdapter(Context context, ArrayList<Song> songs) {
		super(context, R.layout.item_song, songs);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Song song = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
        }
        
        // Lookup view for data population
        TextView songName = (TextView) convertView.findViewById(R.id.songName);
        TextView songArtist = (TextView) convertView.findViewById(R.id.songArtist);
        TextView songRecord = (TextView) convertView.findViewById(R.id.songRecord);
        
        // Populate the data into the template view using the data object
        songName.setText( song.getName() );
        songArtist.setText( song.getArtistName() );
        songRecord.setText( song.getRecordName() );
        
        // Return the completed view to render on screen
        return convertView;
	}
}
