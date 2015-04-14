package com.yg.message;

import android.os.Environment;

public class FileNameGenerator
{
	/**
	 * ����һ���ļ���
	 * @param subDir ����·����Ĭ��Ӧ����'/MobileSocial/image/' �� '/MobileSocial/audio/'
	 * @param fromUserID ������ID
	 * @param toUserID ������ID
	 * @param suffix �ļ�����׺��Ĭ��Ӧ����'amr' �� 'jpg'
	 * @return ���ɺõ��ļ���������Ч����:<br>/storage/emulated/0/<b>MobileSocial/audio/</b>fromUser_<b>4</b>_toUser_<b>5</b>_at_1429012977830.<b>amr</b>
	 */
	public synchronized static String getFileName(String subDir, int fromUserID, int toUserID, String suffix)
	{
		String fileName = null;
		
		fileName = Environment.getExternalStorageDirectory() + subDir
				+ String.format("fromUser_%d_toUser_%d_at_%d.%s", fromUserID, toUserID, System.currentTimeMillis(), suffix);
		
		
		return fileName;
	}
}