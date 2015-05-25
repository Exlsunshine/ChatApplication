package com.yg.commons;

import com.yg.user.ClientUser;

public class ConstantValues
{
	public static ClientUser user = null;
	
	public class InstructionCode
	{
		public static final int SUCCESS = 0xff00;
		public static final int ERROR_NETWORK = 0xffff;
		public static final int ERROR_PASSWOR_DIFFERENT = 0xff01;
		public static final int ERROR_PASSWOR_WRONG = 0xff02;
		public static final int ERROR_CAPTCHA = 0xff03;
		public static final int ERROR_REG_LOGINACCOUNT_OCCUPY = 0xff04;
		
		public static final int MESSAGE_TYPE_TEXT = 0x00aa;
		public static final int MESSAGE_TYPE_AUDIO = 0x00bb;
		public static final int MESSAGE_TYPE_IMAGE = 0x00cc;
		public static final String MESSAGE_IMAGE_FLAG = "___msg_type_img_download_request_id_is_";
		public static final String MESSAGE_AUDIO_FLAG = "___msg_type_audio_download_request_id_is_";
		public static final String MESSAGE_RECEIVEED_FROM_USERID = "received_message_from_userID";
		public static final String MESSAGE_RECEIVEED_BODY = "received_message_body";
		public static final String MESSAGE_RECEIVEED_DATE = "received_message_date";
		public static final String MESSAGE_BROADCAST_RECV_TEXT = "text_message_received";
		public static final String MESSAGE_BROADCAST_RECV_AUDIO = "audio_message_received";
		public static final String MESSAGE_BROADCAST_RECV_IMAGE = "image_message_received";
		public static final String MESSAGE_BROADCAST_RECV_COMPLETED = "message_received_completed";
		public static final String MESSAGE_BROADCAST_SEND_COMPLETED = "message_send_completed";
		public static final String CURRENT_CHAT_WITH_NOTIFICATION = "current_chat_with_notification";
		public static final String CLEAR_MESSAGE_RED_DOT = "clear_message_read_dot";
		public static final String ERASE_LOCAL_HISTORY = "erase_local_history";
		
		public static final int GAME_TYPE_EIGHTPUZZLE = 0x01;
		public static final int GAME_TYPE_SONGPUZZLE = 0x02;
		public static final int GAME_TYPE_BAZINGABALL = 0x03;
		
		public static final int DIRECTION_UP = 0x00;
		public static final int DIRECTION_DOWN = 0x01;
		public static final int DIRECTION_LEFT = 0x02;
		public static final int DIRECTION_RIGHT = 0x03;
		
		public static final int REQUESTCODE_GALLERY = 0x200;
		public static final int REQUESTCODE_CAMERA = 0x201;
		public static final int REQUESTCODE_CROP = 0x202;

		public static final int GAME_NOT_SET = 0x0023;

		public static final int HANDLER_WAIT_FOR_DATA = 0x0100;
		public static final int HANDLER_SUCCESS_GET_DATA = 0x101;

		//shake activity
		public static final int THRESHOLD_SPEED = 11;
		public static final int VIBRATE_TIME = 200;
		public static final int MAP_ZOOM_INITIALIZATION = 16;
		public static final double MAP_ZOOM_CHANGE_VALUE = 0.00001;
		public static final float MAP_MOVE_MAX_SPEED = 500;
		
		public static final int SHAKE_HANDLER_USER_GAME_NOT_SET = 1;
		public static final int SHAKE_HANDLER_SHAKE_SENSOR = 2;
		public static final int SHAKE_HANDLER_MARKER_CLICK = 3;
		public static final int SHAKE_HANDLER_MAP_STATUS_CHANGE = 4;
		public static final int SHAKE_HANDLER_MAP_TOUCH_DOWN = 5;
		public static final int SHAKE_HANDLER_MAP_TOUCH_MOVE = 6;
		public static final int SHAKE_HANDLER_MAP_FAST_MOVE = 7;
		public static final int SHAKE_HANDLER_GAME = 8;
		public static final int SHAKE_HANDLER_CHANGE_MARK = 9;
		public static final int SHAKE_HANDLER_COLLISION = 10;
		public static final int SHAKE_HANDLER_MAP_SHOW = 11;
		
