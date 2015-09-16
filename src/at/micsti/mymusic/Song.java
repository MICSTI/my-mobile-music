package at.micsti.mymusic;

import java.io.Serializable;

public class Song implements Serializable {
	
	private int id;
	private String name;
	
	private int aid;
	private String artist;
	
	private int rid;
	private String record;
	
	private int length;
	private int rating;
	
	private int discno;
	private int trackno;
	
	public Song () {
		this.initSong();
	}
	
	public Song (int sid) {
		this();
	
		this.setId(sid);
	}
	
	private void initSong () {
		this.id = 0;
		this.name = null;
		this.aid = 0;
		this.artist = null;
		this.rid = 0;
		this.record = null;
		this.length = 0;
		this.rating = 0;
		this.discno = 0;
		this.trackno = 0;
	}
	
	public int getId () {
		return this.id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public int getArtistId () {
		return this.aid;
	}
	
	public void setArtistId (int aid) {
		this.aid = aid;
	}
	
	public int getRecordId () {
		return this.rid;
	}
	
	public void setRecordId (int rid) {
		this.rid = rid;
	}
	
	public String getArtistName () {
		return this.artist;
	}
	
	public void setArtistName (String artist) {
		this.artist = artist;
	}
	
	public String getRecordName () {
		return this.record;
	}
	
	public void setRecordName (String record) {
		this.record = record;
	}
	
	public int getLength () {
		return this.length;
	}
	
	public void setLength (int length) {
		this.length = length;
	}
	
	public int getRating () {
		return this.rating;
	}
	
	public void setRating (int rating) {
		this.rating = rating;
	}
	
	public int getDiscNo () {
		return this.discno;
	}
	
	public void setDiscNo (int discno) {
		this.discno = discno;
	}
	
	public int getTrackNo () {
		return this.trackno;
	}
	
	public void setTrackNo (int trackno) {
		this.trackno = trackno;
	}
}
