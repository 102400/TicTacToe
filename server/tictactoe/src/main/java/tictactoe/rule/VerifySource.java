package tictactoe.rule;

import tictactoe.util.MD5;

public class VerifySource {
	
	private static final String LEFT = "#vc";
	private static final String RIGHT = "@*!6^xs";
	
	private static String f(String userId) {
		return LEFT + userId + RIGHT;
	}
	
	public static String get(String userId) {
		return f(userId);
	}
	
	public static String getMD5(String userId) {
		return MD5.code(f(userId));
	}
	
	public static String getMD5(int userId) {
		return getMD5(userId + "");
	}

}
