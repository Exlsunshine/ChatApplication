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
	 * ��ǰTextPost������
	 */
	private String text;

	/**
	 * �½�TextPostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
	 * @see AbstractPost
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postUserID, String postDate, String text, String location, String sex)
	{
		super(postUserID, postDate, text.getBytes(), location, sex);
		this.text = text;
		publish();
	}

	/**
	 * �ӷ�������ȡ�Ѵ��ڵ�TextPostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
	 * @see AbstractPost
	 * @param postID
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postID, int postUserID, int likedNumber, String postDate, String text, String location, String sex)
	{
		super(postID, postUserID, likedNumber, postDate, ConvertUtil.string2Bytes((text)), location, sex);
	}

	public String getText()
	{
		text = new String(content);
		return text;
	}
	
	
	/**
	 * ����ǰ���۷�����������<br>
	 * �ú���ֻӦ����{@link #post(int, String, byte[], String)}�����е���
	 * @return ��ǰ���۷������������󷵻ص�postID<br>
	 * ����0xffff��ʾ����ʧ��
	 */
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
			if (Integer.valueOf(s.toString()) == -1)
				ret = 0xffff;
			else
			{
				setPostID(Integer.valueOf(s.toString()));
				ret = Integer.valueOf(s.toString());
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * ����{@link #publish()}���ص�commentID
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