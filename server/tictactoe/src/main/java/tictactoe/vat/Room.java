package tictactoe.vat;

public class Room {
	
	private int roomId;
	private String roomPassword;
	
	private boolean isFull;  //房间是否满了
	private boolean waitPawn;  //等待什么棋子进入房间 false:x, true:o
	
	private long lastActivityTime;
	
//	private int userIdA;  //x棋
//	private int userIdB;  //o棋
	private User userA;
	private User userB;
	
	private boolean isUserAReady;
	private boolean isUserBReady;
	
	private boolean whoCanPlay;  //false:x ,true:o
	private int step = 0;
	
	private int[][] chessBoard = new int[3][3];  //default:0 , X:1 , O:2

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

	public boolean getIsFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public boolean getWaitPawn() {
		return waitPawn;
	}

	public void setWaitPawn(boolean waitPawn) {
		this.waitPawn = waitPawn;
	}

	public long getLastActivityTime() {
		return lastActivityTime;
	}

	public void setLastActivityTime(long lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}
	
	public User getUserA() {
		return userA;
	}

	public void setUserA(User userA) {
		this.userA = userA;
	}

	public User getUserB() {
		return userB;
	}

	public void setUserB(User userB) {
		this.userB = userB;
	}
	
	public boolean getIsUserAReady() {
		return isUserAReady;
	}
	
	public void setUserAReady(boolean isUserAReady) {
		this.isUserAReady = isUserAReady;
	}

	public boolean getIsUserBReady() {
		return isUserBReady;
	}

	public void setUserBReady(boolean isUserBReady) {
		this.isUserBReady = isUserBReady;
	}

	public boolean getWhoCanPlay() {
		return whoCanPlay;
	}

	public void setWhoCanPlay(boolean whoCanPlay) {
		this.whoCanPlay = whoCanPlay;
	}
	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
	public void setChessBoard(int[][] chessBoard) {
		this.chessBoard = chessBoard;
	}

	public int[][] getChessBoard() {
		return chessBoard;
	}

}
