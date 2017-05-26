package at.micsti.mymusic;

import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<String, Integer, String> {
	
	MyMusicDatabaseHelper helper;
	
	OnTaskCompleted listener;
	
	public DownloadTask(MyMusicDatabaseHelper helper, OnTaskCompleted listener) {
		this.helper = helper;
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			helper.downloadDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return "Done!";
	}
	
	protected void onPostExecute(String s) {
		listener.onTaskCompleted(s);
	}
	
	public interface OnTaskCompleted {
		void onTaskCompleted(String s);
	}

}
