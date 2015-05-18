package com.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;

/**
 * LJ
 */
public class ImageTransmit 
{
	//�洢�ϴ�ͼ���·��
	private final String SAVED_DIRECTORY = "D:/Data/IM/data/image_transportation/";
	//���ݿ���
	private final String DATABASE_NAME = "JMMSRDB";
	//����
	private final String TABLE_NAME = "picture_transportation";
	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);

	/**
	 * ����ͼ�����ƣ�Ψһ��
	 * @return ͼ������
	 */
	private String generateImageName(int fromUserID, int toUserID)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		
		return "picture_transportation_fromUserID_" + String.valueOf(fromUserID) + "_" + String.valueOf(toUserID)
				+ "_" + dateFormat.format(date) + ".jpg";
	}
	
	/**
	 * ����ͼ���������ݿ�
	 * @param from_userid ���ͷ�id
	 * @param to_userid ���շ�id
	 * @param imagePath ͼ�񱣴�·��
	 */
	private void updateDataBaseWhenUpload(int from_userid, int to_userid, String imagePath)
	{
		String[] column = {"from_userid", "to_userid", "pic_path"};
	    String[] value = {String.valueOf(from_userid), String.valueOf(to_userid), imagePath};
	    sql.insert(column, value);
	}
	
	/**
	 * �ϴ�ͼ���������ͻ��˵���
	 * @param from_userid ���ͷ�id
	 * @param to_userid ���շ�id
	 * @param imageBuffer ͼ���ַ���������
	 * @return �ϴ���ͼ����ͼ�����ݿ��е�ͼ��id
	 * @throws Exception
	 */
	public String uploadImage(int from_userid, int to_userid) throws Exception
	{	
		System.out.println("User [" + from_userid + "] send Image to User [" + to_userid + "]");

		String imageName = generateImageName(from_userid, to_userid);
		String imagePath = SAVED_DIRECTORY + imageName;
		updateDataBaseWhenUpload(from_userid, to_userid, imagePath);

        imagePath = imagePath.replace("D:/Data/IM/data/", "");
        String imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
        return imageUrl;
	}
}