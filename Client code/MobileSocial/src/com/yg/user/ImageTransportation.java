package com.yg.user;

import java.io.ByteArrayOutputStream;
import org.kobjects.base64.Base64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * LJ
 */
public class ImageTransportation 
{
	//图像压缩�?30 保留30%
	private final static int QUALITY = 30;
	//webservice 待调用包�?
	private final String WEBSERVICE_IMAGE_PACKAGE = "network.com";
	//webservice 待调用类�?
	private final String WEBSERVICE_IMAGE_CLASS = "ImageTransmit";
	
	//webservice 待调用函数名
	private final String WEBSERVICE_FUNCTION_UPLOAD = "uploadImage";
	private final String WEBSERVICE_FUNCTION_DOWNLOAD = "downloadImage";
	
	private WebServiceAPI imageApi = new WebServiceAPI(WEBSERVICE_IMAGE_PACKAGE, WEBSERVICE_IMAGE_CLASS);
	
	/**
	 * bitmap �?String
	 * @param bitmap 待转换bitmap
	 * @return 转结�?
	 * @throws Exception
	 */
	public static String image2String(Bitmap bitmap) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos); 
        String imageBuffer = new String(Base64.encode(baos.toByteArray()));
        return imageBuffer;
	}
	
	/**
	 * 上传图像，得到该图像对应数据库中的图像id
	 * @param from_userid 发�?方id
	 * @param to_userid 接收方id
	 * @param bitmap 待上传图像的bitmap
	 * @return 图像对应的数据库中的图像id
	 * @throws Exception
	 */
	public int uploadImage(int from_userid, int to_userid, Bitmap bitmap) throws Exception
	{
		String imageBuffer = image2String(bitmap);
		String[] name = {"from_userid", "to_userid", "imageBuffer"};
		Object[] values = {from_userid, to_userid, imageBuffer};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);
		//int a = (Integer)result;
		return Integer.parseInt(result.toString());
	}
	
	/**
	 * 字符串转bitmap
	 * @param imageBuffer 待转换字符串
	 * @return 转换结果
	 */
	public static Bitmap string2Bitmap(String imageBuffer)
	{
		byte[] buffer = Base64.decode(imageBuffer);
		Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		return bitmap;
	}
	
	/**
	 * 下载图像
	 * @param imageId 待下载图像的图像id
	 * @return 下载完成图像的bitmap
	 */
	public Bitmap downloadImage(int imageId)
	{
		Bitmap bitmap = null;
		String[] name = {"imageId"};
		Object[] values = {imageId};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_DOWNLOAD, name, values);
		bitmap = string2Bitmap(result.toString());
		return bitmap;
	}
}