package com.file.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.database.SQLServerEnd;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * LJ
 */
public class ImageTransmit 
{
	//�洢�ϴ�ͼ���·��
	private final String Dir_PATH = "D:\\image\\";
	//���ݿ���
	private final String DATABASE_NAME = "StrangerDB";
	//����
	private final String TABLE_NAME = "picture_transportation";

	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);

	/**
	 * ����ͼ�����ƣ�Ψһ��
	 * @return ͼ������
	 */
	private String generateImageName()
	{
		return "b.jpg";
	}
	
	
	private byte[] string2Byte(String imageBuffer) throws Exception
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(imageBuffer);
		return buffer;
	}
	/**
	 * ���ַ���ת��file�������ڱ���
	 * @param imageBuffer �������ַ���
	 * @return ����·��
	 * @throws Exception
	 */
	private String saveImage(String imageBuffer) throws Exception
	{
		File destDir = new File(Dir_PATH); 
		if(!destDir.exists())
            destDir.mkdir();    
		String imageName = generateImageName();
		String imagePath = Dir_PATH + imageName;
		FileOutputStream fos = null;
		byte[] buffer = string2Byte(imageBuffer);
		fos = new FileOutputStream(new File(destDir, imageName));
		fos.write(buffer);    
		fos.flush();    
        fos.close(); 
        return imagePath;
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
	 * ���ͼ��id
	 * @param imagePath ͼ��·��
	 * @return ͼ��id
	 */
	private String getImageId(String imagePath)
	{
		String[] query = {"id"};
        String[] condition = {"pic_path"};
        String[] conditionVal = {imagePath};
        ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
        return map.get(0).get("id").toString();
	}
	
	/**
	 * �ϴ�ͼ���������ͻ��˵���
	 * @param from_userid ���ͷ�id
	 * @param to_userid ���շ�id
	 * @param imageBuffer ͼ���ַ���������
	 * @return �ϴ���ͼ����ͼ�����ݿ��е�ͼ��id
	 * @throws Exception
	 */
	public int uploadImage(int from_userid, int to_userid, String imageBuffer) throws Exception
	{	
		System.out.println("User [" + from_userid + "] send Image to User [" + to_userid + "]");
		String imagePath = saveImage(imageBuffer);
        updateDataBaseWhenUpload(from_userid, to_userid, imagePath);
        String imageId = getImageId(imagePath);
        return Integer.parseInt(imageId);
        
	}
	
	/**
	 * ����ͼ��id��ø�ͼ��ı���·��
	 * @param imageId ͼ��id
	 * @return ����·��
	 */
	private String getImagePath(int imageId)
	{
		String[] query = {"pic_path"};
		String[] condition = {"id"};
		String[] conditionVal = {String.valueOf(imageId)};
		ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
		return map.get(0).get("pic_path").toString();
	}
	
	/**
	 * ��ͼ��·������ȡͼ���ļ�����ת�����ַ���
	 * @param imagePath ͼ��·��
	 * @return ת����
	 * @throws Exception
	 */
	public static String image2String(String imagePath) throws Exception
	{
		//@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(imagePath);
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
	
	/**
	 * ����ͼ�񣬱��ͻ��˵���
	 * @param imageId �����ص�ͼ��id
	 * @return ͼ���ַ���
	 * @throws Exception
	 */
	public String downloadImage(int imageId) throws Exception
	{
		String imagePath = getImagePath(imageId);
		String imageBuffer = image2String(imagePath);
		return imageBuffer;
	}
}