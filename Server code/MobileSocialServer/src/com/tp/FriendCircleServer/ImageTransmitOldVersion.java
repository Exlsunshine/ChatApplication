package com.tp.FriendCircleServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.kobjects.base64.Base64;

import Decoder.BASE64Decoder;

import com.database.SQLServerEnd;

/**
 * LJ
 */
public class ImageTransmitOldVersion 
{
	//存储上传图像的路径
	private final String SAVED_DIRECTORY = "D:/IM/data/image_transportation";
	//数据库名
	private final String DATABASE_NAME = "JMMSRDB";
	//表名
	private final String TABLE_NAME = "picture_transportation";
	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);

	/**
	 * 生成图像名称（唯一）
	 * @return 图像名称
	 */
	private String generateImageName(int fromUserID, int toUserID)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		
		return "picture_transportation_fromUserID_" + String.valueOf(fromUserID) + "_" + String.valueOf(toUserID)
				+ "_" + dateFormat.format(date) + ".jpg";
	}
	
	private byte[] string2Byte(String imageBuffer) throws Exception
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(imageBuffer);
		return buffer;
	}
	/**
	 * 将字符串转成file并保存于本地
	 * @param imageBuffer 待保存字符串
	 * @return 保存路径
	 * @throws Exception
	 */
	private String saveImage(String imageBuffer, int fromUserID, int toUserID) throws Exception
	{
		File destDir = new File(SAVED_DIRECTORY); 
		if(!destDir.exists())
            destDir.mkdir();    
		String imageName = generateImageName(fromUserID, toUserID);
		String imagePath = SAVED_DIRECTORY + imageName;
		FileOutputStream fos = null;
		byte[] buffer = string2Byte(imageBuffer);
		fos = new FileOutputStream(new File(destDir, imageName));
		fos.write(buffer);    
		fos.flush();    
        fos.close(); 
        return imagePath;
	}
	
	/**
	 * 保存图像后更新数据库
	 * @param from_userid 发送方id
	 * @param to_userid 接收方id
	 * @param imagePath 图像保存路径
	 */
	
	private void updateDataBaseWhenUpload(int from_userid, int to_userid, String imagePath)
	{
		String[] column = {"from_userid", "to_userid", "pic_path"};
	    String[] value = {String.valueOf(from_userid), String.valueOf(to_userid), imagePath};
	    sql.insert(column, value);
	}
	
	/**
	 * 获得图像id
	 * @param imagePath 图像路径
	 * @return 图像id
	 */
	private String getImageId(String imagePath)
	{
		String[] query = {"id"};
        String[] condition = {"pic_path"};
        String[] conditionVal = {imagePath};
        ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
        return map.get(0).get("id").toString();
	}
	
	/**
	 * 上传图像函数，被客户端调用
	 * @param from_userid 发送方id
	 * @param to_userid 接收方id
	 * @param imageBuffer 图像字符串缓冲区
	 * @return 上传后图像在图像数据库中的图像id
	 * @throws Exception
	 */
	public int uploadImage(int from_userid, int to_userid, String imageBuffer) throws Exception
	{	
		System.out.println("User [" + from_userid + "] send Image to User [" + to_userid + "]");
		String imagePath = saveImage(imageBuffer, from_userid, to_userid);
        updateDataBaseWhenUpload(from_userid, to_userid, imagePath);
        String imageId = getImageId(imagePath);
        return Integer.parseInt(imageId);
	}
	
	/**
	 * 根据图像id获得该图像的保存路径
	 * @param imageId 图像id
	 * @return 保存路径
	 */
	private String getImagePath(int imageId)
	{
		String[] query = {"pic_path"};
		String[] condition = {"id"};
		String[] conditionVal = {String.valueOf(imageId)};
		ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
		return map.get(0).get("pic_path").toString();
	}
	
	/**
	 * 从图像路径中提取图像文件，并转换成字符串
	 * @param imagePath 图像路径
	 * @return 转完结果
	 * @throws Exception
	 */
	public static String image2String(String imagePath) throws Exception
	{
		//@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(imagePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int count = 0;
		while((count = fis.read(buffer)) >= 0)   
		    baos.write(buffer, 0, count);
		System.out.print("\n" + "______________" + "\n");
		String result = new String(new Base64().encode(baos.toByteArray())); 
		fis.close();
		baos.close();
		return result;
	}
	
	/**
	 * 下载图像，被客户端调用
	 * @param imageId 待下载的图像id
	 * @return 图像字符串
	 * @throws Exception
	 */
	public String downloadImage(int imageId) throws Exception
	{
		String imagePath = getImagePath(imageId);
		String imageBuffer = image2String(imagePath);
		System.out.println("Get ImageID:" + imageId + " " + imageBuffer.length());
		return imageBuffer;
	}
}