package at.micsti.mymusic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChartArtistAdapter extends ArrayAdapter<ChartArtist> {
	
	public ChartArtistAdapter(Context context, ArrayList<ChartArtist> artists) {
		super(context, R.layout.item_chart_artist, artists);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		ChartArtist chartArtist = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chart_artist, parent, false);
        }
        
        // Lookup view for data population
        TextView artistRank = (TextView) convertView.findViewById(R.id.artistRank);
        TextView artistDiff = (TextView) convertView.findViewById(R.id.artistDiff);
        TextView artistName = (TextView) convertView.findViewById(R.id.artistName);
        TextView artistPlayCount = (TextView) convertView.findViewById(R.id.artistPlayCount);
        
        // Populate the data into the template view using the data object
        artistRank.setText(String.valueOf(chartArtist.getRank()));
        artistDiff.setText(String.valueOf(chartArtist.getRankDiff()));
        artistName.setText(chartArtist.getArtistName());
        artistPlayCount.setText(String.valueOf(chartArtist.getPlayedCount()));
        
        // Return the completed view to render on screen
        return convertView;
	}
}
