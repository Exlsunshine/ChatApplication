package com.lj.driftbottle.logic;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * communicaiton bottle
 * @author admin
 *
 */
public class CommBottle extends AbstractBottle
{
	//json object key string
	public static final String OWNER_ID = "ownerID";
	public static final String BOTTLE_STATUS = "bottleStatus";
	public static final String BOTTLE_RELATION_STATUS = "bottleRelationStatus";
	public static final String PORTRIAT_URL = "portraitURL";
	public static final String VERSION_CODE = "versionCode";
	public static final String BOTTLE_ID = "bottleID";
	public static final String NICK_NAME = "nickname";
	public static final String SEX = "sex";
	public static final String HOMETOWN = "hometown";
	
	public static final int BOTTLE_STATUS_UNPICKED = 1;
	public static final int BOTTLE_STATUS_PICKED = 2;
	
	public static final int BOTTLE_RELATION_STATUS_NORMAL = 1;
	public static final int BOTTLE_RELATION_STATUS_DELETE = 2;
	
	private int ownerID = 0;
	private String nickname = null;
	private String sex = null;
	private String hometown = null;
	private String portraitURL = "";
	private int bottleStatus = 0;
	private int bottleRelationStatus = 0;
	private int bottleID = 0;
	private int versionCode = 0;
	
	/**
	 * constructor function
	 * @param historyText history text
	 * @param portraitURL url of portrait
	 * @param bottleStatus status of bottle<br>
	 * BOTTLE_STATUS_UNPICKED : bottle that is unpicked<br>
	 * BOTTLE_STATUS_PICKED : bottle that is picked
	 * @param bottleRelationStatus status of bottle relation<br>
	 * BOTTLE_RELATION_STATUS_NORMAL : bottle is normal<br>
	 * BOTTLE_RELATION_STATUC_DELETE : bottle is deleted
	 * @param bottleID if of bottle
	 * @param versionCode version code of bottle
	 */
	public CommBottle(int ownerID, String nickname, String sex, String hometown, String historyText, String portraitURL, int bottleStatus, int bottleRelationStatus, int bottleID, int versionCode) 
	{
		super(historyText);
		this.ownerID = ownerID;
		this.nickname = nickname;
		this.sex = sex;
		this.hometown = hometown;
		this.portraitURL = portraitURL;
		this.bottleStatus = bottleStatus;
		this.bottleID = bottleID;
		this.versionCode = versionCode;
		this.bottleRelationStatus = bottleRelationStatus;
	}
	
	public CommBottle(int ownerID, String nickname, String sex, String hometown, AbstractBottle bottle, String portraitURL, int bottleStatus, int bottleRelationStatus, int bottleID, int versionCode)
	{
		this(ownerID, nickname, sex, hometown, bottle.getHistoryText(), portraitURL, bottleStatus, bottleRelationStatus, bottleID, versionCode);
	}
	@Override
	public String toJsonString() 
	{
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(APPEND_TEXT, getAppendText());
		//	jsonObject.put(HISTORY_TEXT, getHistoryText());
		//	jsonObject.put(PORTRIAT_URL, getPortraitURL());
		//	jsonObject.put(BOTTLE_STATUS, getBottleStatus());
		//	jsonObject.put(BOTTLE_RELATION_STATUS, getBottleRelationStatus());
			jsonObject.put(BOTTLE_ID, String.valueOf(getBottleID()));
			jsonObject.put(VERSION_CODE, String.valueOf(getVersionCode()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	public int getOwnerID()
	{
		return ownerID;
	}
	
	public String getLastText()
	{
		String[] strList = getHistoryText().split("\\n");
		return strList[strList.length - 1].replaceAll("u\\d+:", "");
	}
	
	public int getBottleID()
	{
		return bottleID;
	}
	
	public int getBottleRelationStatus() 
	{
		return bottleRelationStatus;
	}
	
	public String getPortraitURL() 
	{
		return portraitURL;
	}
	
	public void updateVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}
	
	public int getBottleStatus()
	{
		return bottleStatus;
	}
	
	public int getVersionCode() 
	{
		return versionCode;
	}
	
	public String getNickname()
	{
		return nickname;
	}
	
	public String getSex()
	{
		return sex;
	}
	
	public String getHometown()
	{
		return hometown;
	}
	public static void main(String[] args) 
	{
	}
}
