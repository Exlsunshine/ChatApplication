package com.yg.message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.yg.commons.ConstantValues;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

/**
 * �������͵���Ϣ���̳���{@link AbstractMessage}��<br>
 * ��Ҫ��{@link AbstractMessage}�еķ�����д
 * @author EXLsunshine
 *
 */
public class AudioMessage extends AbstractMessage
{
	private static String DEBUG_TAG = "______AudioMessage";
	private static String AUDIO_CACHE_DIR = Environment.getExternalStorageDirectory() + "/MobileSocial/audio/";//"/mnt/sdcard/MobileSocial/audio/";
	private MediaPlayer mPlayer;
	private Context context;
	private byte [] audio;
	private String audioPath = null;
	private long audioLength = -1;
	
	public AudioMessage(int msgID, int fromID, int toID, byte [] content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO;
		this.isRead = isRead;
		this.id = msgID;
		this.audio = content;
	}
	
	public AudioMessage(int fromID, int toID, byte [] content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO;
		this.isRead = isRead;
		this.audio = content;
	}
	
	/**
	 * ������Ƶ��Ϣ��ʱ�䳤��
	 * @return ��Ƶʱ�䳤��
	 */
	public long getDuration()
	{
		if (audioLength == -1)
		{
			if (audioPath == null)
				getAudioPath();
			
			File audioCacheFile = new File(audioPath);
			if (audioCacheFile.isFile() && audioCacheFile.exists())
			{
				try 
				{
					audioLength = getAmrDuration(audioCacheFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return audioLength;
	}
	
	private long getAmrDuration(File file) throws IOException
	{  
        long duration = -1;  
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };  
       
        RandomAccessFile randomAccessFile = null;  
        try
        {  
            randomAccessFile = new RandomAccessFile(file, "rw");  
            long length = file.length();//�ļ��ĳ���  
            System.out.println(length);
            int pos = 6;//���ó�ʼλ��  
            int frameCount = 0;//��ʼ֡��  
            int packedPos = -1;  

            byte[] datas = new byte[1];//��ʼ����ֵ  
            while (pos <= length)
            {  
                randomAccessFile.seek(pos);  
                if (randomAccessFile.read(datas, 0, 1) != 1)
                {  
                    duration = length > 0 ? ((length - 6) / 650) : 0;  
                    break;  
                }  
                packedPos = (datas[0] >> 3) & 0x0F;  
                pos += packedSize[packedPos] + 1;  
                frameCount++;  
            }  

            duration = frameCount * 20 / 1000;
        }
        finally
        {  
            if (randomAccessFile != null)
                randomAccessFile.close();  
        }  
        return duration;  
    }  
	
	/**
	 * ������Ƶ�ļ�·��
	 * @return null ��ʾ�ļ���ȡ����<br>
	 * ��null ��ʾ��ȡ�ļ��ɹ�
	 */
	public String getAudioPath()
	{
		if (audioPath != null)
		{
			File audioCacheFile = new File(audioPath);
			if (audioCacheFile.isFile() && audioCacheFile.exists())
				return audioPath;
		}
		
		try
		{
			generateAudioCache();
		} catch (IOException e) {
			audioPath = null;
			e.printStackTrace();
		}
		return audioPath;
	}
	
	private void generateAudioCache() throws IOException
	{
		// create temp file that will hold byte array
		File dir = new File(AUDIO_CACHE_DIR);
		if (!dir.exists())
			if (!dir.mkdirs())
				Log.e("DEBUG_TAG", "Make audio cache dir failed.");
		Log.i(DEBUG_TAG, "Cache dir is " + dir.getAbsolutePath());
		
        File tempMp3 = File.createTempFile("audio_" + String.valueOf(fromUserID)
        		+ "_" + String.valueOf(toUserID) + "_" + getDate(), ".amr", dir);
        audioPath = tempMp3.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(tempMp3);
        fos.write(audio);
        fos.close();
	}
	
	/**
	 * ���Ŵ�����Ϣ�е�����
	 */
	public void play(Context context)
	{
		this.context = context;
		try {
			playFromBytes(this.audio, this.context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void playFromBytes(byte [] bytes, Context context) throws IOException
	{
		File audioFile = ConvertUtil.bytes2AmrFile(bytes, context);
		
		mPlayer = new MediaPlayer();
		mPlayer.setDataSource(audioFile.getPath());
		mPlayer.prepare();
		mPlayer.start();
	}
	
	/**
	 * ֹͣ���Ŵ�����Ϣ�е�����
	 */
	public void stop()
	{
		mPlayer.release();  
        mPlayer = null;
	}

	@Override
	public int getFromUserID() { return fromUserID; }

	@Override
	public int getToUserID() { return toUserID; }

	@Override
	public byte [] getContent() { return audio; }

	@Override
	public int getMessageType() { return ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO; }

	@Override
	public String getDate() { return date; }

	@Override
	public boolean hasBeenRead() { return isRead; }

	@Override
	public void setAsRead() { this.isRead = true; }

	@Override
	public int getID() { return id; }
	
	@Override
	public void setID(int id) { this.id = id; }
}