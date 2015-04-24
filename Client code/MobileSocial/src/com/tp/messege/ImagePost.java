package com.tp.messege;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.kobjects.base64.Base64;

import com.yg.message.ConvertUtil;
import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;

import android.graphics.Bitmap;
import android.util.Log;

public class ImagePost extends AbstractPost
{
	/**
	 * 当前ImagePost的图像内容
	 * 类型暂定
	 */
	private Bitmap image;

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
		super(postUserID, postDate,new Base64().encode(ConvertUtil.bitmap2Bytes(image)).getBytes(), location, sex);
		this.image = image;
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
		super(postID, postUserID, likedNumber, postDate, ConvertUtil.string2Bytes((imgPath)), location, sex);
	}

	public Bitmap getImage()
	{
		image =  ConvertUtil.bytes2Bitmap(content);
		return image;
	}

	/**
	 * 将当前ImagePost中的Image保存到指定路径
	 * @param savePath 保存路径
	 * @throws IOException 
	 */
	public void downloadImage(String savePath) throws IOException
	{
		String fileName = Integer.toString(this.postUserID) + "_" + this.postDate + ".png";
		FileOutputStream outStream;
		outStream = new FileOutputStream(savePath + fileName);
		outStream.write(content);
	    outStream.close();
	}

	@Override
	public int getPostType()
	{
		return 2;
	}

	@Override
	protected int publish() 
	{
		int ret = 0xffff;
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
			try 
			{
				String buffer = new String(getContent());
				item.put("content", buffer);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
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
			if (Integer.valueOf(s.toString()) == -1)
				ret = 0xffff;
			else
			{
				setPostID(Integer.valueOf(s.toString()));
				ret = Integer.valueOf(s.toString());
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
}