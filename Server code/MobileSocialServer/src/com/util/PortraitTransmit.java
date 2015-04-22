package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PortraitTransmit
{
	private int userID;
	//private String portrait;
	private static final String portraitDir = "C:/Users/USER007/Desktop/IM/data/portrait/";
	
	public PortraitTransmit(int userID)
	{
		this.userID = userID;
		//this.portrait = portrait;
	}
	
	public String getSavedImgPath()
	{
		return portraitDir + generateImageName(userID);
		/*try {
			return saveImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/**
	 * ���ַ���ת��file�������ڱ���
	 * @param imageBuffer �������ַ���
	 * @return ����·��
	 * @throws Exception
	 */
	/*private String saveImg() throws Exception
	{
		File destDir = new File(portraitDir); 
		if(!destDir.exists())
			destDir.mkdir();
		
		try
		{
			String imgPath = portraitDir + generateImageName(userID);
			FileOutputStream fos = null;
			byte[] buffer = string2Byte(portrait);
			fos = new FileOutputStream(new File(imgPath));
			fos.write(buffer);    
			fos.flush();    
	        fos.close(); 
	        return imgPath;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}*/
	
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
	
	/*private byte[] string2Byte(String imageBuffer) throws Exception
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(imageBuffer);
		return buffer;
	}*/
}