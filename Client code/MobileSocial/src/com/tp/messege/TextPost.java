package com.tp.messege;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.yg.message.ConvertUtil;
import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;


public class TextPost extends AbstractPost
{
	/**
	 * 当前TextPost的内容
	 */
	private String text;

	/**
	 * 新建TextPost时调用<br>
	 * <b>用法参看AbstractPost<br>
	 * @see AbstractPost
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postUserID, String postDate, String text, String location, String sex)
	{
		super(postUserID, postDate, text, location, sex);
		this.text = text;
		publish();
	}

	/**
	 * 从服务器获取已存在的TextPost时调用<br>
	 * <b>用法参看AbstractPost<br>
	 * @see AbstractPost
	 * @param postID
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postID, int postUserID, int likedNumber, String postDate, String text, String location, String sex, int versionCode)
	{
		super(postID, postUserID, likedNumber, postDate, text, location, sex, versionCode);
		this.text = text;
	}

	public String getText()
	{
		return text;
	}
	
	
	/**
	 * 将当前评论发布至服务器<br>
	 * 该函数只应该在{@link #post(int, String, byte[], String)}函数中调用
	 * @return 当前评论发布到服务器后返回的postID<br>
	 * 返回0xffff表示发布失败
	 */
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
			item.put("content", getText());
			item.put("post_type", Integer.toString(getPostType()));
			item.put("location", location);
			item.put("sex", sex);
			items.add(item);
			
			String ps = PackString.arrylist2JsonString("posts", items);
			Object []value = {ps}; 
			String []para = {"postJson"}; 
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
				setPostID(Integer.valueOf(identity));
				ret = identity;
			}
		} 
		catch (JSONException e) 
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

	@Override
	public int getPostType() 
	{
		return 1;
	}
	
	public static void main(String []args)
	{
		TextPost tp = new TextPost(10,"2011-1-1","caonima","beijing","male");
	}
}