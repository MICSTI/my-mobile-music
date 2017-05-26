package at.micsti.mymusic;

public class ChartArtist {
	
	private static int RANK_DIFF_NEW = 99999;
	
	private int rank;
	private int artistId;
	private String artistName;
	private int playedCount;
	private int rankDiff;
	
	public ChartArtist() {
		
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
	
	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
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
