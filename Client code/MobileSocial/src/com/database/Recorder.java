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
	
    //��Ƶ����-��˷�
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //����Ƶ��
    //44100��Ŀǰ�ı�׼������ĳЩ�豸��Ȼ֧��22050��16000��11025
    public final static int AUDIO_SAMPLE_RATE = 44100;  //44.1KHz,�ձ�ʹ�õ�Ƶ��   
    //¼������ļ�
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
        //�ж��Ƿ����ⲿ�洢�豸sdcard
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
                    // ��¼��״̬Ϊtrue  
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
         /* Initial��ʵ����MediaRecorder���� */
        mMediaRecorder = new MediaRecorder();
         
        /* setAudioSource/setVedioSource*/
        mMediaRecorder.setAudioSource(AUDIO_INPUT);//������˷�
         
        /* ��������ļ��ĸ�ʽ��THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp��ʽ��H263��Ƶ/ARM��Ƶ����)��MPEG-4��RAW_AMR(ֻ֧����Ƶ����Ƶ����Ҫ��ΪAMR_NB)
         */
         mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
          
         /* ������Ƶ�ļ��ı��룺AAC/AMR_NB/AMR_MB/Default */
         mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
          
         /* ��������ļ���·�� */
         File file = new File(getAMRFilePath());
         if (file.exists()) 
             file.delete();
         
         mMediaRecorder.setOutputFile(getAMRFilePath());
    }
    
    /**
     * �ж��Ƿ����ⲿ�洢�豸sdcard
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
     * ��ȡ������AMR��ʽ��Ƶ�ļ�·��
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
     * ��ȡ�ļ���С
     * @param path,�ļ��ľ���·��
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