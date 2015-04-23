package com.yg.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DownloadManager
{
	private static final String DEBUG_TAG = "DownloadManager______";
	private String url;
	
	public DownloadManager(String url)
	{
		this.url = url;
	}
	
	/**
	 * 下载并返回数据<br>
	 * @return 下载到手机中的Bitmap<br>
	 * null 表示下载失败
	 */
	public Bitmap getBmpFile()
	{
		if (url.contains(".jpg") || url.contains(".png") || url.contains(".bmp"))
		{
			ImageDownloadThread download = new ImageDownloadThread(url);
			download.start();
			
			synchronized (download) 
			{
				try
				{
					download.wait();
					return download.bitmap;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else
			Log.i(DEBUG_TAG, "Wrong type, your url does not contains .jpg suffix");

		return null;
	}
	
	/**
	 * 下载并返回数据<br>
	 * @return 下载到手机中的byte []<br>
	 * null 表示下载失败
	 */
	public byte [] getAudioFile()
	{
		if (url.contains(".amr"))
		{
			AudioDownloadThread download = new AudioDownloadThread(url);
			download.start();
			
			synchronized (download) 
			{
				try
				{
					download.wait();
					return download.bytes;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else
			Log.i(DEBUG_TAG, "Wrong type, your url does not contains .amr suffix");

		return null;
	}
	
	private class ImageDownloadThread extends Thread
	{
		private Bitmap bitmap = null;
		private String url = null;
		
		public ImageDownloadThread(String url)
		{
			this.url = url;
		}
		
		@Override
		public void run() 
		{
			super.run();
			synchronized (this) 
			{
				try 
				{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					options.inInputShareable = true;
					options.inSampleSize = 2;
					
					Log.i(DEBUG_TAG, "Downloading image at " + url);
					InputStream is = new java.net.URL(url).openStream();
					bitmap = BitmapFactory.decodeStream(is, null, options);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				notify();
			}
		}
	}
	
	private class AudioDownloadThread extends Thread
	{
		private byte [] bytes = null;
		private String url = null;
		
		public AudioDownloadThread(String url)
		{
			this.url = url;
		}
		
		@Override
		public void run() 
		{
			super.run();
			synchronized (this) 
			{
				try 
				{
					Log.i(DEBUG_TAG, "Downloading audio at " + url);
					InputStream is = new java.net.URL(url).openStream();

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					int nRead = 0;
					byte [] data = new byte[16384];
					while ((nRead = is.read(data)) != -1)
						buffer.write(data, 0, nRead);
					
					buffer.flush();
					bytes = buffer.toByteArray();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				notify();
			}
		}
	}
	
	public static void main(String[] args)
	{
		/*						
		 *		Sample code		
		 *
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				
				imageView.setImageBitmap((Bitmap)msg.obj);
				System.gc();
			}
		};
		
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String url = "http://172.18.8.30:8887/image_transportation/picture_transportation_fromUserID_4_6_2015-04-15-20-36-44.jpg";
				DownloadManager dm = new DownloadManager(url);
						
				Message msg = new Message();
				msg.obj = dm.getBmpFile();
				handler.sendMessage(msg);
			}
		});
		td.start();
		*/
	}
}