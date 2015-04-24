package com.tp.messege;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.kobjects.base64.Base64;

import android.os.Environment;
import android.util.Log;



public class ImagePostTransmit 
{
	//存储上传图像的路径
		private final static String SAVED_DIRECTORY = "D:\\IM\\data\\image_transportation\\";

		/**
		 * 生成图像名称（唯一）
		 * @return 图像名称
		 */
		private static String generateImageName(int postUserID)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date date = new Date();
			return "picture_transportation_postUserID_" + String.valueOf(postUserID) + "_" + dateFormat.format(date) + ".jpg";
		}
		
		public static byte[] string2Byte(String imageBuffer) throws Exception
		{
			byte[] buffer = new Base64().decode(imageBuffer);
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
			File destDir = new File(getSDPath()); 
			if(!destDir.exists())
	            destDir.mkdir();    
			String imageName = generateImageName(postUserID);
			String imagePath = getSDPath() + imageName;
			FileOutputStream fos = null;
			byte[] buffer = string2Byte(imageBuffer);
			fos = new FileOutputStream(new File(destDir, imageName));
			fos.write(buffer);    
			fos.flush();    
	        fos.close(); 
	        return imagePath;
		}
		
		private static String getSDPath()
		{ 
		       File sdDir = null; 
		       //boolean sdCardExist = Environment.getExternalStorageState().equals(Android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 

		      /* if (sdCardExist)      //如果SD卡存在，则获取跟目录
		       {*/
		    	   sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
		      /* }   */
		       return sdDir.toString() + "/tp/"; 
		       
		}

		
}
