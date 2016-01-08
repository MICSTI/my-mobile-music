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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

public class FetchUrlContentTask extends AsyncTask<String, Integer, String> {
	
	OnFetchCompleted listener;
	
	String url;
	
	public FetchUrlContentTask(String url, OnFetchCompleted listener) {
		this.listener = listener;
		this.url = url;
	}

	@Override
	protected String doInBackground(String... params) {
		// Call URL
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		
		try {
			// Execute request
			HttpResponse response = httpclient.execute(httpget);
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "DONE!";
	}
	
	protected void onPostExecute(String s) {
		listener.onFetchCompleted(s);
	}
	
	public interface OnFetchCompleted {
		void onFetchCompleted(String s);
	}

}
