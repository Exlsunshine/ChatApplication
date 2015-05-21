package com.tp.FriendCircleServer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.kobjects.base64.Base64;



public class ImagePostTransmit 
{
	//存储上传图像的路径
		private final static String SAVED_DIRECTORY = "D:/Data/IM/data/friendCircleImages/";

		/**
		 * 生成图像名称（唯一）
		 * @return 图像名称
		 */
		private static String generateImageName(int postUserID)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
			Date date = new Date();
			return "picture_transportation_postUserID_" + String.valueOf(postUserID) + "_" + dateFormat.format(date)  + ".jpg";
		}
		
		private static byte[] string2Byte(String imageBuffer) throws Exception
		{
			System.out.print("before decode");
			byte[] buffer = new Base64().decode(imageBuffer);
			System.out.print("after decode");
			return buffer;
		}
		/**
		 * 将字符串转成file并保存于本地
		 * @param imageBuffer 待保存字符串
		 * @return 保存路径
		 * @throws Exception
		 */
		public static String saveImage(String imageBuffer, int postUserID) throws Exception
		{
			File destDir = new File(SAVED_DIRECTORY); 
			if(!destDir.exists())
	            destDir.mkdir();    
			String imageName = generateImageName(postUserID);
			String imagePath = SAVED_DIRECTORY + imageName;
			FileOutputStream fos = null;
			byte[] buffer = string2Byte(imageBuffer);
			fos = new FileOutputStream(new File(destDir, imageName));
			fos.write(buffer);    
			fos.flush();    
	        fos.close(); 
	        return imagePath;
		}
		
				
}
