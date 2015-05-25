package com.commonapi;

/**
 * @author LiJian
 */
public class ConstantValues {
	public class InstructionCode {
		public static final int SUCCESS = 0xff00;
		public static final int ERROR_NETWORK = 0xffff;
		public static final int ERROR_PASSWOR_DIFFERENT = 0xff01;
		public static final int ERROR_PASSWOR_WRONG = 0xff02;
		public static final int ERROR_CAPTCHA = 0xff03;
		public static final int ERROR_REG_LOGINACCOUNT_OCCUPY = 0xff04;

		public static final int MESSAGE_TYPE_TEXT = 0x00aa;
		public static final int MESSAGE_TYPE_AUDIO = 0x00bb;
		public static final int MESSAGE_TYPE_IMAGE = 0x00cc;

		public static final int GAME_TYPE_EIGHTPUZZLE = 0x01;
		public static final int GAME_TYPE_QUIZ = 0x02;
		public static final int GAME_TYPE_WHAC = 0x03;

		public static final int GAME_NOT_SET = 0x0023;
		public static final int GAME_HAS_SET = 0x0024;

		public static final int DIRECTION_UP = 0x00;
		public static final int DIRECTION_DOWN = 0x00;
		public static final int DIRECTION_LEFT = 0x00;
		public static final int DIRECTION_RIGHT = 0x00;
		
		//Tp
		public static final String POST_TYPE_TEXT = "1";
		public static final String POST_TYPE_IMAGE = "2";
		
		//LJ
		public static final int STATISTICS_FRIENDS_NUM_TYPE = 0;
		public static final int STATISTICS_SHAKE_NUM_TYPE = 1;
		public static final int STATISTICS_GAME_CHALLENGED_SUCCESS_TYPE = 2;
		public static final int STATISTICS_GAME_CHALLENGED_FAIL_TYPE = 3;
		public static final int STATISTICS_GAME_CHALLENG_SUCCESS_TYPE = 4;
		public static final int STATISTICS_GAME_CHALLENG_FAIL_TYPE = 5;
		public static final int STATISTICS_VOICE_NUM_TYPE = 6;
	}

	public class Configs 
	{
		public static final int INDEX_LOGINACCOUNT = 0;
		public static final int INDEX_PASSWORD = 1;

		public static final String MESSAGE_TEXT_HEAD = "0x213";
		public static final String MESSAGE_AUDIO_HEAD = "0x213";
		public static final String MESSAGE_IMAGE_HEAD = "0x213";
		
		public static final String TORNADO_SERVER_IP = "101.200.230.173";
	//	public static final String TORNADO_SERVER_IP = "172.18.7.189";
		public static final String TORNADO_SERVER_PORT = "8887";
		
		public static final String DATABASE_SERVER_IP = "101.200.230.173";
	//	public static final String DATABASE_SERVER_IP = "127.0.0.1";
		public static final String DATABASE_SERVER_PORT = "1433";
	}
}