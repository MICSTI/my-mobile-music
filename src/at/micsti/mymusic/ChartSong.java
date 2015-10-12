package at.micsti.mymusic;

public class ChartSong {
	
	private static int RANK_DIFF_NEW = 99999;
	
	private int rank;
	private int songId;
	private String songName;
	private String artistName;
	private String recordName;
	private int playedCount;
	private int rankDiff;
	
	public ChartSong() {
		
	}
	
	private void setRankDiffNew() {
		this.rankDiff = RANK_DIFF_NEW;
	}
	
	public void setRankDiff(String diff) {
		if (diff.equals("NEW"))
			this.setRankDiffNew();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public int getPlayedCount() {
		return playedCount;
	}

	public void setPlayedCount(int playedCount) {
		this.playedCount = playedCount;
	}

	public int getRankDiff() {
		return rankDiff;
	}

	public void setRankDiff(int rankDiff) {
		this.rankDiff = rankDiff;
	}
}
