package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PortraitTransmit
{
	private int userID;
	private static final String portraitDir = "C:/Users/USER007/Desktop/IM/data/portrait/";
	
	public PortraitTransmit(int userID)
	{
		this.userID = userID;
	}
	
	public String getSavedImgPath()
	{
		return portraitDir + generateImageName(userID);
	}
	
	/**
	 * 生成图像名称（唯一）
	 * @param userID 上传用户ID
	 * @return 生成的图像名称
	 */
	private String generateImageName(int userID)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		
		return "portrait_userID_" + String.valueOf(userID) + "_" + dateFormat.format(date) + ".jpg";
	}
}