package com.database;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

/**
 * manifest permission <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				 	    <uses-permission android:name="android.permission.RECORD_AUDIO" />  
				    	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />  
				    	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * @author EXLsunshine
 *	
 */
public class Recorder
{ 
	private final static int SUCCESS = 1000;
	private final static int E_NOSDCARD = 1001;
	private final static int E_STATE_RECODING = 1002;
	private final static int E_UNKOWN = 1003;
	
    //音频输入-麦克风
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 44100;  //44.1KHz,普遍使用的频率   
    //录音输出文件
    public final static String AUDIO_AMR_FILENAME = "FinalAudio.amr";
	
    private boolean isRecord = false;
    private MediaRecorder mMediaRecorder;
    private Recorder() { }
    private static Recorder mInstance;

    public synchronized static Recorder getInstance()
    {
        if(mInstance == null)
            mInstance = new Recorder();
        return mInstance;
    }
     
    public int startRecordAndFile()
    {
        //判断是否有外部存储设备sdcard
        if(isSdcardExist())
        {
            if(isRecord)
                return E_STATE_RECODING;
            else
            {
                if(mMediaRecorder == null)
                    createMediaRecord();
                 
                try
                {
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    // 让录制状态为true  
                    isRecord = true;
                    return SUCCESS;
                }catch(IOException ex){
                    ex.printStackTrace();
                    return E_UNKOWN;
                }
            }
        }       
        else
            return E_NOSDCARD;            
    }
     
     
    public void stopRecordAndFile()
    {
    	if (mMediaRecorder != null) 
        {  
            System.out.println("Stop record");  
            isRecord = false;
            mMediaRecorder.stop();  
            mMediaRecorder.release();  
            mMediaRecorder = null;
        }  
    }
     
    public long getRecordFileSize()
    {
        return getFileSize(getAMRFilePath());
    }
     
     
    private void createMediaRecord()
    {
         /* Initial：实例化MediaRecorder对象 */
        mMediaRecorder = new MediaRecorder();
         
        /* setAudioSource/setVedioSource*/
        mMediaRecorder.setAudioSource(AUDIO_INPUT);//设置麦克风
         
        /* 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */
         mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
          
         /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
         mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
          
         /* 设置输出文件的路径 */
         File file = new File(getAMRFilePath());
         if (file.exists()) 
             file.delete();
         
         mMediaRecorder.setOutputFile(getAMRFilePath());
    }
    
    /**
     * 判断是否有外部存储设备sdcard
     * @return true | false
     */
    private boolean isSdcardExist()
    {       
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
    
    /**
     * 获取编码后的AMR格式音频文件路径
     * @return
     */
    private String getAMRFilePath()
    {
        String mAudioAMRPath = "";
        if(isSdcardExist())
        {
            //String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileBasePath ="/mnt/sdcard/TestMobileDatabase/voice/";
            File forlder = new File(fileBasePath);
    		if (!forlder.exists())
    			forlder.mkdir();
            
            mAudioAMRPath = fileBasePath + AUDIO_AMR_FILENAME;
        }
        return mAudioAMRPath;
    } 
    
    /**
     * 获取文件大小
     * @param path,文件的绝对路径
     * @return
     */
    private long getFileSize(String path)
    {
        File mFile = new File(path);
        if(!mFile.exists())
            return -1;
        return mFile.length();
    }
}