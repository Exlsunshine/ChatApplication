package com.commons;

/**
 * @author LiJian
 */
public class ConstantValues
{
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
		
		
		public static final int GAME_TYPE_EIGHTPUZZLE = 0x00;
		public static final int GAME_TYPE_QUIZ = 0x00;
		public static final int GAME_TYPE_WHAC = 0x00;
		
		public static final int DIRECTION_UP = 0x00;
		public static final int DIRECTION_DOWN = 0x00;
		public static final int DIRECTION_LEFT = 0x00;
		public static final int DIRECTION_RIGHT = 0x00;
	}
	public class Configs
	{
		public static final String WEBSERVICE_NAMESPACE = "http://network.com";
		public static final String WEBSERVICE_ENDPOINT = "http://172.18.8.142:8080/WebServiceProject/services/NetworkHandler";
		public static final String OPENFIRE_SERVER_IP = "172.18.8.142";
		public static final int OPENFIRE_SERVER_PORT = 5222;
		public static final String OPENFIRE_SERVER_NAME = "user007-pc";
		
		public static final int INDEX_LOGINACCOUNT = 0;
		public static final int INDEX_PASSWORD = 1;
		
		public static final String MESSAGE_TEXT_HEAD = "0x213";
		public static final String MESSAGE_AUDIO_HEAD = "0x213";
		public static final String MESSAGE_IMAGE_HEAD = "0x213";
	}
}