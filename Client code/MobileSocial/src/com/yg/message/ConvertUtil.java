package com.yg.message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ConvertUtil
{
	public static byte [] string2Bytes(String str)
	{
		return str.getBytes();
	}
	
	public static String bytes2String(byte [] bytes)
	{
		try
		{
			return new String(bytes, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] bitmap2Bytes(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap bytes2Bitmap(byte[] bytes)
	{
		if (bytes.length != 0) 
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		else
			return null;
	}
	
	public static Drawable bitmap2Drawable(Bitmap bmp, Context context)
	{
		return new BitmapDrawable(context.getResources(), bmp); 
	}
	
	public static byte [] amr2Bytes(String amrFilePath) throws IOException
	{
		FileInputStream fis = new FileInputStream(amrFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer =new byte[1024];
        int read;
        while ((read = fis.read(buffer)) != -1)
            baos.write(buffer, 0, read);
        baos.flush();
        fis.close();
        
        return baos.toByteArray();
	}
	
	public static File bytes2AmrFile(byte [] bytes, Context context) throws IOException
	{
		String tempFilePath = context.getCacheDir() + "/" + "audio.amr";
		File tempFile = new File(tempFilePath);
		
		if (tempFile.exists())
			tempFile.delete();
		
		if (tempFile.createNewFile())
		{
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(bytes);
			fos.close();
			return tempFile;
		}
		else
			return null;
	}
}