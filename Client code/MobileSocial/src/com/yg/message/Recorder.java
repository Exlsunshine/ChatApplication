package com.yg.message;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class Recorder
{ 
	private final static int SUCCESS = 1000;
	private final static int E_NOSDCARD = 1001;
	private final static int E_STATE_RECODING = 1002;
	private final static int E_UNKOWN = 1003;
	
    //��Ƶ����-��˷�
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //����Ƶ�� 44100��Ŀǰ�ձ�ʹ�õ�Ƶ�ʱ�׼������ĳЩ�豸��Ȼ֧��22050 16000 11025
    public final static int AUDIO_SAMPLE_RATE = 44100;
    //¼������ļ�
    public final static String AUDIO_AMR_FILENAME = "FinalAudio.amr";
	
    private boolean isRecord = false;
    private MediaRecorder mMediaRecorder = null;
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
    
    /**
     * ��ȡ�ļ���С
     * @return
     */
    public long getRecordFileSize()
    {
    	File mFile = new File(getAMRFilePath());
    	if(!mFile.exists())
    		return -1;
    	return mFile.length();
    }

    private void createMediaRecord()
    {
        mMediaRecorder = new MediaRecorder();
      
        //������˷�
        mMediaRecorder.setAudioSource(AUDIO_INPUT);
         
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
    public String getAMRFilePath()
    {
        String mAudioAMRPath = "";
        if(isSdcardExist())
        {
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            System.out.println(fileBasePath);
            //String fileBasePath ="/mnt/sdcard/TestMobileDatabase/voice/";
            File forlder = new File(fileBasePath);
    		if (!forlder.exists())
    			forlder.mkdir();
            
            mAudioAMRPath = fileBasePath + AUDIO_AMR_FILENAME;
        }
        return mAudioAMRPath;
    } 
}