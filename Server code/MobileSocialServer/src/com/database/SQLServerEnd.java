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
	 * �����ݿ�Ͽ�����
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
	 * ������һ����¼�����ݿ���е�column[i]�е�ֵΪvalue[i]
	 * @param column ���ݿ���е�����
	 * @param value �������ֵ����columnӦһ һ��Ӧ
	 * @return �������<br>
	 * 1 һ����������Ϊnull<br>
	 * 2 �������������ĳ��Ȳ�һ��<br>
	 * 3 ���ݿ��������<br>
	 * 0 �ɹ�
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
	 * ɾ����¼��ɾ�����з���condition[i] == value[i] �����ļ�¼
	 * @param condition ����(���ݿ��е�����)
	 * @param value ����ֵ����conditionӦһ һ��Ӧ
	 * @return �������<br>
	 * 1 һ����������Ϊnull<br>
	 * 2 �������������ĳ��Ȳ�һ��<br>
	 * 3 ���ݿ��������<br>
	 * 0 �ɹ�
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
	 * ���¼�¼�������з���condition[i] == conditionVal[i] �����ļ�¼��updateCol[j]�е����ݸ���ΪupdateVal[j]
	 * @param updateCol �����µ���
	 * @param updateVal �����µ�ֵ
	 * @param condition ����(���ݿ��е�����)
	 * @param conditionVal ����ֵ����conditionӦһ һ��Ӧ
	 * @return �������<br>
	 * 1 һ����������Ϊnull<br>
	 * 2 ����ĳɶԵ����鳤�Ȳ�һ��<br>
	 * 3 ���ݿ��������<br>
	 * 0 �ɹ�
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
	 * ��ѯ��¼��������condition[i] == conditionVal[i] �����ļ�¼��query[i]�е�ֵ ȫ������<br>
	 * ����ѯ������ص�ArrayList<HashMap<String, String>>�У�����HashMap��Key[i]����query[i]<br>
	 * @param query ����ѯ����
	 * @param condition ����(���ݿ��е�����)
	 * @param conditionVal ����ֵ����conditionӦһ һ��Ӧ
	 * @return
	 * null ʧ�ܣ���ʾ һ����������Ϊnull������ĳɶԵ����鳤�Ȳ�һ�¡����ݿ��������<br>
	 * not null �ɹ�<br>
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
