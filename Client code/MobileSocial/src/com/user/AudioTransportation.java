package com.user;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import org.kobjects.base64.Base64;

/**
 * LJ
 */
public class AudioTransportation 
{
	//webservice 待调用包名
	private final String WEBSERVICE_AUDIO_PACKAGE = "network.com";
	//webservice 待调用类名
	private final String WEBSERVICE_AUDIO_CLASS = "AudioTransmit";
		
	//webservice 待调用函数名
	private final String WEBSERVICE_FUNCTION_UPLOAD = "uploadAudio";
	private final String WEBSERVICE_FUNCTION_DOWNLOAD = "downloadAudio";
		
	private WebServiceAPI imageApi = new WebServiceAPI(WEBSERVICE_AUDIO_PACKAGE, WEBSERVICE_AUDIO_CLASS);
	
	
	private String audio2String(String audioPath) throws Exception
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
	}
	
	public int uploadAduio(int from_userid, int to_userid, String audioPath) throws Exception
	{
		String audioBuffer = audio2String(audioPath);
		String[] name = {"from_userid", "to_userid", "audioBuffer"};
		Object[] values = {from_userid, to_userid, audioBuffer};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_UPLOAD, name, values);
		return Integer.parseInt(result.toString());
	}
	
	private byte[] string2Byte(String audioBuffer)
	{
		byte[] buffer = Base64.decode(audioBuffer);
		return buffer;
	}
	
	public byte[] downloadAudio(int audioId)
	{
		String[] name = {"audioId"};
		Object[] values = {audioId};
		Object result = imageApi.callFuntion(WEBSERVICE_FUNCTION_DOWNLOAD, name, values);
		byte[] buffer = string2Byte(result.toString());
		return buffer;
	}
}
