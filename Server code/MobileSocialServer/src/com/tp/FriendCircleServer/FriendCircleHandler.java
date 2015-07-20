package com.tp.FriendCircleServer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.mail.Address;

import org.json.JSONException;

import com.commonapi.ConstantValues;
import com.commonapi.PackString;
import com.database.SQLServerEnd;

/**
 * @author Tp
 */
public class FriendCircleHandler
{
	private SQLServerEnd postDataTB = null;
	private SQLServerEnd commentDataTB = null;
	private SQLServerEnd postLikeTB = null;
	private SQLServerEnd UerRelationTB = null;
	private final String DATABASE_NAME = "JMMSRDB";
	private final String CommentTable = "comment_data";
	private final String PostTable = "post_data";
	private final String PostLikeTable = "post_like";
	private final String RelationTable = "user_relationship";
	private final static String SAVED_DIRECTORY = "D:/Data/IM/data/friendCircleImages/";
	
	private void initPostDataTB()
	{
		if (postDataTB == null)
			postDataTB = new SQLServerEnd(DATABASE_NAME, PostTable);
	}
	
	private void initCommentDataTB()
	{
		if (commentDataTB == null)
			commentDataTB = new SQLServerEnd(DATABASE_NAME, CommentTable);
	}
	
	private void initUerRelationTB()
	{
		if (UerRelationTB == null)
			UerRelationTB = new SQLServerEnd(DATABASE_NAME, RelationTable);
	}
	
	private void initPostLikeTB()
	{
		if (postLikeTB == null)
			postLikeTB = new SQLServerEnd(DATABASE_NAME, PostLikeTable);
	}
	/************************								***************************/
	/************************			以下是要发布的接口		***************************/
	/************************								***************************/
	/**
	 * 将客户端传来的Comment存入服务器的数据库中
	 * @param commentJson 从客户端传来的JSON格式的Comment信息
	 * @return 将成功存入服务器的Comment对应的commentID返回给客户端<br>
	 * null表示出现异常
	 */
	public String publishComment(String commentJson)
	{
		int identity = -1;
		initCommentDataTB();
		System.out.print(commentJson);
		String []insertCol = {"post_user_id", "post_id", "comment_user_id", "comment", "comment_date", "sex"};
		String []insertVal = new String[insertCol.length];
		PackString jsonString = new PackString(commentJson.toString());
		ArrayList<HashMap<String, Object>> commentResult = jsonString.jsonString2Arrylist("comments");
		for (int i = 0; i < commentResult.size(); i++)
		{
			HashMap<String, Object> map = commentResult.get(i);
			insertVal[0] = (String) map.get("post_user_id");
			insertVal[1] = (String) map.get("post_id");
			insertVal[2] = (String) map.get("comment_user_id");
			insertVal[3] = (String) map.get("comment");
			/*insertVal[4] = (String) map.get("comment_date");*/
			insertVal[4] = now();
			insertVal[5] = (String) map.get("sex");
			int errorCode = commentDataTB.insert(insertCol, insertVal);
			identity = getIdentityID("comment_data");
			System.out.print(identity + "\n");
			if (errorCode == 0)
				System.out.println("publishComment success.");
			else
				System.out.println("publishComment failed.");
		}
		commentDataTB.disconnect();
		return Integer.toString(identity);
	}
	
	private int getIdentityID(String tbName)
	{
		int identity = -1;
		String sql = "SELECT SCOPE_IDENTITY()  as [SCOPE_IDENTITY]";
		String []query = {"SCOPE_IDENTITY"};
		if (tbName.equals("comment_data"))
		{
			ArrayList<HashMap<String, String>> result = commentDataTB.excecuteRawQuery(sql, query);
			identity = Integer.parseInt(result.get(0).get("SCOPE_IDENTITY"));
			return identity;
		}
			
		if (tbName.equals("post_data"))
		{
			ArrayList<HashMap<String, String>> result = postDataTB.excecuteRawQuery(sql, query);
			identity = Integer.parseInt(result.get(0).get("SCOPE_IDENTITY"));
			return identity;
		}
		return identity;	
	}
	
