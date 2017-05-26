package at.micsti.mymusic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class FetchUrlContentTask extends AsyncTask<String, Integer, String> {

    OnFetchCompleted listener;

    String urlStr;

    public FetchUrlContentTask(String url, OnFetchCompleted listener) {
        this.listener = listener;
        this.urlStr = url;
    }

    @Override
    protected String doInBackground(String... params) {
        // Call URL
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(this.urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();

                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
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
