package at.micsti.mymusic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Played {
	private Song song;
	private int devid;
	private long timestamp;
	
	public Played () {
		
	}
	
	public Song getSong () {
		return this.song;
	}
	
	public void setSong (Song song) {
		this.song = song;
	}
	
	public int getDevid () {
		return this.devid;
	}
	
	public void setDevid (int devid) {
		this.devid = devid;
	}
	
	public long getTimestamp () {
		return this.timestamp;
	}
	
	public void setTimestamp (long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getFormattedDateTime () {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(this.timestamp * 1000);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		String date = String.format("%02d.%02d.%d", day, month, year);
		String time = String.format("%02d:%02d", hour, minute);
		
		return date + " " + time;
	}
}
