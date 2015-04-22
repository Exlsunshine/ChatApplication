package com.network;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;

import Decoder.BASE64Decoder;

public class AudioTransmit 
{
	//存储上传图像的路径
	private final String SAVED_DIRECTORY = "C:/Users/USER007/Desktop/IM/data/audio_transportation/";
	//数据库名
	private final String DATABASE_NAME = "JMMSRDB";
	//表名
	private final String TABLE_NAME = "file_transportation";

	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	
	/**
	 * 生成音频文件名称（唯一）
	 * @return 音频文件名称
	 */
	private String generateAudioName(int fromUserID, int toUserID)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		
		return "audio_transportation_fromUserID_" + String.valueOf(fromUserID) + "_toUserID" + String.valueOf(toUserID)
				+ "_" + dateFormat.format(date) + ".amr";
	}
	
	private byte[] string2Byte(String audioBuffer) throws Exception
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(audioBuffer);
		return buffer;
	}
	
	private String saveAudio(String audioBuffer, int fromUserID, int toUserID) throws Exception
	{
		File destDir = new File(SAVED_DIRECTORY); 
		if(!destDir.exists())
            destDir.mkdir();    
		String audioName = generateAudioName(fromUserID, toUserID);
		String audioPath = SAVED_DIRECTORY + audioName;
		FileOutputStream fos = null;
		byte[] buffer = string2Byte(audioBuffer);
		fos = new FileOutputStream(new File(destDir, audioName));
		fos.write(buffer);    
		fos.flush();    
        fos.close(); 
        return audioPath;
	}
	
	private void updateDataBaseWhenUpload(int from_userid, int to_userid, String audioPath)
	{
		
		String[] column = {"from_userid", "to_userid", "file_path"};
	    String[] value = {String.valueOf(from_userid), String.valueOf(to_userid), audioPath};
	    sql.insert(column, value);
	}
	
	/*private String getAudioId(String audioPath)
	{
		String[] query = {"id"};
        String[] condition = {"file_path"};
        String[] conditionVal = {audioPath};
        ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
        return map.get(0).get("id").toString();
	}*/
	
	public String uploadAudio(int from_userid, int to_userid, String audioBuffer) throws Exception
	{	
		/*System.out.println("User [" + from_userid + "] send Audio to User [" + to_userid + "]");
		String audioPath = saveAudio(audioBuffer, from_userid, to_userid);
        updateDataBaseWhenUpload(from_userid, to_userid, audioPath);
        String aduioId = getAudioId(audioPath);
        return Integer.parseInt(aduioId);*/
		
		System.out.println("User [" + from_userid + "] send Audio to User [" + to_userid + "]");
		String audioPath = saveAudio(audioBuffer, from_userid, to_userid);
        updateDataBaseWhenUpload(from_userid, to_userid, audioPath);
        
        
        audioPath = audioPath.replace("C:/Users/USER007/Desktop/IM/data/", "");
        String audioUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + audioPath;
        return audioUrl;
	}
	
	/*
	private String getAudioPath(int audioId)
	{
		String[] query = {"file_path"};
		String[] condition = {"id"};
		String[] conditionVal = {String.valueOf(audioId)};
		ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
		
		System.out.println("map size: " + map.size());
		System.out.println("file path: " + map.get(0).get("file_path").toString());
		
		return map.get(0).get("file_path").toString();
	}
	
	
	private String audio2String(String audioPath) throws Exception
	{
		//@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(audioPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int count = 0;
		while((count = fis.read(buffer)) >= 0)   
		    baos.write(buffer, 0, count);
		String result = new String(new BASE64Encoder().encode(baos.toByteArray())); 
		fis.close();
		baos.close();
		return result;
	}
	public String downloadAudio(int audioId) throws Exception
	{
		String audioPath = getAudioPath(audioId);
		String audioBuffer = audio2String(audioPath);
		
		System.out.println("audioBuffer length: " + audioBuffer.length());
		//System.out.println("audioBuffer: " + audioBuffer);
		
		return audioBuffer;
	}*/
}