	/**
	 * 将客户端传来的Post存入服务器的数据库中
	 * @param postJson 从客户端传来的JSON格式的Post信息
	 * @return 将成功存入服务器的Post对应的postID返回给客户端
	 * null表示出现异常
	 */
	public String publishPost(String postJson)
	{
		int identity = -1;
		int errorCode = -1;
		String imageUrl = "";
		initPostDataTB();
		System.out.print("publishPost" + "\n" + postJson);
		String []insertCol = {"post_user_id", "liked_number", "post_date", "content", "post_type", "location", "sex"};
		String []insertVal = new String[insertCol.length];
		List<String> val = new ArrayList<String>();
		PackString jsonString = new PackString(postJson);
		ArrayList<HashMap<String, Object>> postResult = jsonString.jsonString2Arrylist("posts");
		
		System.out.print("before for" + "\n");
		for (int i = 0; i < postResult.size(); i++)
		{
			HashMap<String, Object> map = postResult.get(i);
			val.add((String) map.get("post_user_id"));
			insertVal[0] = (String)val.get(0);
			val.add((String) map.get("liked_number"));
			insertVal[1] = (String)val.get(1);
			val.add((String) map.get("post_date"));
			System.out.print("before now()" + "\n");
			String date = now();
			insertVal[2] = date;
			
			if (map.get("post_type").equals("1")) //text
			{
				val.add((String) map.get("content"));
				insertVal[3] = (String)val.get(3);
				val.add((String) map.get("post_type"));
				insertVal[4] = (String)val.get(4);
			}
			else  //image
			{
				String imageName = generateImageName(Integer.parseInt(insertVal[0]));
				String imagePath = SAVED_DIRECTORY + imageName; 
				val.add("");
				insertVal[3] = imagePath;
				imagePath = imagePath.replace("D:/Data/IM/data/", "");
				
				imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
						+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
				System.out.print("\n" + imageUrl + "\n");
				val.add((String) map.get("post_type"));
				insertVal[4] = (String)val.get(4);
			}
			
			val.add((String) map.get("location"));
			insertVal[5] = (String)val.get(5);
			
			val.add((String) map.get("sex"));
			insertVal[6] = (String)val.get(6);
			if (insertVal[4].equals("1"))
			{
				System.out.print("insert text_____________" + "\n");
				errorCode = postDataTB.insert(insertCol, insertVal);
			}
			else if (insertVal[4].equals(ConstantValues.InstructionCode.POST_TYPE_IMAGE))
			{
				System.out.print("insert image_____________" + "\n");
				
				errorCode = postDataTB.insert(insertCol, insertVal);
			}
			identity = getIdentityID("post_data");
			System.out.print(identity + "\n");
			if (errorCode == 0)
				System.out.println("publishpost success.");
			else
				System.out.println("publishpost failed.");
		}
		postDataTB.disconnect();
		
		ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("imgURL", imageUrl);
		item.put("identityNUM", Integer.toString(identity));
		items.add(item);
		String ps = "";
		try {
			ps = PackString.arrylist2JsonString("publishpost", items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}
	
	/**
	 * 根据给定的postID获取其对应的所有Comment
	 * @param postID 指定的postID
	 * @return JSON格式的Comments信息<br>
	 * null表示出现异常
	 */
	public String getCommentsByPostID(int postID)
	{
		initCommentDataTB();
		
		String []query = {"comment"};
		String []condition = {"post_id"};
		String []conditionVal = {Integer.toString(postID)};
		ArrayList<HashMap<String, String>> result = commentDataTB.select(query, condition, conditionVal);
		commentDataTB.disconnect();
		if (result == null)
			System.out.print("get comments failed.");

		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("comments", result, 0);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.print("\n" + str);
		return str;
	}
	
	/**
	 * 根据用户ID获得用户自己所有的post
	 * @param userID
	 * @return JSON格式的Post信息<br>
	 * null表示出现异常
	 */
	public String getMyselfPosts(int userID)
	{
		initPostDataTB();
		
		String imageUrl = "";
		String []query = {"id", "post_user_id", "liked_number", "post_date", "content", "post_type", "location", "sex"};
		String []condition = {"post_user_id"};
		String []conditionVal = {Integer.toString(userID)};
		ArrayList<HashMap<String, String>> result = postDataTB.select(query, condition, conditionVal);
		postDataTB.disconnect();
		if (result.size() == 0)
			return null;
		System.out.print(result.size() + "\n");
		for (int i = 0; i < result.size(); i++)
		{
			try 
			{
				if (result.get(i).get("post_type").equals("2"))
				{
					System.out.print("\n" + result.get(i).get("content") + "\n");
					//String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					String imagePath = result.get(i).get("content");
					imagePath = imagePath.replace("D:/Data/IM/data/", "");
					
					imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
							+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
					
					System.out.print("\n" + imagePath + "\n");
					System.out.print("\n" + imageUrl + "\n");
					result.get(i).put("content", imageUrl);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("userhimselfpost", result, 0);
			System.out.print("\n" + str + "\n");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 根据用户ID获得用户10条历史的post
	 * @param userID
	 * @param postID
	 * @return JSON格式的Post信息<br>
	 * null表示出现异常
	 */
	public String getHistoryPosts(int userID, int postID)
	{
		initUerRelationTB();
		
		String imageUrl = "";
		System.out.print("\n" + postID + "\n" + "------------------------------------------");
		String []relationquery = {"second_userid"};
		String []relationcondition = {"first_userid"};
		String []relationconditionVal = {Integer.toString(userID)};
		ArrayList<HashMap<String, String>> relationResult = UerRelationTB.select(relationquery, relationcondition, relationconditionVal);
		UerRelationTB.disconnect();
		if (relationResult.size() == 0)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("second_userid", Integer.toString(userID));
			relationResult.add(map);
		}
		
		initPostDataTB();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select top 10 id ,post_user_id, liked_number, post_date, content, post_type, location, sex from post_data where ( post_user_id = ");
		for (int i = 0; i < relationResult.size(); i++)
		{
			sb.append(String.valueOf(userID)).append(" or post_user_id = ");
			String friendId = relationResult.get(i).get("second_userid");
			sb.append(friendId + " ");
			if (i != relationResult.size() - 1)
				sb.append("or ").append("post_user_id = ");
			else
			{
				if (postID == -1)
					sb.append(")"  + "order by id DESC");
				else
					sb.append(") and (id < " + Integer.toString(postID) + ") " + "order by post_date DESC");
			}
		}
		String sql = sb.toString();
		System.out.print(sql);
		String []query = {"id", "post_user_id", "liked_number", "post_date", "content", "post_type", "location", "sex"};
		ArrayList<HashMap<String, String>> result = postDataTB.excecuteRawQuery(sql, query);
		postDataTB.disconnect();
		if (result.size() == 0)
			return null;
		for (int i = 0; i < result.size(); i++)
		{
			try 
			{
				if (result.get(i).get("post_type").equals("2"))
				{
					System.out.print("\n" + result.get(i).get("content") + "\n");
					//String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					
					String imagePath = result.get(i).get("content");
					imagePath = imagePath.replace("D:/Data/IM/data/", "");
					
					imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
							+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
					
					System.out.print("\n" + imageUrl + "\n");
					result.get(i).put("content", imageUrl);
				}
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("historypostsFromServer", result, 0);
			System.out.print("\n" + str + "\n");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	
	public String get10Posts(int userID)
	{
		initUerRelationTB();
		
		System.out.print("get10Posts" + "\n");
		String []relationquery = {"second_userid"};
		String []relationcondition = {"first_userid"};
		String []relationconditionVal = {Integer.toString(userID)};
		ArrayList<HashMap<String, String>> relationResult = UerRelationTB.select(relationquery, relationcondition, relationconditionVal);
		UerRelationTB.disconnect();
		if (relationResult.size() == 0)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("second_userid", Integer.toString(userID));
			relationResult.add(map);
		}
		
		initPostDataTB();
		
		String imageUrl = "";
		StringBuilder sb = new StringBuilder();
		sb.append("select top 10 id ,post_user_id, liked_number, post_date, content, post_type, location, sex from post_data where ( post_user_id = ");
		for (int i = 0; i < relationResult.size(); i++)
		{
			sb.append(String.valueOf(userID)).append(" or post_user_id = ");
			String friendId = relationResult.get(i).get("second_userid");
			sb.append(friendId + " ");
			if (i != relationResult.size() - 1)
				sb.append("or ").append("post_user_id = ");
			else
			{
				sb.append(")"  + "order by id DESC");
			}
		}
		String sql = sb.toString();
		System.out.print(sql);
		String []query = {"id", "post_user_id", "liked_number", "post_date", "content", "post_type", "location", "sex"};
		ArrayList<HashMap<String, String>> result = postDataTB.excecuteRawQuery(sql, query);
		postDataTB.disconnect();
		if (result.size() == 0)
			return null;
		for (int i = 0; i < result.size(); i++)
		{
			try 
			{
				if (result.get(i).get("post_type").equals("2"))
				{
					//String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					
					String imagePath = result.get(i).get("content");
					imagePath = imagePath.replace("D:/Data/IM/data/", "");
					
					imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
							+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
					
					result.get(i).put("content", imageUrl);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("10postsFromServer", result, 0);
			System.out.print("\n" + str + "\n");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 根据给定的userID，从服务器获取其自己与好友们N + 10条最新的Post<br>
	 * <b>注：是从指定的postID下一条Post开始的最近N + 10条，即返回的postID均应该大于当前指定的postID</b>
	 * 如果postID = -1 则意味着不做筛选，仅选择好友们最新的10条Post
	 * @param userID
	 * @return JSON格式的Post信息<br>
	 * null表示出现异常
	 */
	public String getLatestPosts(int userID, int postIDlatest, int postIDoldest, int num)
	{
		initUerRelationTB();
	
		String []relationquery = {"second_userid"};
		String []relationcondition = {"first_userid"};
		String []relationconditionVal = {Integer.toString(userID)};
		ArrayList<HashMap<String, String>> relationResult = UerRelationTB.select(relationquery, relationcondition, relationconditionVal);
		UerRelationTB.disconnect();
		if (relationResult.size() == 0)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("second_userid", Integer.toString(userID));
			relationResult.add(map);
		}
		
		initPostDataTB();
		String imageUrl;
		StringBuilder sb = new StringBuilder();
		sb.append("select top ").append(num + " ");
		sb.append("id ,post_user_id, liked_number, post_date, content, post_type, location, sex from post_data where ( post_user_id = ");
		for (int i = 0; i < relationResult.size(); i++)
		{
			sb.append(String.valueOf(userID)).append(" or post_user_id = ");
			String friendId = relationResult.get(i).get("second_userid");
			sb.append(friendId + " ");
			if (i != relationResult.size() - 1)
				sb.append("or ").append("post_user_id = ");
			else
			{
				sb.append(") and (id > " + Integer.toString(postIDlatest) + ") " + "order by id DESC");
			}
		}
		String sql = sb.toString();
		System.out.print(sql);
		String []query = {"id", "post_user_id", "liked_number", "post_date", "content", "post_type", "location", "sex"};
		ArrayList<HashMap<String, String>> result = postDataTB.excecuteRawQuery(sql, query);
		
		
		StringBuilder sb1 = new StringBuilder();
		sb1.append("select ");
		sb1.append("id ,post_user_id, liked_number, post_date, content, post_type, location, sex from post_data where ( post_user_id = ");
		for (int i = 0; i < relationResult.size(); i++)
		{
			sb1.append(String.valueOf(userID)).append(" or post_user_id = ");
			String friendId = relationResult.get(i).get("second_userid");
			sb1.append(friendId + " ");
			if (i != relationResult.size() - 1)
				sb1.append("or ").append("post_user_id = ");
			else
			{
				sb1.append(") and (id <= " + Integer.toString(postIDlatest) + ") " +"and ( id >= " + Integer.toString(postIDoldest) + ") " + "order by id DESC");
			}
		}
		String sql1 = sb1.toString();
		System.out.print(sql1);
		result.addAll(postDataTB.excecuteRawQuery(sql1, query));
		postDataTB.disconnect();
		if (result.size() == 0)
			return null;
		for (int i = 0; i < result.size(); i++)
		{
			try 
			{
				if (result.get(i).get("post_type").equals("2"))
				{
					//String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					
					String imagePath = result.get(i).get("content");
					imagePath = imagePath.replace("D:/Data/IM/data/", "");
					
					imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
							+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
					result.get(i).put("content", imageUrl);
				}
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		
		
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("latestpostsFromServer", result, 0);
			System.out.print("\n" + str + "\n");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 删除Post中指定的一条Comment
	 * @param postID 要删除的Comment所在的Post的postID
	 * @param commentID 要删除的Comment的commentID
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	public int deleteComment(int postID, int commentID)
	{
		initCommentDataTB();
		String [] condition = {"post_id","id"};
		String [] value = {Integer.toString(postID),Integer.toString(commentID)};
		int erorcode= commentDataTB.delete(condition, value);
		commentDataTB.disconnect();
		if (erorcode == 0)
			return 0;
		else 
			return 1;
	}
	
	public String getComments(int postID)
	{
		initCommentDataTB();
		String [] query = {"id", "post_id", "post_user_id","comment_user_id", "comment", "comment_date", "sex"};
		String [] condition = {"post_id"};
		String [] conditionVal = {Integer.toString(postID)};
		
		ArrayList<HashMap<String, String>> resultHashmap = commentDataTB.select(query, condition, conditionVal);
		if (resultHashmap.size() == 0)
			return null;
		
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("commentsFromServer", resultHashmap, 0);
			System.out.print("\n" + str);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	
	public String getpostbypostid(int postID)
	{
		initPostDataTB();
		
		String imageUrl;
		String []query = {"id","post_user_id", "liked_number", "post_date", "content", "post_type", "location", "sex"};
		String []condition = {"id"};
		String []conditionVal = {Integer.toString(postID)};
		ArrayList<HashMap<String, String>> result = postDataTB.select(query, condition, conditionVal);
		postDataTB.disconnect();
		if (result == null)
			return null;
		
		for (int i = 0; i < result.size(); i++)
		{
			try 
			{
				if (result.get(i).get("post_type").equals("2"))
				{
					//String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					
					String imagePath = result.get(i).get("content");
					imagePath = imagePath.replace("D:/Data/IM/data/", "");
					
					imageUrl = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
							+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + imagePath;
					
					result.get(i).put("content", imageUrl);
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("post", result, 0);
			System.out.print("\n" + str + "\n");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.print("\n" + str);
		return str;
	}
	/**
	 * 判断该条POST用户是否点赞，点赞则删除点赞记录，没点赞则插入点赞记录
	 * @param userID
	 * @param postID
	 * @return 操作结果的状态
	 * -1表示已经点赞，点赞数-1
	 * +1表示没有点赞，点赞数+1
	 */
	public int modifyLikedNumber(int postID, int userID)
	{
		initPostLikeTB();
		
		String []condition = {"post_id", "user_id"};
		String []conditionVal = {Integer.toString(postID), Integer.toString(userID)};
		
		boolean isLiked = postLikeTB.isConditionExist(condition, conditionVal);
		if (isLiked)
		{
			postLikeTB.delete(condition, conditionVal);
			decreaseLikeByPostID(postID);
			postLikeTB.disconnect();
			return -1;
		}
		else
		{
			postLikeTB.insert(condition, conditionVal);
			increaseLikeByPostID(postID);
			postLikeTB.disconnect();
			return 1;
		}
	}
	
	/************************									**************************/
	/************************			以上是要发布的接口			**************************/
	/************************									**************************/
	/**
	 * 给指定的Post赞的数量+1
	 * @param postID 指定的Post对应的postID
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	private int increaseLikeByPostID(int postID)
	{
		initPostDataTB();
		String sql = "select id from post_data where id = " + String.valueOf(postID) + " update post_data set liked_number = liked_number + 1 where id = " + String.valueOf(postID);
		String []query = {"id"};
		System.out.print("_______________________" + "\n");
		ArrayList<HashMap<String, String>> result = postDataTB.excecuteRawQuery(sql, query);
		postDataTB.disconnect();
		
		if (result != null)
		{
			if (result.size() == 0)
			{
				System.out.print("updata failed" + "\n");
				return 1;
			}
			else if (result.get(0).get("id").equals(Integer.toString(postID)))
			{
				System.out.print("updata success" + "\n");
				return 0;
			}
			else
			{
				System.out.print("updata failed" + "\n");
				return 1;
			}
		}
		else
		{
			System.out.print("updata failed" + "\n");
			return 1;
		}
	}
	
	/**
	 * 给指定的Post赞的数量-1
	 * @param postID 指定的Post对应的postID
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	private int decreaseLikeByPostID(int postID)
	{
		initPostDataTB();
		String sql = "select id from post_data where id = " + String.valueOf(postID) + " update post_data set liked_number = liked_number - 1 where id = " + String.valueOf(postID);
		String []query = {"id"};
		System.out.print("_______________________" + "\n");
		ArrayList<HashMap<String, String>> result = postDataTB.excecuteRawQuery(sql, query);
		postDataTB.disconnect();
		
		if (result != null)
		{
			if (result.size() == 0)
			{
				System.out.print("updata failed" + "\n");
				return 1;
			}
			else if (result.get(0).get("id").equals(Integer.toString(postID)))
			{
				System.out.print("updata success" + "\n");
				return 0;
			}
			else
			{
				System.out.print("updata failed" + "\n");
				return 1;
			}
		}
		else
		{
			System.out.print("updata failed" + "\n");
			return 1;
		}
	}
	
	
	
	private String now()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return sdf.format(cal.getTime());
	}
	
	private String generateImageName(int postUserID)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date date = new Date();
		return "picture_transportation_postUserID_" + String.valueOf(postUserID) + "_" + dateFormat.format(date)  + ".jpg";
	}
}