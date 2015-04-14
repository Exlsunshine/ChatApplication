package com.yg.message;

import android.os.Environment;

public class FileNameGenerator
{
	/**
	 * 生成一个文件名
	 * @param subDir 保存路径，默认应填入'/MobileSocial/image/' 或 '/MobileSocial/audio/'
	 * @param fromUserID 发送者ID
	 * @param toUserID 接收者ID
	 * @param suffix 文件名后缀，默认应填入'amr' 或 'jpg'
	 * @return 生成好的文件名，最终效果如:<br>/storage/emulated/0/<b>MobileSocial/audio/</b>fromUser_<b>4</b>_toUser_<b>5</b>_at_1429012977830.<b>amr</b>
	 */
	public synchronized static String getFileName(String subDir, int fromUserID, int toUserID, String suffix)
	{
		String fileName = null;
		
		fileName = Environment.getExternalStorageDirectory() + subDir
				+ String.format("fromUser_%d_toUser_%d_at_%d.%s", fromUserID, toUserID, System.currentTimeMillis(), suffix);
		
		
		return fileName;
	}
}