package com.yg.user;

import java.io.File;

import com.yg.network.openfire.OpenfireHandler;

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
	
	public String uploadAduio(int from_userid, int to_userid, File file, OpenfireHandler handler, String oID) throws Exception
	{
		String[] name = {"from_userid", "to_userid"};
		Object[] values = {from_userid, to_userid};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);
		
		
		String audioUrl = result.toString();
		Log.i(DEBUG_TAG, "Uploading image to " + audioUrl);
		String serverIP = audioUrl.substring(audioUrl.indexOf("http://") + 7, audioUrl.indexOf(':', 7));
		String newFileName = audioUrl.substring(audioUrl.indexOf('/', 7) + 1, audioUrl.length());
		String serverPort = audioUrl.substring(audioUrl.indexOf(':', 5) + 1, audioUrl.indexOf(':', 5) + 5);
		UploadManager um = new UploadManager(file, newFileName, serverIP, serverPort);
		um.setHandler(handler, result.toString(), oID);
		um.excecuteUpload();
		
		return result.toString();
	}
	
	public byte[] downloadAudio(String audioUrl)
	{
		Log.i(DEBUG_TAG, "Downloading audio at " + audioUrl);
		DownloadManager dm = new DownloadManager(audioUrl);
		return dm.getAudioFile();
	}
}