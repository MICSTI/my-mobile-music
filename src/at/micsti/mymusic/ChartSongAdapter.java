package at.micsti.mymusic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChartSongAdapter extends ArrayAdapter<ChartSong> {
	
	public ChartSongAdapter(Context context, ArrayList<ChartSong> songs) {
		super(context, R.layout.item_chart_song, songs);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		ChartSong chartSong = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chart_song, parent, false);
        }
        
        // Lookup view for data population
        TextView songRank = (TextView) convertView.findViewById(R.id.songRank);
        TextView songDiff = (TextView) convertView.findViewById(R.id.songDiff);
        TextView songName = (TextView) convertView.findViewById(R.id.songName);
        TextView songArtist = (TextView) convertView.findViewById(R.id.songArtist);
        TextView songRecord = (TextView) convertView.findViewById(R.id.songRecord);
        TextView songPlayCount = (TextView) convertView.findViewById(R.id.songPlayCount);
        
        // Populate the data into the template view using the data object
        songRank.setText(String.valueOf(chartSong.getRank()));
        songDiff.setText(String.valueOf(chartSong.getRankDiff()));
        songName.setText(chartSong.getSongName());
        songArtist.setText(chartSong.getArtistName());
        songRecord.setText(chartSong.getRecordName());
        songPlayCount.setText(String.valueOf(chartSong.getPlayedCount()));
        
        // Return the completed view to render on screen
        return convertView;
	}
}
