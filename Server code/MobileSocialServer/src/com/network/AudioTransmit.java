package com.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;
import com.lj.statistics.UserStatistics;

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
	
	private void updateDataBaseWhenUpload(int from_userid, int to_userid, String audioPath)
	{
		
		String[] column = {"from_userid", "to_userid", "file_path"};
	    String[] value = {String.valueOf(from_userid), String.valueOf(to_userid), audioPath};
	    sql.insert(column, value);
	}
	
	public String uploadAudio(int from_userid, int to_userid) throws Exception
	{	
		System.out.println("User [" + from_userid + "] send Audio to User [" + to_userid + "]");
		
		String audioName = generateAudioName(from_userid, to_userid);
		String audioPath = SAVED_DIRECTORY + audioName;
        updateDataBaseWhenUpload(from_userid, to_userid, audioPath);
        
        audioPath = audioPath.replace("C:/Users/USER007/Desktop/IM/data/", "");
        String audioUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + audioPath;
        {
        	//LJ
        	UserStatistics userStatistics = new UserStatistics();
			userStatistics.increaseStatistic(from_userid, ConstantValues.InstructionCode.STATISTICS_VOICE_NUM_TYPE);
        }
        return audioUrl;
	}
}