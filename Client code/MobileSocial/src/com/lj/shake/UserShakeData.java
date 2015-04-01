/**
 * LJ
 */
package com.lj.shake;


public class UserShakeData 
{
	//用户id
	private int userId;
	//昵称
	private String nickName;
	//性别
	private String sex;
	//地理位置
	private float longitude;
	private float latitude;
	//摇一摇时间
	private String dateTime;
		
	/**
	 * GAME_TYPE_EIGHTPUZZLE：图片华容道
	 * GAME_TYPE_QUIZ：音乐闯关
	 * GAME_TYPE_WHAC：打地鼠
	 */
	private int gameType;
	
	public UserShakeData(int userid, String nickname, float longitude, float latitude, int gametype) 
	{
		// TODO Auto-generated constructor stub
		userId = userid;
		nickName = nickname;
		this.longitude = longitude;
		this.latitude = latitude;
		gameType = gametype;
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
