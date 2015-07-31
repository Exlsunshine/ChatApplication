package com.tp.messege;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.yg.user.DownloadManager;
import com.yg.user.PackString;
import com.yg.user.UploadManager;
import com.yg.user.WebServiceAPI;

import android.graphics.Bitmap;
import android.util.Log;

public class ImagePost extends AbstractPost
{
	/**
	 * ��ǰImagePost��ͼ������
	 * �����ݶ�
	 */
	private String imgPath = null;
	private String imgUrl = null;
	private final static String SAVED_DIRECTORY = "/JMMSR/FRIEND_CIRCLE/";
	/**
	 * �½�ImagePostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
	 * @see AbstractPost
	 * @param postUserID
	 * @param postDate
	 * @param image
	 * @param location
	 * @throws Exception 
	 */
	public ImagePost(int postUserID, String postDate, Bitmap image, String location, String sex) 
	{
		super(postUserID, postDate,image, location, sex);
		String imgPathTmp = android.os.Environment.getExternalStorageDirectory() + "/" + SAVED_DIRECTORY + "/" +  generateImageName(postUserID, postID);
		
		try 
		{
			File file = new File(imgPathTmp);
			file.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(imgPathTmp);
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
			imgPath = imgPathTmp;
		} 
		catch (IOException ex) 
		{
			System.out.println(ex);
		}
		publish();
	}

	/**
	 * �ӷ�������ȡ�Ѵ��ڵ�ImagePostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
	 * @see AbstractPost
	 * @param postID
	 * @param postUserID
	 * @param postDate
	 * @param imgPath
	 * @param location
	 */
	public ImagePost(int postID, int postUserID, int likedNumber, String postDate, String imgPath, String location,String sex, int versionCode)
	{
		super(postID, postUserID, likedNumber, postDate, imgPath, location, sex, versionCode);
		imgUrl = imgPath;
	}


/*	*//**
	 * ����ǰImagePost�е�Image���浽ָ��·��
	 * @param savePath ����·��
	 * @throws IOException 
	 *//*
	public void downloadImage(String savePath) throws IOException
	{
		String fileName = Integer.toString(this.postUserID) + "_" + this.postDate + ".png";
		FileOutputStream outStream;
		outStream = new FileOutputStream(savePath + fileName);
		outStream.write(content);
	    outStream.close();
	}*/

	@Override
	public int getPostType()
	{
		return 2;
	}
	
	
	@Override
	protected int publish() 
	{
		int ret = 0xffff;
		int identity = -1;
		String imgURL = "";
		try 
		{
			WebServiceAPI wsApi;
			String packageName = "FriendCircleServer.tp.com";
			String className = "FriendCircleHandler";
			
			ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> item = new HashMap<String, Object>();
			
			item.put("post_user_id", Integer.toString(postUserID));
			item.put("liked_number", Integer.toString(getLikedNumber()));
			item.put("post_date", getPostDate());
			item.put("post_type", Integer.toString(getPostType()));
			item.put("location", location);
			item.put("sex", sex);
			items.add(item);
			
			String ps = PackString.arrylist2JsonString("posts", items);
			Log.e("debug_____________",ps);
			String []para = {"postJson"}; 
			Object []value = {ps}; 
			wsApi = new WebServiceAPI(packageName, className);
			Object s = wsApi.callFuntion("publishPost", para ,value);
			System.out.print(s.toString());
			
			PackString jsonString = new PackString(s.toString());
			ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("publishpost");
			
			for (int i = 0; i < postResult.size(); i++)
			{
				HashMap<String, Object> map = postResult.get(i);
				identity = Integer.parseInt(map.get("identityNUM").toString());
				imgURL = map.get("imgURL").toString();
			}
			if (identity == -1)
				ret = 0xffff;
			else
			{
				setPostID(identity);
				Log.i("publishdebug__________", "Uploading image to " + imgURL);
				String serverIP = imgURL.substring(imgURL.indexOf("http://") + 7, imgURL.indexOf(':', 7));
				Log.e("debug_____________", serverIP);
				String newFileName = imgURL.substring(imgURL.indexOf('/', 7) + 1, imgURL.length());
				Log.e("debug_____________", newFileName);
				String serverPort = imgURL.substring(imgURL.indexOf(':', 5) + 1, imgURL.indexOf(':', 5) + 5);
				Log.e("debug_____________", serverPort);
				File file = new File(imgPath);
				Log.e("debug_____________", imgPath);
				UploadManager um = new UploadManager(file, newFileName, serverIP, serverPort);
				um.excecuteUpload();
				ret = identity;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getImagePath()
	{
		if (imgPath == null)
 		{
 			DownloadManager dm = new DownloadManager(imgUrl);
			Bitmap image = dm.getBmpFile();
			String imgPathTmp = android.os.Environment.getExternalStorageDirectory() + "/" + SAVED_DIRECTORY + "/" + generateImageName(postUserID, postID);
			try 
			{
				File file = new File(imgPathTmp);
				file.getParentFile().mkdirs();
				FileOutputStream out = new FileOutputStream(imgPathTmp);
				image.compress(Bitmap.CompressFormat.PNG, 90, out);
				imgPath = imgPathTmp;
			} 
			catch (IOException ex) 
			{
				System.out.println(ex);
			}
 		}
		return imgPath;
	}
	
	public boolean isImgPathEmpty()
	{
		if (imgPath == null)
			return true;
		else
			return false;
	}
	
	public String getImageURL()
	{
		return this.imgUrl;
	}
	
	public void setImagePath(String path)
	{
		this.imgPath = path;
	}
	/**
	 * ����{@link #publish()}���ص�commentID
	 */
	protected void setPostID(int PostIDRtn)
	{
		postID = PostIDRtn;
	}
	
	private String generateImageName(int postUserID, int postID)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date date = new Date();
		return "picture_transportation_postUserID_" + String.valueOf(postUserID) + "_" +  String.valueOf(postID) + "_"  + dateFormat.format(date) + ".jpg";	
	}
}