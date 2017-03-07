package tictactoe.json;

public class MatchJson {
	
	private boolean isMatchOk;
	private int roomId;
	private String roomPassword;

	public boolean getIsMatchOk() {
		return isMatchOk;
	}
	public void setMatchOk(boolean isMatchOk) {
		this.isMatchOk = isMatchOk;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getRoomPassword() {
		return roomPassword;
	}
	public void setRoomPassword(String roomPassword) {
		this.roomPassword = roomPassword;
	}
	
}
