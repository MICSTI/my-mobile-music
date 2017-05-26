package at.micsti.mymusic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import at.micsti.mymusic.DownloadTask.OnTaskCompleted;
import at.micsti.mymusic.FetchUrlContentTask.OnFetchCompleted;
import at.micsti.mymusic.UploadTask.OnUploadCompleted;

public class Update extends Activity implements OnTaskCompleted, OnUploadCompleted {
	
	private static final String BACKEND_API = "https://mymusic.micsti.at/api.php?key=mobile_db_mod";
	
	private static final String UPLOAD_SERVER = "https://mymusic.micsti.at/upload/upload_mobile.php";
	
	private static String KEY_MOBILE_DB_LAST_MODIFIED = "mobile_db_last_modified";
	
	private String update_message = "";
	
	private MyMusicDatabaseHelper myDbHelper;
	
	private SharedPreferences default_values;
	
	private long local_last_modified;
	
	private long server_last_modified; 
	
	private TextView status;
	private TextView local_db_mod;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		// Database helper
		this.myDbHelper = MyMusicDatabaseHelper.getHelper(this);
		
		// Shared preferences for saving database file modification file
		this.default_values = PreferenceManager.getDefaultSharedPreferences(this);
		
		local_last_modified = default_values.getLong(KEY_MOBILE_DB_LAST_MODIFIED, 0);
		
		// Local database modification time
		local_db_mod = (TextView) findViewById(R.id.local_database_status);
		local_db_mod.setText("Last database update: " + this.getFormattedTime(local_last_modified));
		
		// Update button
		final Button updateButton = (Button) findViewById(R.id.button_perform_update);
		updateButton.setEnabled(false);
		
		// Status text view
		status = (TextView) findViewById(R.id.message);
		
		// Check network connection
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
			updateButton.setEnabled(true);
			updateButton.setClickable(true);
		}
		
		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform update
				
				// Create mobile played string
				if (myDbHelper.playedEntryExists()) {
					String mobile_file = myDbHelper.getMobilePlayedXml();
					
					new UploadTask(UPLOAD_SERVER, mobile_file, Update.this).execute("DO IT");
				} else {
					addUpdateMessage("no new entries to upload!");
					status.setText(update_message);
					
					checkIfNewerDatabaseExists();
				}
			}
		} );
	}
	
	private void checkIfNewerDatabaseExists() {
		server_last_modified = -1;
		
		new FetchUrlContentTask(BACKEND_API, new OnFetchCompleted() {

			@Override
			public void onFetchCompleted(String s) {
				try {
					Log.d("Update", "got server last modified time: " + s);

					server_last_modified = Long.parseLong(s.trim());
					
					if (server_last_modified > local_last_modified) {
						myDbHelper.closeConnection();
						
						new DownloadTask(myDbHelper, Update.this).execute("DO IT");
					} else {
						addUpdateMessage("Database up to date");
						status.setText(update_message);
					}
				} catch (Exception e) {
					Log.e("Update", e.toString());
					addUpdateMessage("Failed to get database modified info");
					status.setText(update_message);
				}
			}
			
		}).execute("DO IT");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_update, menu);
		return true;
	}
	
	private void addUpdateMessage (String msg) {
		this.update_message += "\n" + msg;
	}
	
	private static String getFileContents (String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
	
	private String getFormattedTime (long unix) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(unix * 1000);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		String date = String.format("%02d.%02d.%d", day, month, year);
		String time = String.format("%02d:%02d", hour, minute);
		
		return date + " " + time;
	}

	@Override
	public void onTaskCompleted(String s) {
		myDbHelper.openDatabase();
		
		// Save new current timestamp of database
		SharedPreferences.Editor editor = default_values.edit();
		editor.putLong(KEY_MOBILE_DB_LAST_MODIFIED, server_last_modified);
		editor.commit();
		
		addUpdateMessage("Download successful!");
		status.setText(update_message);
		
		local_last_modified = default_values.getLong(KEY_MOBILE_DB_LAST_MODIFIED, 0);
		local_db_mod.setText("Last database update: " + getFormattedTime(local_last_modified));
	}

	@Override
	public void onUploadCompleted(String s) {
		// Delete played entries
		myDbHelper.deletePlayed();
		
		addUpdateMessage("uploaded mobile played file");
		
		checkIfNewerDatabaseExists();
	}
}
