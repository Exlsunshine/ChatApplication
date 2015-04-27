/**
 * LJ
 */
package com.lj.shake;

public class UserShakeData 
{
	private int userId;
	private String nickName;
	private String sex;
	private float longitude;
	private float latitude;
	private String dateTime;
		
	/**
	 * GAME_TYPE_EIGHTPUZZLE 图片华容道
	 * GAME_TYPE_QUIZ 歌迷
	 * GAME_TYPE_WHAC 打地鼠
	 */
	private int gameType;
	
	public UserShakeData(int userid, String nickname, float longitude, float latitude, int gametype, String sex) 
	{
		userId = userid;
		nickName = nickname;
		this.longitude = longitude;
		this.latitude = latitude;
		gameType = gametype;
		this.sex = sex;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public String getNickName()
	{
		return nickName;
	}
	
	public String getSex() 
	{
		return sex;
	}
	
	public float getLongitude()
	{
		return longitude;
	}
	
	public float getLatitude()
	{
		return latitude;
	}
	
	public String getDateTime()
	{
		return dateTime;
	}
	
	public int getGameType()
	{
		return gameType;
	}
}
