package com.tp.messege;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.yg.message.ConvertUtil;
import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;

import android.graphics.Bitmap;
import android.util.Log;

public class PostManager 
{
	private int userID;
	private ArrayList<AbstractPost> friendPosts;
	private ArrayList<AbstractPost> myselfPosts;
	private int currentLatestPostID;
	private int currentOldestPostID;
	private final String packageName = "FriendCircleServer.tp.com";
	private final String className = "FriendCircleHandler";
	private TextPost tp;
	
	public PostManager(int userID)
	{
		this.userID = userID;
		friendPosts = new ArrayList<AbstractPost>();
		myselfPosts = new ArrayList<AbstractPost>();
		currentLatestPostID = 0;
		currentOldestPostID = 0;
	}
	public ArrayList<AbstractPost> get10Posts()
	{
		ArrayList<AbstractPost> postArrayList = new ArrayList<AbstractPost>();
		if (friendPosts.size() != 0)
		{
			currentLatestPostID = friendPosts.get(0).getPostID();
			currentOldestPostID = friendPosts.get(friendPosts.size() - 1).getPostID();
		}
		WebServiceAPI wsApi;
		Log.d("getLatestPostsdebug_______", currentLatestPostID + "");
		Object []value = {userID}; 
		String []para = {"userID"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("get10Posts", para ,value);
		if (s == null)
			return null;
		PackString jsonString = new PackString(s.toString());
		ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("10postsFromServer");
		Log.d("get10Postsdebug_______", postResult.size() + "__________");
		for (int i = 0; i < postResult.size(); i++)
		{
			try 
			{
				int postIDFromServer = Integer.parseInt(postResult.get(i).get("id").toString());
				int postUserID = Integer.parseInt(postResult.get(i).get("post_user_id").toString());
				int likedNumber = Integer.parseInt(postResult.get(i).get("liked_number").toString()); 
				String postDate = postResult.get(i).get("post_date").toString();
				int postType = Integer.parseInt(postResult.get(i).get("post_type").toString());
				String location = postResult.get(i).get("location").toString();
				String sex = postResult.get(i).get("sex").toString();
				String content = (postResult.get(i).get("content").toString());
				if (postType == 1) //文字
				{
					TextPost tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex);
					friendPosts.add(tp);
					postArrayList.add(tp);
				}
				else if (postType == 2) //图片
				{
					ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex);
					Log.e("get10Posts", ConvertUtil.bytes2String(ip.getContent()));
					friendPosts.add(ip);
					postArrayList.add(ip);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		Collections.sort(friendPosts, new SortByPostDate());
		return friendPosts;
	}
	/**
	 * 从服务器获得最新的 N + 10条Post
	 * 
	 * @return 最新Post的ArrayList集合
	 */
	public ArrayList<AbstractPost> getLatestPosts()
	{
		if (friendPosts.size() != 0)
		{
			currentLatestPostID = friendPosts.get(0).getPostID();
			currentOldestPostID = friendPosts.get(friendPosts.size() - 1).getPostID();
		}
		WebServiceAPI wsApi;
		Object []value = {userID, currentLatestPostID, currentOldestPostID, friendPosts.size() + 10}; 
		String []para = {"userID", "postIDlatest","postIDoldest","num"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("getLatestPosts", para ,value);
		if (s == null)
			return null;
		PackString jsonString = new PackString(s.toString());
		ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("latestpostsFromServer");
		Log.d("getLatestPostsdebug_______", postResult.size() + "__________");
		for (int i = 0; i < postResult.size(); i++)
		{
			try 
			{
				final int postIDFromServer = Integer.parseInt(postResult.get(i).get("id").toString());
				final int postUserID = Integer.parseInt(postResult.get(i).get("post_user_id").toString());
				final int likedNumber = Integer.parseInt(postResult.get(i).get("liked_number").toString()); 
				final String postDate = postResult.get(i).get("post_date").toString();
				final int postType = Integer.parseInt(postResult.get(i).get("post_type").toString());
				final String location = postResult.get(i).get("location").toString();
				final String sex = postResult.get(i).get("sex").toString();
				final String content = (postResult.get(i).get("content").toString());
				boolean isSame = false;
				if (postType == 1) //文字
				{
					Thread td = new Thread(new Runnable()
			        {
						@Override
			        	public void run() 
			        	{
			        		try
			        		{
			        			tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex);
			        			Log.e("getLatestPosts","创建post" + "" + tp.getPostID());
			        		}
			        		catch (Exception e)
			                {
			        			e.printStackTrace();
			                }
			        	}
			        });
					td.start();
					try 
					{
						td.join();
						int j = 0;
						Log.e("getLatestPosts","替换post");
						for (j = 0; j < friendPosts.size(); j++)
						{
							if (friendPosts.get(j).getPostID() == tp.getPostID())  //已经存在的POST
							{
								isSame = true;
								Log.e("getLatestPosts", "替换" + friendPosts.get(j).getcommentsize() + " " + tp.getcommentsize());
								friendPosts.set(j, tp);         //替换
								break;
							}
						}
						if (j == friendPosts.size() && isSame == false)
						{
							friendPosts.add(tp);
							Log.e("getLatestPosts", "添加" + j);
						}
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					
				}
				else if (postType == 2) //图片
				{
					ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex);
					int j = 0;
					for (j = 0; j < friendPosts.size(); j++)
					{
						if (friendPosts.get(j).getPostID() == ip.getPostID())  //已经存在的POST
						{
							isSame = true;
							Log.e("getLatestPosts", "替换" + friendPosts.get(j).getcommentsize() + " " + ip.getcommentsize());
							friendPosts.set(j, ip);         //替换
							break;
						}
					}
					if (j == friendPosts.size() && isSame == false)
					{
						friendPosts.add(ip);
						Log.e("getLatestPosts", "添加" + j);
					}
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		Collections.sort(friendPosts, new SortByPostDate());
		return friendPosts;
	}
	/**
	 * 从服务器获得用户自己与用户好友的10条历史Post
	 * 
	 * @return 户自己与用户好友的历史Post的ArrayList集合
	 */
	public ArrayList<AbstractPost> getHistoryPosts()
	{
		ArrayList<AbstractPost> postArrayList = new ArrayList<AbstractPost>();
		WebServiceAPI wsApi;
		if (friendPosts.size() != 0)
		{
			currentLatestPostID = friendPosts.get(0).getPostID();
			currentOldestPostID = friendPosts.get(friendPosts.size() - 1).getPostID();
		}
		Object []value = {userID, currentOldestPostID}; 
		String []para = {"userID", "postID"};
		Log.d("getHistoryPostssdebug_______", friendPosts.size() + " " + currentOldestPostID + "");
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("getHistoryPosts", para ,value);
		if (s == null)
			return null;
		PackString jsonString = new PackString(s.toString());
		ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("historypostsFromServer");
		for (int i = 0; i < postResult.size(); i++)
		{
			try 
			{
				int postIDFromServer = Integer.parseInt(postResult.get(i).get("id").toString());
				int postUserID = Integer.parseInt(postResult.get(i).get("post_user_id").toString());
				int likedNumber = Integer.parseInt(postResult.get(i).get("liked_number").toString()); 
				String postDate = postResult.get(i).get("post_date").toString();
				int postType = Integer.parseInt(postResult.get(i).get("post_type").toString());
				String location = postResult.get(i).get("location").toString();
				String sex = postResult.get(i).get("sex").toString();
				String content = (postResult.get(i).get("content").toString());
				if (postType == 1) //文字
				{
					TextPost tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex);
					friendPosts.add(tp);
					postArrayList.add(tp);
				}
				else if (postType == 2) //图片
				{
					ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex);
					friendPosts.add(ip);
					postArrayList.add(ip);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		Collections.sort(friendPosts, new SortByPostDate());
		return friendPosts;
	}
	/**
	 * 从服务器获得用户自己的的Post
	 * 
	 * @return 用户自己的的Post的ArrayList集合
	 */
	public ArrayList<AbstractPost> getMyselfPosts()
	{
		if (myselfPosts.size() != 0)
			return myselfPosts;
		ArrayList<AbstractPost> postArrayList = new ArrayList<AbstractPost>();
		WebServiceAPI wsApi;
		Object []value = {userID}; 
		String []para = {"userID"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("getMyselfPosts", para ,value);
		if (s == null)
		{
			Log.d("getMyselfPosts__", "MyselfPosts is null");
			return null;
		}
		else
			Log.d("getMyselfPosts__", s.toString());
		PackString jsonString = new PackString(s.toString());
		ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("userhimselfpost");
		myselfPosts.clear();
		for (int i = 0; i < postResult.size(); i++)
		{
			try 
			{
				int postIDFromServer = Integer.parseInt(postResult.get(i).get("id").toString());
				int postUserID = Integer.parseInt(postResult.get(i).get("post_user_id").toString());
				int likedNumber = Integer.parseInt(postResult.get(i).get("liked_number").toString()); 
				String postDate = postResult.get(i).get("post_date").toString();
				int postType = Integer.parseInt(postResult.get(i).get("post_type").toString());
				String location = postResult.get(i).get("location").toString();
				String sex = postResult.get(i).get("sex").toString();
				String content = (postResult.get(i).get("content").toString());
				if (postType == 1)
				{
					TextPost tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex);
					myselfPosts.add(tp);
				}
				else if (postType == 2)
				{
					ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex);
					myselfPosts.add(ip);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		Collections.sort(myselfPosts, new SortByPostDate());
		return myselfPosts;
	}
	
	public ArrayList<AbstractPost> getfriendpost()
	{
		return friendPosts;
	}
	
	public ArrayList<AbstractPost> getmypost()
	{
		return myselfPosts;
	}
	
	public AbstractPost getPostByID(int postID)
	{
		AbstractPost post = null;
		for (int i = 0; i < friendPosts.size(); i++)
		{
			if (friendPosts.get(i).getPostID() == postID)
				post = friendPosts.get(i);
		}
		return post;
	}
	
	/**
	 * 用户新发布一条Comment<br>
	 * <b>或</b>用户接收到一条推送过来的Comment
	 * @param commentID
	 * @param postID
	 * @param postUserID
	 * @param commentUserID
	 * @param comment
	 * @param commentDate
	 * @param sex
	 */
	public void addNewComment(int commentID, int postID, int postUserID, int commentUserID, String comment, String commentDate, String sex)
	{
		if (commentID == -1)
		{
			//用户新发布一条Comment
			Comment comm = new Comment(postID, postUserID, userID, comment, commentDate, sex);
			if (commentUserID == postUserID)   //用户自己评论自己
			{
				if (myselfPosts.size() != 0)
				{
					for (int i = 0; i < myselfPosts.size(); i++)
					{
						if (myselfPosts.get(i).getPostID() == comm.getPostID())
						{
							myselfPosts.get(i).appendComment(comm);
							Log.e("addNewComment", comm.getPostID() + "");
						}
					}
				}
			}
			if (friendPosts.size() != 0)
			{
				for (int i = 0; i < friendPosts.size(); i++)
				{
					if (friendPosts.get(i).getPostID() == comm.getPostID())
					{
						friendPosts.get(i).appendComment(comm);
						Log.e("addNewComment", comm.getPostID() + "");
					}
				}
			}
			else
				Log.d("debug____", "friendPosts is null");
		}
		else
		{
			//用户接收到一条推送过来的Comment
			Comment comm = new Comment(commentID, postID, postUserID, commentUserID, comment, commentDate, sex);
			//将其插入对应的Post中
			if (friendPosts.size() != 0)
			{
				for (int i = 0; i < friendPosts.size(); i++)
				{
					if (friendPosts.get(i).getPostID() == comm.getPostID())
					{
						friendPosts.get(i).appendComment(comm);
					}
				}
			}
			else
				Log.d("debug____", "friendPosts is null");
		}
	}
	
	
	/**
	 * 用户新发布一条TextPost
	 * @param postID
	 * @param postUserID
	 * @param likedNumber
	 * @param postDate
	 * @param text
	 * @param location
	 * @param sex
	 */
	public void addNewTextPost(String postDate, String text, String location, String sex)
	{
		TextPost tp = new TextPost(userID, postDate, text, location, sex);
		myselfPosts.add(tp);
		friendPosts.add(tp);
		Collections.sort(myselfPosts, new SortByPostDate());
		Collections.sort(friendPosts, new SortByPostDate());
		Log.w("______________________add", String.valueOf(myselfPosts.size()));
	}
	
	/**
	 * 用户新发布一条ImagePost
	 * @param postUserID
	 * @param postDate
	 * @param image
	 * @param location
	 * @param sex
	 */
	public void addNewImagePost(String postDate, Bitmap image, String location, String sex)
	{
		ImagePost ip = new ImagePost(userID, postDate, image, location, sex);
		myselfPosts.add(ip);
		friendPosts.add(ip);
		Collections.sort(myselfPosts, new SortByPostDate());
		Collections.sort(friendPosts, new SortByPostDate());
		Log.w("______________________add", String.valueOf(myselfPosts.size()));
	}
}
