package com.tp.messege;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONException;

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
	private boolean isExist = false;
	
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
		friendPosts.clear();
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
				String content = postResult.get(i).get("content").toString();
				int versionCode = Integer.parseInt(postResult.get(i).get("version_code").toString());
				Log.e("get10Posts versionCode", postIDFromServer + " " + versionCode);
				if (postType == 1) //文字
				{
					TextPost tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex, versionCode);
					friendPosts.add(tp);
					postArrayList.add(tp);
				}
				else if (postType == 2) //图片
				{
					ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex, versionCode);
					Log.e("get10Posts imgurl", content);
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
	public ArrayList<AbstractPost> getLatestPosts() throws JSONException
	{
		if (friendPosts.size() != 0)
		{
			currentLatestPostID = friendPosts.get(0).getPostID();
			currentOldestPostID = friendPosts.get(friendPosts.size() - 1).getPostID();
		}
		else
			return get10Posts();
		WebServiceAPI wsApi;
		
		ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
		Log.e("getLatestPostsdebug_______", friendPosts.size() + "__________");
		for (int i = 0; i < friendPosts.size(); i++)
		{
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("id", friendPosts.get(i).getPostID());
			item.put("version_code", friendPosts.get(i).getVersionCode());
			Log.e("getLatestPostsdebug_______", friendPosts.get(i).getPostID() + "__________");
			items.add(item);
		}
		String ps = PackString.arrylist2JsonString("postIDandVersionCode", items);
		Log.e("getLatestPostsdebug_______", "JSON STRING_______" + ps);
		Object []value = {userID, currentLatestPostID, currentOldestPostID, friendPosts.size() + 10, ps}; 
		String []para = {"userID", "postIDlatest","postIDoldest","num", "postIDandVersionCodeJson"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("getLatestPosts", para ,value);
		if (s == null)
			return friendPosts;
		PackString jsonString = new PackString(s.toString());
		final ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("latestpostsFromServer");
		Log.d("getLatestPostsdebug_______", postResult.size() + "__________");
		Thread td = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				for (int i = 0; i < postResult.size(); i++)
				{
					final int postIDFromServer = Integer.parseInt(postResult.get(i).get("id").toString());
					Log.e("getLatestPosts", "postIDFromServer:" + postIDFromServer);
					final int postUserID = Integer.parseInt(postResult.get(i).get("post_user_id").toString());
					final int likedNumber = Integer.parseInt(postResult.get(i).get("liked_number").toString()); 
					final String postDate = postResult.get(i).get("post_date").toString();
					final int postType = Integer.parseInt(postResult.get(i).get("post_type").toString());
					final String location = postResult.get(i).get("location").toString();
					final String sex = postResult.get(i).get("sex").toString();
					final String content = (postResult.get(i).get("content").toString());	
					final int versionCode = Integer.parseInt(postResult.get(i).get("version_code").toString());
					Log.e("getLatestPosts versionCode", postIDFromServer + " " + versionCode);
					if (postType == 1) //文字
					{
						int j = 0;
						for (j = 0; j < friendPosts.size(); j++)
						{
							Log.e("getLatestPosts", "POSTid:" + friendPosts.get(j).getPostID());
							if (friendPosts.get(j).getPostID() == postIDFromServer)  //已经存在的POST
							{
								Log.e("getLatestPosts", "POST Already exist________");
								isExist = true;
								//versionCode不同,本地版本落后于服务器
								if (friendPosts.get(j).getVersionCode() != versionCode)
								{
									//更新versionCode
									friendPosts.get(j).setVersionCode(versionCode);
									//likedNumber不同，更新likedNumber,更新comment
									if (friendPosts.get(j).getLikedNumber() != likedNumber)
									{
										Log.e("getLatestPosts", "updateComment and LIKEDNUMBER");
										friendPosts.get(j).setLocalLikedNumber(likedNumber);
										friendPosts.get(j).updateComment();
									}
									//likedNumber相同，说明COMMENT不同，更新COMMENT
									else
									{
										Log.e("getLatestPosts", "updateComment");
										friendPosts.get(j).updateComment();
									}
									break;
								}
							}
						}
						//服务器返回的postID与本地存储的都不同，说明本地没有该POST，添加该POST
						if (j == friendPosts.size() && isExist == false)
						{
							tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex, versionCode);
							Log.e("getLatestPosts","创建post" + "" + tp.getPostID());
							friendPosts.add(tp);
						}
					}
					else if (postType == 2) //图片
					{
						int j = 0;
						for (j = 0; j < friendPosts.size(); j++)
						{
							Log.e("getLatestPosts", "POSTid:" + friendPosts.get(j).getPostID());
							if (friendPosts.get(j).getPostID() == postIDFromServer)  //已经存在的POST
							{
								Log.e("getLatestPosts", "POST Already exist________");
								isExist = true;
								//versionCode不同,本地版本落后于服务器
								if (friendPosts.get(j).getVersionCode() != versionCode)
								{
									//更新versionCode
									friendPosts.get(j).setVersionCode(versionCode);
									//likedNumber不同，更新likedNumber,更新comment
									if (friendPosts.get(j).getLikedNumber() != likedNumber)
									{
										Log.e("getLatestPosts", "updateComment and LIKEDNUMBER");
										friendPosts.get(j).setLocalLikedNumber(likedNumber);
										friendPosts.get(j).updateComment();
									}
									//likedNumber相同，说明COMMENT不同，更新COMMENT
									else
									{
										Log.e("getLatestPosts", "updateComment");
										friendPosts.get(j).updateComment();
									}
									break;
								}
							}
						}
						//服务器返回的postID与本地存储的都不同，说明本地没有该POST，添加该POST
						if (j == friendPosts.size() && isExist == false)
						{
							ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex, versionCode);						friendPosts.add(ip);
							Log.e("getLatestPosts", "添加" + j);
						}
					}
					isExist = false;
				}
			}
		});
		td.start();
		try 
		{
			td.join();
			Collections.sort(friendPosts, new SortByPostDate());
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
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
				String content = postResult.get(i).get("content").toString();
				int versionCode = Integer.parseInt(postResult.get(i).get("version_code").toString());
				Log.e("getHistoryPosts versionCode", postIDFromServer + " " + versionCode);
				if (postType == 1) //文字
				{
					TextPost tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex, versionCode);
					friendPosts.add(tp);
					postArrayList.add(tp);
				}
				else if (postType == 2) //图片
				{
					ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex, versionCode);
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
		WebServiceAPI wsApi;
		Object []value = {userID}; 
		String []para = {"userID"};
		wsApi = new WebServiceAPI(packageName, className);
		final Object s = wsApi.callFuntion("getMyselfPosts", para ,value);
		if (s == null)
		{
			Log.d("getMyselfPosts__", "MyselfPosts is null");
			return null;
		}
		else
			Log.d("getMyselfPosts__", s.toString());
		
		myselfPosts.clear();
		PackString jsonString = new PackString(s.toString());
		final ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("userhimselfpost");
		Log.e("getMyselfPosts postResult.size()", postResult.size() + "");
		Thread td = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				for (int i = 0; i < postResult.size(); i++)
				{
					boolean isExsist = false; 
					int postIDFromServer = Integer.parseInt(postResult.get(i).get("id").toString());
					int postUserID = Integer.parseInt(postResult.get(i).get("post_user_id").toString());
					int likedNumber = Integer.parseInt(postResult.get(i).get("liked_number").toString()); 
					String postDate = postResult.get(i).get("post_date").toString();
					int postType = Integer.parseInt(postResult.get(i).get("post_type").toString());
					String location = postResult.get(i).get("location").toString();
					String sex = postResult.get(i).get("sex").toString();
					String content = postResult.get(i).get("content").toString();
					int versionCode = Integer.parseInt(postResult.get(i).get("version_code").toString());
					
					for (int j = 0; j < friendPosts.size(); j++)
					{
						if (postIDFromServer == friendPosts.get(j).getPostID())
						{
							Log.e("getMyselfPosts isExsist", j + "isExsist " + postIDFromServer);
							isExsist = true;
							if (postType == 1)
								myselfPosts.add(friendPosts.get(j));
							else if (postType == 2)
								myselfPosts.add(friendPosts.get(j));
							break;
						}
					}
					if (isExsist == true)
						continue;
					try 
					{
						Log.e("getMyselfPosts versionCode", postIDFromServer + " " + versionCode);
						if (postType == 1)
						{
							TextPost tp = new TextPost(postIDFromServer,postUserID, likedNumber, postDate, content, location, sex, versionCode);
							myselfPosts.add(tp);
						}
						else if (postType == 2)
						{
							ImagePost ip = new ImagePost(postIDFromServer,postUserID, likedNumber ,postDate, content, location, sex, versionCode);
							myselfPosts.add(ip);
						}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			}
		});
		td.start();
		try 
		{
			td.join();
			Collections.sort(myselfPosts, new SortByPostDate());
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
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
		if (post == null)
		{
			for (int i = 0; i < myselfPosts.size(); i++)
			{
				if (myselfPosts.get(i).getPostID() == postID)
					post = myselfPosts.get(i);
			}
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
			boolean isPostExsitInFriendPost = false;
			//用户新发布一条Comment
			Comment comm = new Comment(postID, postUserID, userID, comment, commentDate, sex);
			if (friendPosts.size() != 0)
			{
				for (int i = 0; i < friendPosts.size(); i++)
				{
					if (friendPosts.get(i).getPostID() == comm.getPostID())
					{
						isPostExsitInFriendPost = true;
						Log.e("before appendComment", comm.getSex());
						friendPosts.get(i).appendComment(comm);
					}
				}
			}
			if (postUserID == commentUserID && isPostExsitInFriendPost == false)
			{
				if (myselfPosts.size() != 0)
				{
					for (int i = 0; i < myselfPosts.size(); i++)
					{
						if (myselfPosts.get(i).getPostID() == comm.getPostID())
						{
							Log.e("before appendComment", comm.getSex());
							myselfPosts.get(i).appendComment(comm);
						}
					}
				}
			}
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
		Log.e("addNewTextPost", userID + "");
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
