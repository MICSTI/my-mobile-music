package at.micsti.mymusic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class MyMusicDatabaseHelper extends SQLiteOpenHelper {
	
	private static MyMusicDatabaseHelper instance;
		
	private static String DB_PATH = "/data/data/at.micsti.mymusic/databases/";
	
	private static String ROOT_DIR = "";
	
	private static String DB_NAME = "myMobileMusic.DB";
	
	private SQLiteDatabase myMusic;
	
	private final Context myContext;
	
	private static final String DATABASE_UPDATE_PATH = "http://mymusic.micsti.at/files/myMobileMusic.DB";
	
	// Songs view
	private static final String VIEW_SONGS = "SongsView";
	
	// Table names
	private static final String TABLE_SONGS = "songs";
	private static final String TABLE_ARTISTS = "artists";
	private static final String TABLE_PLAYED = "played";
	
	// Table columns names
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	
	private static final String KEY_SID = "sid";
	private static final String KEY_TIMESTAMP = "timestamp";
	private static final String KEY_DEVID = "devid";
	
	private static final String KEY_SONG_ID = "SongId";
	private static final String KEY_SONG_NAME = "SongName";
	private static final String KEY_ARTIST_NAME = "ArtistName";
	private static final String KEY_RECORD_NAME = "RecordName";
	private static final String KEY_SONG_LENGTH = "SongLength";
	private static final String KEY_SONG_RATING = "SongRating";
	
	// Table column arrays
	private static final String[] VIEW_SONGS_COLUMNS = {KEY_SONG_ID, KEY_SONG_NAME, KEY_ARTIST_NAME, KEY_RECORD_NAME, KEY_SONG_LENGTH, KEY_SONG_RATING};
	private static final String[] ARTISTS_COLUMNS = {KEY_ID, KEY_NAME};
	private static final String[] SONGS_COLUMNS = {KEY_ID, KEY_NAME};
	
	public static synchronized MyMusicDatabaseHelper getHelper(Context context) {
		if (instance == null)
			instance = new MyMusicDatabaseHelper(context);
		
		return instance;
	}
	
	public MyMusicDatabaseHelper (Context context) {
		super(context, DB_NAME, null, 1);
		
		//if (android.os.Build.VERSION.SDK_INT >= 17) {
	    //   DB_PATH = context.getApplicationInfo().dataDir + "/databases/";         
	    //}
	    //else
	    //{
	    //   DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	    //}
		
		//DB_PATH = "/data/" + context.getPackageName() + "/databases/";
		
		//DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		
		//ROOT_DIR = Environment.getExternalStorageDirectory().toString();
		
		this.myContext = context;
	}
	
	public void checkLocalDatabase () { 
		// does the local database exist?
		boolean dbExists = checkDatabase();
		
		Log.d("checkDb", "Database exists:" + dbExists);
		
		if (!dbExists) {
			//this.copyDatabaseFromAssetsFolder();
			//this.downloadDatabase();
			
			this.getReadableDatabase();
			
			try {
				copyDatabaseFromAssetsFolder();
			} catch (IOException e) {
				Log.d("checkDb", "error copying database");
				e.printStackTrace();
				throw new Error("Error copying database!");
			}
			
			Log.d("checkDb", "DONE!!!");
		}
	}
	
	 /**
	  * Check if the database already exist to avoid re-copying the file each time you open the application.
	  * @return true if it exists, false if it doesn't
	  */
	private boolean checkDatabase () {
		File dbFile = new File(DB_PATH + DB_NAME);
		
		if (dbFile.exists())
			return true;
		
		return false;
	}
	
	public void downloadDatabase () {
		this.downloadFromUrl(DATABASE_UPDATE_PATH, DB_PATH + DB_NAME);
	}
	
	public void closeConnection () {
		this.myMusic.close();
	}
	
	 public void downloadFromUrl(String sourceURL, String fileName) {  //this is the downloader method
         try {
                 URL url = new URL(sourceURL);
                 File file = new File(fileName);

                 long startTime = System.currentTimeMillis();
                 Log.d("ImageManager", "download begining");
                 Log.d("ImageManager", "download url:" + url);
                 Log.d("ImageManager", "downloaded file name:" + fileName);
                 /* Open a connection to that URL. */
                 URLConnection ucon = url.openConnection();

                 /*
                  * Define InputStreams to read from the URLConnection.
                  */
                 InputStream is = ucon.getInputStream();
                 BufferedInputStream bis = new BufferedInputStream(is);

                 /*
                  * Read bytes to the Buffer until there is nothing more to read(-1).
                  */
                 ByteArrayBuffer baf = new ByteArrayBuffer(50);
                 int current = 0;
                 while ((current = bis.read()) != -1) {
                         baf.append((byte) current);
                 }

                 /* Convert the Bytes read to a String. */
                 FileOutputStream fos = new FileOutputStream(file);
                 fos.write(baf.toByteArray());
                 fos.close();
                 Log.d("downloadFromUrl", "download ready in"
                                 + ((System.currentTimeMillis() - startTime) / 1000)
                                 + " sec");

         } catch (IOException e) {
                 Log.d("downloadFromUrl", "Error: " + e);
         }

	 }
	
	 /**
	  * Copies your database from your local assets-folder to the just created empty database in the
	  * system folder, from where it can be accessed and handled.
	  * This is done by transfering bytestream.
	  * */
	public void copyDatabaseFromAssetsFolder () throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		
		File newDbFile = new File(outFileName);
		if (!newDbFile.exists()) {
			newDbFile.createNewFile();
		}
		
		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName, false);
		
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	
	public boolean openDatabase () throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		
		this.myMusic = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		
		Log.d("openDatabase", "Opened Database " + myPath);
		
		return this.myMusic != null;
	}
	
	@Override
	public synchronized void close () {
		if (this.myMusic != null)
			this.myMusic.close();
		
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Song> searchSongs (String text) {
		ArrayList<Song> songs = new ArrayList<Song>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String where = "";
		String[] where_args = { "SongName", "ArtistName", "RecordName" };
		
		for (String wh : where_args) {
			where += wh + " LIKE \"%" + text + "%\" OR ";
		}
		
		String query = "SELECT * FROM SongsView WHERE " + where.substring(0, where.length() - 4);
		
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			do {
				Song song = new Song();
				song.setId( Integer.parseInt(cursor.getString(0)) );
				song.setName( cursor.getString(1) );
				song.setArtistName( cursor.getString(2) );
				song.setRecordName( cursor.getString(3) );
				song.setRating( Integer.parseInt(cursor.getString(4)) );
				song.setLength( Integer.parseInt(cursor.getString(5)) );
				
				try {
					song.setDiscNo( Integer.parseInt(cursor.getString(6)) );
				} catch (Exception e) {
					song.setDiscNo(0);
				}
				
				try {
					song.setTrackNo( Integer.parseInt(cursor.getString(7)) );
				} catch (Exception e) {
					song.setTrackNo(0);
				}
				
				songs.add(song);
			} while (cursor.moveToNext());
		}
		
		return songs;
	}
	
	public Song getSongById (int sid) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT * FROM SongsView WHERE SongId = " + sid;
		
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			Song song = new Song();
			song.setId( Integer.parseInt(cursor.getString(0)) );
			song.setName( cursor.getString(1) );
			song.setArtistName( cursor.getString(2) );
			song.setRecordName( cursor.getString(3) );
			song.setRating( Integer.parseInt(cursor.getString(4)) );
			song.setLength( Integer.parseInt(cursor.getString(5)) );
			
			try {
				song.setDiscNo( Integer.parseInt(cursor.getString(6)) );
			} catch (Exception e) {
				song.setDiscNo(0);
			}
			
			try {
				song.setTrackNo( Integer.parseInt(cursor.getString(7)) );
			} catch (Exception e) {
				song.setTrackNo(0);
			}
			
			return song;
		}
		
		return null;
	}
	
	public List<SpinnerDeviceObject> getAllDevices () {
		List<SpinnerDeviceObject> devices = new ArrayList<SpinnerDeviceObject>();
		
		// Select query
		String selectQuery = "SELECT * FROM devices WHERE active = 1";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// adding all devices to list
		if (cursor.moveToFirst()) {
			do {
				devices.add( new SpinnerDeviceObject( Integer.parseInt(cursor.getString(0)), cursor.getString(1) ) );
			} while (cursor.moveToNext());
		}
		
		return devices;
	}
	
	public void deletePlayed () {
		String query = "DELETE FROM played";
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL(query);
	}
	
	public String getMobilePlayedXml () {
		String mobile_file = "";
		
		// XML header
		mobile_file += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		
		// Parent element
		mobile_file += "<mobile>";
		
		// Select query
		String selectQuery = "SELECT sid, devid, timestamp FROM played";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// add all played elementes
		if (cursor.moveToFirst()) {
			do {
				String sid = cursor.getString(0);
				String devid = cursor.getString(1);
				String timestamp = cursor.getString(2);
				
				mobile_file += "<play>";
					mobile_file += "<sid>" + sid + "</sid>";
					mobile_file += "<devid>" + devid + "</devid>";
					mobile_file += "<timestamp>" + timestamp + "</timestamp>";
				mobile_file += "</play>";
			} while (cursor.moveToNext());
		}
		
		mobile_file += "</mobile>";
		
		return mobile_file;
	}
	
	public boolean playedEntryExists () {
		// Select query
		String selectQuery = "SELECT * FROM played";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.getCount() > 0)
			return true;
		
		return false;
	}
	
	public ArrayList<Played> getPlayedEntries () {
		ArrayList<Played> entries = new ArrayList<Played>();
		
		// Select query
		String selectQuery = "SELECT * FROM played ORDER BY timestamp DESC";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			do {
				Played played = new Played();
				
				played.setSong( this.getSongById( Integer.parseInt(cursor.getString(1)) ) );
				played.setDevid( Integer.parseInt(cursor.getString(2)) );
				played.setTimestamp( Long.parseLong(cursor.getString(3)) );
				
				entries.add(played);
			} while (cursor.moveToNext());
		}
			
		return entries;
	}
	
	public boolean addPlayedEntry (Song song, long timestamp, int devid) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_SID, song.getId());
		values.put(KEY_DEVID, devid);
		values.put(KEY_TIMESTAMP, timestamp);
		
		long id = db.insert(TABLE_PLAYED, null, values);
		db.close();
		
		if (id >= 0) {
			return true;
		} else {
			return false;
		}
	}
}
