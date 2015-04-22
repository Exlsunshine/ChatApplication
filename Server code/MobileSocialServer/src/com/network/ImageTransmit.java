package com.network;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;

import Decoder.BASE64Decoder;

/**
 * LJ
 */
public class ImageTransmit 
{
	//存储上传图像的路径
	private final String SAVED_DIRECTORY = "C:/Users/USER007/Desktop/IM/data/image_transportation/";
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
	 * 上传图像函数，被客户端调用
	 * @param from_userid 发送方id
	 * @param to_userid 接收方id
	 * @param imageBuffer 图像字符串缓冲区
	 * @return 上传后图像在图像数据库中的图像id
	 * @throws Exception
	 */
	public String uploadImage(int from_userid, int to_userid, String imageBuffer) throws Exception
	{	
		System.out.println("User [" + from_userid + "] send Image to User [" + to_userid + "]");
		String imagePath = saveImage(imageBuffer, from_userid, to_userid);
        updateDataBaseWhenUpload(from_userid, to_userid, imagePath);


        imagePath = imagePath.replace("C:/Users/USER007/Desktop/IM/data/", "");
        String imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
        return imageUrl;
	}
}