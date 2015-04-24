package com.tp.FriendCircleServer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
	private SQLServerEnd UerRelationTB = null;
	private final String DATABASE_NAME = "JMMSRDB";
	private final String CommentTable = "comment_data";
	private final String PostTable = "post_data";
	private final String RelationTable = "user_relationship";
	
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
	
	/************************								***************************/
	/************************			������Ҫ�����Ľӿ�		***************************/
	/************************								***************************/
	/**
	 * ���ͻ��˴�����Comment��������������ݿ���
	 * @param commentJson �ӿͻ��˴�����JSON��ʽ��Comment��Ϣ
	 * @return ���ɹ������������Comment��Ӧ��commentID���ظ��ͻ���<br>
	 * null��ʾ�����쳣
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
			insertVal[4] = (String) map.get("comment_date");
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
	 * ���ͻ��˴�����Post��������������ݿ���
	 * @param postJson �ӿͻ��˴�����JSON��ʽ��Post��Ϣ
	 * @return ���ɹ������������Post��Ӧ��postID���ظ��ͻ���
	 * null��ʾ�����쳣
	 */
	public String publishPost(String postJson)
	{
		int identity = -1;
		int errorCode = -1;
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
				System.out.print("image__________" + "\n");
				String str = map.get("content").toString();
				try 
				{
					System.out.print("before save image__________" + "\n");
					String imagePath = ImagePostTransmit.saveImage(str, Integer.parseInt(insertVal[0]));
					System.out.print("after save image__________" + "\n");
					val.add(imagePath);
					insertVal[3] = (String)val.get(3);
				} 
				catch (NumberFormatException e) 
				{
					e.printStackTrace();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
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
		return Integer.toString(identity);
	}
	
	/**
	 * ���ݸ�����postID��ȡ���Ӧ������Comment
	 * @param postID ָ����postID
	 * @return JSON��ʽ��Comments��Ϣ<br>
	 * null��ʾ�����쳣
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
	 * �����û�ID����û��Լ����е�post
	 * @param userID
	 * @return JSON��ʽ��Post��Ϣ<br>
	 * null��ʾ�����쳣
	 */
	public String getMyselfPosts(int userID)
	{
		initPostDataTB();
		
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
					String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					System.out.print("\n" + img + "\n");
					result.get(i).put("content", img);
				}
				
			} catch (Exception e) 
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
	 * �����û�ID����û�10����ʷ��post
	 * @param userID
	 * @param postID
	 * @return JSON��ʽ��Post��Ϣ<br>
	 * null��ʾ�����쳣
	 */
	public String getHistoryPosts(int userID, int postID)
	{
		initUerRelationTB();
		System.out.print("\n" + postID + "\n" + "------------------------------------------");
		String []relationquery = {"second_userid"};
		String []relationcondition = {"first_userid"};
		String []relationconditionVal = {Integer.toString(userID)};
		ArrayList<HashMap<String, String>> relationResult = UerRelationTB.select(relationquery, relationcondition, relationconditionVal);
		UerRelationTB.disconnect();
		if (relationResult.size() == 0)
			return null;
		
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
					String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					System.out.print("\n" + img + "\n");
					result.get(i).put("content", img);
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
			return null;
		
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
					String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					result.get(i).put("content", img);
				}
				
			} catch (Exception e) 
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
	 * ���ݸ�����userID���ӷ�������ȡ���Լ��������N + 10�����µ�Post<br>
	 * <b>ע���Ǵ�ָ����postID��һ��Post��ʼ�����N + 10���������ص�postID��Ӧ�ô��ڵ�ǰָ����postID</b>
	 * ���postID = -1 ����ζ�Ų���ɸѡ����ѡ����������µ�10��Post
	 * @param userID
	 * @return JSON��ʽ��Post��Ϣ<br>
	 * null��ʾ�����쳣
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
			return null;
		
		initPostDataTB();
		
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
					String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					result.get(i).put("content", img);
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
	 * ��ָ����Post�޵�����+1
	 * @param postID ָ����Post��Ӧ��postID
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
	 */
	public int increaseLikeByPostID(int postID)
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
	 * ��ָ����Post�޵�����-1
	 * @param postID ָ����Post��Ӧ��postID
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
	 */
	public int decreaseLikeByPostID(int postID)
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
	
	/**
	 * ɾ��Post��ָ����һ��Comment
	 * @param postID Ҫɾ����Comment���ڵ�Post��postID
	 * @param commentID Ҫɾ����Comment��commentID
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
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
					String img =  ImageTransmitOldVersion.image2String(result.get(i).get("content"));
					result.get(i).put("content", img);
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
	/************************									**************************/
	/************************			������Ҫ�����Ľӿ�			**************************/
	/************************									**************************/
	private String now()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return sdf.format(cal.getTime());
	}
}