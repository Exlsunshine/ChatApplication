package com.yg.user;

import java.io.ByteArrayOutputStream;
import java.io.File;
import org.kobjects.base64.Base64;

import com.yg.commons.ConstantValues;
import com.yg.network.openfire.OpenfireHandler;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * LJ
 */
public class ImageTransportation 
{
	public static final String DEBUG_TAG = "ImageTransportation______";
	
	//图像压缩�?30 保留30%
	private final static int QUALITY = 30;
	//webservice 待调用包名
	private final String WEBSERVICE_IMAGE_PACKAGE = "network.com";
	//webservice 待调用类名
	private final String WEBSERVICE_IMAGE_CLASS = "ImageTransmit";
	
	//webservice 待调用函数名
	private final String WEBSERVICE_FUNCTION_UPLOAD = "uploadImage";
	
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
	 * 
	 * @param from_userid 发送者ID
	 * @param to_userid 接收者ID
	 * @param file 图片文件
	 * @return 图片的下载链接
	 * @throws Exception
	 */
	public String uploadImage(int from_userid, int to_userid, File file, OpenfireHandler handler, String oID) throws Exception
	{
		String[] name = {"from_userid", "to_userid"};
		Object[] values = {from_userid, to_userid};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);

		String imgUrl = result.toString();
		Log.i(DEBUG_TAG, "Uploading image to " + imgUrl);
		String serverIP = imgUrl.substring(imgUrl.indexOf("http://") + 7, imgUrl.indexOf(':', 7));
		String newFileName = imgUrl.substring(imgUrl.indexOf('/', 7) + 1, imgUrl.length());
		String serverPort = imgUrl.substring(imgUrl.indexOf(':', 5) + 1, imgUrl.indexOf(':', 5) + 5);
		UploadManager um = new UploadManager(file, newFileName, serverIP, serverPort);
		um.setHandler(handler, result.toString(), oID, ConstantValues.InstructionCode.MESSAGE_IMAGE_FLAG);
		um.excecuteUpload();
		
		return imgUrl;
	}
	
	/**
	 * 下载图像
	 * @param imgUrl 待下载图像的下载链接
	 * @return 下载成功的图像(Bitmap类型)
	 */
	public Bitmap downloadImage(String imgUrl)
	{
		Log.i(DEBUG_TAG, "Downloading image at " + imgUrl);
		DownloadManager dm = new DownloadManager(imgUrl);
		return dm.getBmpFile();
	}
}