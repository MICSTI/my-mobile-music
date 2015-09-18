package at.micsti.mymusic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Update extends Activity {
	
	private static final String BACKEND_API = "http://mymusic.micsti.at/api.php?key=mobile_db_mod";
	
	private static final String UPLOAD_SERVER = "http://mymusic.micsti.at/upload/upload_mobile.php";
	
	private static String DB_SERVER_FILE = "http://mymusic.micsti.at/files/myMobileMusic.DB";
	
	private static String KEY_MOBILE_DB_LAST_MODIFIED = "mobile_db_last_modified";
	
	private String update_message = "";
	
	private MyMusicDatabaseHelper myDbHelper;
	
	private SharedPreferences default_values;
	
	private long local_last_modified;
	
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
		final TextView local_db_mod = (TextView) findViewById(R.id.local_database_status);
		local_db_mod.setText("Last database update: " + this.getFormattedTime(local_last_modified));
		
		// Update button
		final Button updateButton = (Button) findViewById(R.id.button_perform_update);
		updateButton.setEnabled(false);
		
		// Status text view
		final TextView status = (TextView) findViewById(R.id.message);
		
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
					
					// Call URL
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(UPLOAD_SERVER);
					
					try {
						// Add data
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("mobile_played", mobile_file));
						
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						
						// Execute request
						HttpResponse response = httpclient.execute(httppost);
						
						// Delete played entries
						myDbHelper.deletePlayed();
						
						addUpdateMessage("uploaded mobile played file");
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					addUpdateMessage("no new entries to upload!");
				}
				
				status.setText(update_message);
				
				// Check if newer mobile database file is available
				File mobile_db_file = new File(DB_SERVER_FILE);
				
				long server_last_modified = -1;
				try {
					server_last_modified = Long.parseLong(getFileContents(BACKEND_API));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				if (server_last_modified > local_last_modified) {
					myDbHelper.closeConnection();
					myDbHelper.downloadDatabase();
					myDbHelper.openDatabase();
					
					// Save new current timestamp of database
					SharedPreferences.Editor editor = default_values.edit();
					editor.putLong(KEY_MOBILE_DB_LAST_MODIFIED, server_last_modified);
					editor.commit();
					
					addUpdateMessage("Download successful!");
					status.setText(update_message);
					
					local_last_modified = default_values.getLong(KEY_MOBILE_DB_LAST_MODIFIED, 0);
					local_db_mod.setText("Last database updat: " + getFormattedTime(local_last_modified));
				}
			}
		} );
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
}
