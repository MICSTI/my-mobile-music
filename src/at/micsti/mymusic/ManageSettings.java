package at.micsti.mymusic;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ManageSettings extends Activity {
	
	private SharedPreferences default_values;
	
	private final static String KEY_DEFAULT_DEVICE = "default_device";
	
	private MyMusicDatabaseHelper myDbHelper;
	
	private ArrayAdapter<SpinnerDeviceObject> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_settings);
		
		// Shared preferences for saving default values
		this.default_values = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Database helper for getting all devices
		this.myDbHelper = MyMusicDatabaseHelper.getHelper(this);
		
		// Default device
			// Stored default device
			int device_id = this.default_values.getInt(KEY_DEFAULT_DEVICE, 0);
		
			// Spinner
			final Spinner default_device = (Spinner) findViewById(R.id.default_device);
			
			// Get all devices
			List<SpinnerDeviceObject> allDevices = this.myDbHelper.getAllDevices();
			
			// Create array adapter
			this.adapter = new ArrayAdapter<SpinnerDeviceObject>(this, android.R.layout.simple_spinner_item, allDevices);
			
			this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			default_device.setAdapter(this.adapter);
			
			if (device_id > 0) {
				for (int pos = (this.adapter.getCount() - 1); pos >= 0; pos--) {
					SpinnerDeviceObject device = adapter.getItem(pos);
					
					if (device.getDeviceId() == device_id) {
						default_device.setSelection(pos);
						break;
					}
				}
			}
			
		// Save button
		Button saveButton = (Button) findViewById(R.id.button_save_settings);
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SpinnerDeviceObject selected_device = (SpinnerDeviceObject) default_device.getSelectedItem();
				
				saveSettings(v, selected_device.getDeviceId());
				
				// Close activity
				finish();
			}
		} );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_manage_settings, menu);
		return true;
	}
	
	public void saveSettings (View view, int device_id) {
		SharedPreferences.Editor editor = this.default_values.edit();
		editor.putInt(KEY_DEFAULT_DEVICE, device_id);
		editor.commit();
	}

}
