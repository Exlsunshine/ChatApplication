package com.tp.messege;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.kobjects.base64.Base64;

import com.yg.message.ConvertUtil;
import com.yg.user.DownloadManager;
import com.yg.user.PackString;
import com.yg.user.UploadManager;
import com.yg.user.WebServiceAPI;

import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;

public class ImagePost extends AbstractPost
{
	/**
	 * 当前ImagePost的图像内容
	 * 类型暂定
	 */
	private Bitmap image;
	private String imgPath;
	private String imgUrl;
	private final static String SAVED_DIRECTORY = "/sdcard/tp/";
	private File imgFile;
	/**
	 * 新建ImagePost时调用<br>
	 * <b>用法参看AbstractPost<br>
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
		imgPath = SAVED_DIRECTORY + generateImageName(postUserID);
		this.image = image;
		
		try 
		{
			FileOutputStream out = new FileOutputStream(imgPath);
			image.compress(Bitmap.CompressFormat.PNG, 90, out);
		} 
		catch (IOException ex) 
		{
			System.out.println(ex);
		}
		
		
		publish();
	}

	/**
	 * 从服务器获取已存在的ImagePost时调用<br>
	 * <b>用法参看AbstractPost<br>
	 * @see AbstractPost
	 * @param postID
	 * @param postUserID
	 * @param postDate
	 * @param imgPath
	 * @param location
	 */
	public ImagePost(int postID, int postUserID, int likedNumber, String postDate, String imgPath, String location,String sex)
	{
		super(postID, postUserID, likedNumber, postDate, imgPath, location, sex);
		imgUrl = imgPath;
	}

	public Bitmap getImage()
	{
		if (image == null)
		{
			DownloadManager dm = new DownloadManager(imgUrl);
			image = dm.getBmpFile();
		}
		return image;
	}

/*	*//**
	 * 将当前ImagePost中的Image保存到指定路径
	 * @param savePath 保存路径
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
			/*try 
			{
				String buffer = new String(getContent());
				item.put("content", buffer);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}*/
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

	/**
	 * 设置{@link #publish()}返回的commentID
	 */
	protected void setPostID(int PostIDRtn)
	{
		postID = PostIDRtn;
	}
	
	private String generateImageName(int postUserID)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date date = new Date();
		return "picture_transportation_postUserID_" + String.valueOf(postUserID) + "_" + dateFormat.format(date)  + ".jpg";
	}
}