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
	 * ����ͼ�����ƣ�Ψһ��
	 * @param userID �ϴ��û�ID
	 * @return ���ɵ�ͼ������
	 */
	private String generateImageName(int userID)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		
		return "portrait_userID_" + String.valueOf(userID) + "_" + dateFormat.format(date) + ".jpg";
	}
}