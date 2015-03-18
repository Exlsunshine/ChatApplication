package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLServerEnd
{
	private static final String CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String DATABASE_NAME = null;
	private String TABLE_NAME = null;
	private String USERNAME = null;
	private String PASSWORD = null;
	private String DATABASE_URL = null;
	
	private Connection connection = null;
	
	public SQLServerEnd(String databaseName, String tableName)
	{
		DATABASE_NAME = databaseName;
		TABLE_NAME = tableName;
		USERNAME = "sa";
		PASSWORD = "007";
		DATABASE_URL = "jdbc:sqlserver://172.18.8.171:1433"+";databaseName=" + DATABASE_NAME;
		connect();
	}
	
	private void connect()
	{
		try
        {
            Class.forName(CLASS_NAME);
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        }
		catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	 * 与数据库断开链接
	 */
	public void disconnect()
	{
		try 					{ connection.close(); } 
		catch (SQLException e) 	{ e.printStackTrace(); }
	}
	
	
	private String joinStatement(String []column, String seperator, boolean addQuoteMark)
	{
		String values = "";
		String quoterMark = addQuoteMark? "'" : "";
		
		for (int i = 0; i < column.length - 1; i++)
			values += quoterMark + column[i] + quoterMark + " " + seperator + " ";

		values += quoterMark + column[column.length - 1] + quoterMark;
		
		return values;
	}
	
	/**
	 * 插入新一条记录：数据库表中的column[i]列的值为value[i]
	 * @param column 数据库表中的列名
	 * @param value 待插入的值，与column应一 一对应
	 * @return 错误代码<br>
	 * 1 一个或多个参数为null<br>
	 * 2 传入的两个数组的长度不一致<br>
	 * 3 数据库操作错误<br>
	 * 0 成功
	 */
	public int insert(String [] column, String [] value)
	{
		if (column == null || value == null)
		{
			System.out.println("Error:\tColumns or values null.");
			return 1;
		}
		if (column.length != value.length)
		{
			System.out.println("Error:\tColumns length and values length are not equal.");
			return 2;
		}
		
		try
		{
			Statement s1 = connection.createStatement();
			s1.executeUpdate("INSERT INTO " + TABLE_NAME
					+ "(" + joinStatement(column, ",", false) + ")"
					+ "VALUES (" + joinStatement(value, ",", true) + ")");
			System.out.println("Insert success.");
		} catch (SQLException e) {
			e.printStackTrace();
			return 3;
		}
		
		return 0;
	}
	
	private String joinConditionStatement(String [] condition, String [] value, String seperator)
	{
		String sentence = "";
		
		for (int i = 0; i < condition.length - 1; i++)
			sentence += condition[i] + " = " + "'" + value[i] + "'" + " " + seperator + " ";

		sentence += condition[condition.length - 1]  + " = " + "'" + value[condition.length - 1] + "'";
		return sentence;
	}
	
	/**
	 * 删除记录：删除所有符合condition[i] == value[i] 条件的记录
	 * @param condition 条件(数据库中的列名)
	 * @param value 条件值，与condition应一 一对应
	 * @return 错误代码<br>
	 * 1 一个或多个参数为null<br>
	 * 2 传入的两个数组的长度不一致<br>
	 * 3 数据库操作错误<br>
	 * 0 成功
	 */
	public int delete(String [] condition, String [] value)
	{
		if (condition == null || value == null)
		{
			System.out.println("Error:\tConditions or values null.");
			return 1;
		}
		if (condition.length != value.length)
		{
			System.out.println("Error:\tConditions length and values length are not equal.");
			return 2;
		}
		
		try
		{
			Statement s1 = connection.createStatement();
			s1.executeUpdate("DELETE FROM " + TABLE_NAME + " WHERE " + joinConditionStatement(condition, value, "and"));
			System.out.println("Delete success.");
		} catch (SQLException e) {
			e.printStackTrace();
			return 3;
		}
		
		return 0;
	}
	
	/**
	 * 更新记录：将所有符合condition[i] == conditionVal[i] 条件的记录的updateCol[j]列的内容更新为updateVal[j]
	 * @param updateCol 待更新的列
	 * @param updateVal 待更新的值
	 * @param condition 条件(数据库中的列名)
	 * @param conditionVal 条件值，与condition应一 一对应
	 * @return 错误代码<br>
	 * 1 一个或多个参数为null<br>
	 * 2 传入的成对的数组长度不一致<br>
	 * 3 数据库操作错误<br>
	 * 0 成功
	 */
	public int update(	String [] updateCol, String [] updateVal,
						String [] condition, String [] conditionVal)
	{
		if (updateCol == null || updateVal == null)
		{
			System.out.println("Error:\tUpdate columns or update values null.");
			return 1;
		}
		if (updateCol.length != updateVal.length)
		{
			System.out.println("Error:\tUpdate columns length and update values length are not equal.");
			return 2;
		}
		if (condition == null || conditionVal == null)
		{
			System.out.println("Error:\tConditions or values null.");
			return 1;
		}
		if (condition.length != conditionVal.length)
		{
			System.out.println("Error:\tConditions length and values length are not equal.");
			return 2;
		}
		
		try
		{
			Statement s1 = connection.createStatement();
			s1.executeUpdate("UPDATE " + TABLE_NAME + " SET "
			+ joinConditionStatement(updateCol, updateVal, ",")
			+ " WHERE " + joinConditionStatement(condition, conditionVal, "and"));
			System.out.println("Update success.");
		} catch (SQLException e) {
			e.printStackTrace();
			return 3;
		}
		
		return 0;
	}
	
	/**
	 * 查询记录：将符合condition[i] == conditionVal[i] 条件的记录的query[i]列的值 全部返回<br>
	 * 将查询结果返回到ArrayList<HashMap<String, String>>中，其中HashMap的Key[i]等于query[i]<br>
	 * @param query 待查询的列
	 * @param condition 条件(数据库中的列名)
	 * @param conditionVal 条件值，与condition应一 一对应
	 * @return
	 * null 失败：表示 一个或多个参数为null、传入的成对的数组长度不一致、数据库操作错误<br>
	 * not null 成功<br>
	 */
	public ArrayList<HashMap<String, String>> select(String [] query, String [] condition, String [] conditionVal)
	{
		if (query == null || condition == null || conditionVal == null)
		{
			System.out.println("Error:\tQuery, conditions or values null.");
			return null;
		}
		if (condition.length != conditionVal.length)
		{
			System.out.println("Error:\tConditions length and values length are not equal.");
			return null;
		}
		
		ArrayList<HashMap<String, String>> list = null;
		
		try
		{
			Statement s1 = connection.createStatement();
			ResultSet rs = s1.executeQuery("SELECT " + joinStatement(query, ",", false) + " FROM " + TABLE_NAME + " WHERE " + joinConditionStatement(condition, conditionVal, "and"));
			
			if (rs != null)
			{
				list = new ArrayList<HashMap<String, String>>();
				
				while (rs.next())
				{
					HashMap<String, String> map = new HashMap<String, String>();
					
					for (int i = 0; i < query.length; i++)
					{
						map.put(query[i], rs.getString(query[i]));
						map.put(query[i], rs.getString(query[i]));
					}
					
					list.add(map);
				}
			}
			
			System.out.println("Select success. Return " + list.size() + " results.");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	public static void main(String[] args)
	{
		SQLServerEnd se = new SQLServerEnd("test_smack_db", "mytest");
		
		String[] tableColumns = {"id", "name"};
		String[] values = {"5", "frank"};
		//se.insert(tableColumns, values);
		//se.delete(tableColumns, values);
		//se.update(new String [] {"name"}, new String [] {"AAA8aaa"}, tableColumns, values);
	
		ArrayList<HashMap<String, String>> list = se.select(new String [] {"id", "name"}, tableColumns, values);
		
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i).get("id") + "\t" + list.get(i).get("name"));
	}
}































/*try
{
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    String userName = "sa";
    String password = "007";
    String url = "jdbc:sqlserver://172.18.8.235:1433"+";databaseName=test_smack_db";
    Connection con = DriverManager.getConnection(url, userName, password);
    Statement s1 = con.createStatement();
    ResultSet rs = s1.executeQuery("SELECT * FROM ofUser");
    
    if(rs != null)
    {
        while (rs.next())
        {
        	System.out.println(rs.getString("username") + "\t" + rs.getString("plainPassword") + "\t" 
        			+rs.getString("encryptedPassword") + "\t" + rs.getString("name") + "\t"
        			+rs.getString("email") + "\t" + rs.getString("creationDate") + "\t"
        			+rs.getString("modificationDate"));
        }
    }
}
catch (Exception e)
{
    e.printStackTrace();
}*/
