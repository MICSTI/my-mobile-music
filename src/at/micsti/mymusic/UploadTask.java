package at.micsti.mymusic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;

public class UploadTask extends AsyncTask<String, Integer, String> {
	
	MyMusicDatabaseHelper helper;
	
	OnUploadCompleted listener;
	
	String upload_server;
	
	String mobile_file;
	
	public UploadTask(String server, String fileString, OnUploadCompleted listener) {
		this.listener = listener;
		this.upload_server = server;
		this.mobile_file = fileString;
	}

	@Override
	protected String doInBackground(String... params) {
		// Call URL
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(upload_server);
		
		try {
			// Add data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mobile_played", mobile_file));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Execute request
			HttpResponse response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Done!";
	}
	
	protected void onPostExecute(String s) {
		listener.onUploadCompleted(s);
	}
	
	public interface OnUploadCompleted {
		void onUploadCompleted(String s);
	}

}
