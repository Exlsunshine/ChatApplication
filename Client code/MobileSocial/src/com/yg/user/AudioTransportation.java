package com.yg.user;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.kobjects.base64.Base64;

import android.util.Log;

/**
 * LJ
 */
public class AudioTransportation 
{
	public static final String DEBUG_TAG = "AudioTransportation______";
	//webservice 待调用包名
	private final String WEBSERVICE_AUDIO_PACKAGE = "network.com";
	//webservice 待调用类名
	private final String WEBSERVICE_AUDIO_CLASS = "AudioTransmit";
		
	//webservice 待调用函数名
	private final String WEBSERVICE_FUNCTION_UPLOAD = "uploadAudio";
		
	private WebServiceAPI imageApi = new WebServiceAPI(WEBSERVICE_AUDIO_PACKAGE, WEBSERVICE_AUDIO_CLASS);
	
	
	/*private String audio2String(String audioPath) throws Exception
	{
		FileInputStream fis = new FileInputStream(audioPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024];    
        int count = 0; 
        while((count = fis.read(buffer)) >= 0){    
            baos.write(buffer, 0, count);
        } 
        
        String uploadBuffer = new String(Base64.encode(baos.toByteArray()));
        fis.close();
        return uploadBuffer;
	}*/
	
	public String uploadAduio(int from_userid, int to_userid, File file) throws Exception
	{
		/*String audioBuffer = audio2String(audioPath);
		String[] name = {"from_userid", "to_userid", "audioBuffer"};
		Object[] values = {from_userid, to_userid, audioBuffer};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);
		return result.toString();*/
		
		String[] name = {"from_userid", "to_userid"};
		Object[] values = {from_userid, to_userid};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);
		
		
		String audioUrl = result.toString();
		Log.i(DEBUG_TAG, "Uploading image to " + audioUrl);
		String serverIP = audioUrl.substring(audioUrl.indexOf("http://") + 7, audioUrl.indexOf(':', 7));
		String newFileName = audioUrl.substring(audioUrl.indexOf('/', 7) + 1, audioUrl.length());
		String serverPort = audioUrl.substring(audioUrl.indexOf(':', 5) + 1, audioUrl.indexOf(':', 5) + 5);
		UploadManager um = new UploadManager(file, newFileName, serverIP, serverPort);
		um.excecuteUpload();
		
		return result.toString();
	}
	
	public byte[] downloadAudio(String audioUrl)
	{
		byte [] bytes = null;
		DownloadThread download = new DownloadThread(audioUrl);
		download.start();
		
		synchronized (download) 
		{
			try
			{
				download.wait();
				bytes = download.bytes;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return bytes;
	}
	
	private class DownloadThread extends Thread
	{
		private byte [] bytes = null;
		private String url = null;
		
		public DownloadThread(String url)
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
					Log.i(DEBUG_TAG, "Downloading portrait at " + url);
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
}