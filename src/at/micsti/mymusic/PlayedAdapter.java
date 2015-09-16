package at.micsti.mymusic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayedAdapter extends ArrayAdapter<Played> {
	
	public PlayedAdapter(Context context, ArrayList<Played> playeds) {
		super(context, R.layout.item_played, playeds);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Played played = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_played, parent, false);
        }
        
        // Lookup view for data population
        TextView playedTitle = (TextView) convertView.findViewById(R.id.playedTitle);
        TextView playedRecord = (TextView) convertView.findViewById(R.id.playedRecord);
        TextView playedTime = (TextView) convertView.findViewById(R.id.playedTime);
        
        // Populate the data into the template view using the data object
        playedTitle.setText( played.getSong().getArtistName() + " - " + played.getSong().getName() );
        playedRecord.setText( played.getSong().getRecordName() );
        
        playedTime.setText( played.getFormattedDateTime() );
        
        // Return the completed view to render on screen
        return convertView;
	}
}