		//game setting activity
		public static final int GAMESET_HANDLER_DOWNLOAD_IMAGE = 1;
		public static final int GAMESET_HANDLER_INIT_SONG = 2;
		public static final int GAMESET_HANDLER_INIT_MOLE = 3;
		public static final int GAMESET_HANDLER_INIT_GAMETYPE = 4;
		public static final int GAMESET_REQUESTCODE_SONG = 3;

		public static final String GAMESET_IMAGE_NAME = "EightPuzzleGame.jpg";
		
		public static final String GAMESET_SONG_PUZZLE_FOR_SINGER = "0";
		public static final String GAMESET_SONG_PUZZLE_FOR_SONG = "1";
		public static final String GAMESET_KEY_SINGER = "singer";
		public static final String GAMESET_KEY_SONG = "song";
		public static final String GAMESET_KEY_LYRIC = "lyric";
		public static final String GAMESET_KEY_ANSWERTYPE = "puzzle_answer";
		public static final String GAMESET_DEFAULT_SINGER = "∏Ë ÷";
		
		//user basic setting Activity
		public static final String USERSET_PORTRAIT = "Portrait.jpg";
		public static final int USERSET_HANDLER_PROVINCE = 1;
		public static final int USERSET_HANDLER_CITY = 2;
		public static final int USERSET_HANDLER_DISTRICT = 3;
		public static final int USERSET_HANDLER_HOMETOWN = 4;
		public static final int USERSET_HANDLER_SET_NICKNAME = 5;
		public static final int USERSET_HANDLER_SET_BIRTHDAY = 6;
		public static final int USERSET_HANDLER_SET_PHONE = 7;
		public static final int USERSET_HANDLER_SET_SEX = 8;
		
		//webservice package name
		public static final String PACKAGE_NETWORK = "network.com";
		public static final String PACKAGE_GAME_SETTING = "gameSettingPackage.lj.com";
		public static final String PACKAGE_GAME = "gamePackage.lj.com";
		
		//songpuzzle
		public static final int SONGPUZZLE_HANDLER_GET_SONGDATA = 1;
		public static final int SONGPUZZLE_NUM = 3;
						
		//BazingaBall
		public static final int BAZINGABALL_HANDLER_MOVE = 1;
		public static final int BAZINGABALL_HANDLER_UPDATE_SCORE = 2;
		public static final int BAZINGABALL_HANDLER_GET_SCORE = 3;
		
		public static final int USERSTATISTICS_HANDLER_GETDATA = 1;
	}
	
	public class Configs
	{
		public static final String WEBSERVICE_NAMESPACE = "http://network.com";
		public static final String OPENFIRE_SERVER_IP = "101.200.230.173";
	//	public static final String OPENFIRE_SERVER_IP = "192.168.95.1";

		public static final String WEBSERVICE_SERVER_IP = "101.200.230.173";
	//	public static final String WEBSERVICE_SERVER_IP = "192.168.95.1";
		public static final int WEBSERVICE_SERVER_PORT = 8080;
		public static final String WEBSERVICE_ENDPOINT = "http://" + WEBSERVICE_SERVER_IP + ":8080/WebServiceProject/services/NetworkHandler";
		
		public static final int OPENFIRE_SERVER_PORT = 5222;
		public static final String OPENFIRE_SERVER_NAME = "101.200.230.173";
		
		public static final int INDEX_LOGINACCOUNT = 0;
		public static final int INDEX_PASSWORD = 1;
		
		public static final String MESSAGE_TEXT_HEAD = "0x213";
		public static final String MESSAGE_AUDIO_HEAD = "0x213";
		public static final String MESSAGE_IMAGE_HEAD = "0x213";
	}
}