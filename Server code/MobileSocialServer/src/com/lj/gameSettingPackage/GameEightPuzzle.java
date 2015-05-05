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
	private final String DIR_PATH = "C:/Users/USER007/Desktop/IM/data/game_settings_images";
	
	private String generateImageName(int userID)
	{
		return userID + ".jpg";
	}
	private byte[] string2Byte(String imageBuffer) throws Exception
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(imageBuffer);
		return buffer;
	}
	private String saveImage(int userID) throws Exception
	{
		File destDir = new File(DIR_PATH); 
		if(!destDir.exists())
            destDir.mkdir();    
		String imageName = generateImageName(userID);
		String imagePath = DIR_PATH + "/" + imageName;
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
	public String getUploadImageUrl(int userID) throws Exception
	{	
		System.out.println("User [" + userID + "] upload 8Puzzle Image");
		String imagePath = saveImage(userID);
        updateDataBaseWhenUpload(userID, imagePath);
        imagePath = imagePath.replace("C:/Users/USER007/Desktop/IM/data/", "");
        String imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
        System.out.println(imageUrl);
        return imageUrl;
        
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
	public String getEightPuzzleImageUrl(int userID) throws Exception
	{
		String imagePath = getImagePath(userID);
		if (imagePath == null)
			return null;
		imagePath = imagePath.replace("C:/Users/USER007/Desktop/IM/data/", "");
		String imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
		System.out.println(imageUrl);
		return imageUrl;
	}
}
