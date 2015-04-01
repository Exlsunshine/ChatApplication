package com.lj.gameSettingPackage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class GameEightPuzzle
{
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "game_picturepuzzle_setting";
	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	private final String DIR_PATH = "C:\\Users\\USER007\\Desktop\\IM\\data\\game_settings_images";
	
	private String generateImageName(int userID)
	{
		return userID + ".jpg";
	}
	private byte[] string2Byte(String imageBuffer) throws Exception
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(imageBuffer);
		return buffer;
	}
	private String saveImage(int userID, String imageBuffer) throws Exception
	{
		File destDir = new File(DIR_PATH); 
		if(!destDir.exists())
            destDir.mkdir();    
		String imageName = generateImageName(userID);
		String imagePath = DIR_PATH + "\\" + imageName;
		FileOutputStream fos = null;
		byte[] buffer = string2Byte(imageBuffer);
		fos = new FileOutputStream(new File(destDir, imageName));
		fos.write(buffer);    
		fos.flush();    
        fos.close(); 
        return imagePath;
	}
	private void updateDataBaseWhenUpload(int userID, String imagePath)
	{
		String[] condition = {"user_id"};
        String[] conditionVal = {String.valueOf(userID)};
        boolean result = sql.isConditionExist(condition, conditionVal);
        if (result)
        	sql.delete(condition, conditionVal);
		String[] column = {"user_id", "pic_path"};
	    String[] value = {String.valueOf(userID), imagePath};
	    sql.insert(column, value);
	}
	public int uploadImage(int userID, String imageBuffer) throws Exception
	{	
		System.out.println("User [" + userID + "] upload 8Puzzle Image");
		String imagePath = saveImage(userID, imageBuffer);
        updateDataBaseWhenUpload(userID, imagePath);
        return ConstantValues.InstructionCode.SUCCESS;
        
	}
	
	private String getImagePath(int userID)
	{
		String[] query = {"pic_path"};
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
		if (map.size() == 0)
			return null;
		return map.get(0).get("pic_path").toString();
	}
	private String image2String(String imagePath) throws Exception
	{
		FileInputStream fis = new FileInputStream(imagePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int count = 0;
		while((count = fis.read(buffer)) >= 0)   
		    baos.write(buffer, 0, count);
		String result = new String(new BASE64Encoder().encode(baos.toByteArray())); 
		fis.close();
		baos.close();
		return result;
	}
	public String downloadImage(int userID) throws Exception
	{
		String imagePath = getImagePath(userID);
		if (imagePath == null)
			return null;
		String imageBuffer = image2String(imagePath);
		return imageBuffer;
	}
}
