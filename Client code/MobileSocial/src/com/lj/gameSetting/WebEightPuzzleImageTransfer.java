package com.lj.gameSetting;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import com.yg.commons.CommonUtil;
import com.yg.user.WebServiceAPI;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class WebEightPuzzleImageTransfer 
{
	//webservice 待调用包名
	private final String WEBSERVICE_IMAGE_PACKAGE = "imagePackage";
	//webservice 待调用类名
	private final String WEBSERVICE_IMAGE_CLASS = "GameEightPuzzle";
		
	//webservice 待调用函数名
	private final String WEBSERVICE_FUNCTION_UPLOAD = "uploadImage";
	private final String WEBSERVICE_FUNCTION_DOWNLOAD = "downloadImage";
	private WebServiceAPI imageApi = new WebServiceAPI(WEBSERVICE_IMAGE_PACKAGE, WEBSERVICE_IMAGE_CLASS);
	
	private String image2String(Bitmap bitmap) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
        String imageBuffer = new String(Base64.encode(baos.toByteArray()));
        return imageBuffer;
	}
	
	public boolean uploadImage(int userID, Bitmap bitmap) throws Exception
	{
		String imageBuffer = image2String(bitmap);
		String[] name = {"userID", "imageBuffer"};
		Object[] values = {userID, imageBuffer};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);
		if (CommonUtil.isNetWorkError(result))
			return false;
		return true;
	}
	
	private Bitmap string2Bitmap(String imageBuffer)
	{
		byte[] buffer = Base64.decode(imageBuffer);
		Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		return bitmap;
	}
	
	public Bitmap downloadImage(int userID)
	{
		Bitmap bitmap = null;
		String[] name = {"userID"};
		Object[] values = {userID};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_DOWNLOAD, name, values);
		if (result == null || CommonUtil.isNetWorkError(result))
			return null;
		bitmap = string2Bitmap(result.toString());
		return bitmap;
	}
}
