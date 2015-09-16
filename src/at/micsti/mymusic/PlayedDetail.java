package at.micsti.mymusic;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.DatePicker; // Required for date picker
import android.widget.TimePicker; // Required for time picker
import android.widget.Toast; // Required for pop up notification message.


public class PlayedDetail extends Activity {
	
	private Song song;
	
	private DatePicker datepicker; // object for datepicker
    private int year , month , day;  // declaring variables for year, month and day
    private TimePicker timepicker; // object for timepicker
    
    private MyMusicDatabaseHelper dbHelper;
    
    private SharedPreferences default_values;
    
    private final static String KEY_DEFAULT_DEVICE = "default_device";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_played_detail);
		
		// Init database helper
		this.dbHelper = MyMusicDatabaseHelper.getHelper(this);
		
		// Shared preferences for saving default values
		this.default_values = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Init date and time picker
		datepicker = (DatePicker) findViewById(R.id.datePicker); // Obtain datepicker attributes from layout
		timepicker = (TimePicker) findViewById(R.id.timePicker); //Obtain timepicker attributes from layout
        timepicker.setIs24HourView(true);    //setting timepicker to 24 hr clock view

		this.song = (Song) this.getIntent().getSerializableExtra("song");
		
		TextView songTitle = (TextView) findViewById(R.id.detailSongTitle);
		TextView songRecord = (TextView) findViewById(R.id.detailSongRecord);
		
		songTitle.setText(this.song.getArtistName() + " - " + this.song.getName());
		songRecord.setText(this.song.getRecordName());
		
		// Checkbox handling (act like radio button)
		final CheckBox chkStart = (CheckBox) findViewById(R.id.chkStart);
		final CheckBox chkEnd = (CheckBox) findViewById(R.id.chkEnd);
		
		chkStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				chkStart.setChecked(true);
				chkEnd.setChecked(false);
			}
		} );
		
		chkEnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				chkStart.setChecked(false);
				chkEnd.setChecked(true);
			}
		} );
		
		// Save button
		final Button btnSave = (Button) findViewById(R.id.btnSave);
		
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Insert into played table
				long timestamp;
				
				Calendar cal = new GregorianCalendar();
				cal.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(), timepicker.getCurrentHour(), timepicker.getCurrentMinute(), 0);
				
				timestamp = cal.getTimeInMillis() / 1000;
				
				// If song start was selected, play duration is added
				long song_duration = chkStart.isChecked() ? (long) (song.getLength() / 1000) : (long) 0;
				timestamp += song_duration;
				
				int devid = default_values.getInt(KEY_DEFAULT_DEVICE, 0);
				
				boolean success = dbHelper.addPlayedEntry(song, timestamp, devid);
				
				// Show toast
				Context context = getApplicationContext();
				CharSequence textSuccess = "Saved played song successfully";
				CharSequence textError = "Couldn't save played song";
				int duration = Toast.LENGTH_SHORT;
				
				CharSequence text = success ? textSuccess : textError;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
				finish();
			}
		} );
		
		// Cancel button
		final Button btnCancel = (Button) findViewById(R.id.btnCancel);
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		} );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_played_detail, menu);
		return true;
	}
}
