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
	private boolean DEBUG_MODEL = true;
	
	private Connection connection = null;
	
	public SQLServerEnd(String databaseName, String tableName)
	{
		DATABASE_NAME = databaseName;
		TABLE_NAME = tableName;
		USERNAME = "sa";
		PASSWORD = "007";
		DATABASE_URL = "jdbc:sqlserver://172.18.8.142:1433"+";databaseName=" + DATABASE_NAME;
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
			System.out.print(DEBUG_MODEL ? "Error:\tColumns or values null." : "");
			return 1;
		}
		if (column.length != value.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tColumns length and values length are not equal." : "");
			return 2;
		}
		
		try
		{
			Statement s1 = connection.createStatement();
			s1.executeUpdate("INSERT INTO " + TABLE_NAME
					+ "(" + joinStatement(column, ",", false) + ")"
					+ "VALUES (" + joinStatement(value, ",", true) + ")");
			System.out.print(DEBUG_MODEL ? "Insert success." : "");
		} catch (SQLException e) {
			e.printStackTrace();
			return 3;
		}
		
		return 0;
	}
	
	private String joinConditionStatement(String [] condition, String [] value, String seperator, String operator)
	{
		String sentence = "";
		
		for (int i = 0; i < condition.length - 1; i++)
			sentence += condition[i] + " " + operator + " " + "'" + value[i] + "'" + " " + seperator + " ";

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
			System.out.print(DEBUG_MODEL ? "Error:\tConditions or values null." : "");
			return 1;
		}
		if (condition.length != value.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions length and values length are not equal." : "");
			return 2;
		}
		
		try
		{
			Statement s1 = connection.createStatement();
			s1.executeUpdate("DELETE FROM " + TABLE_NAME + " WHERE " + joinConditionStatement(condition, value, "and", "="));
			System.out.print(DEBUG_MODEL ? "Delete success." : "");
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
			System.out.print(DEBUG_MODEL ? "Error:\tUpdate columns or update values null." : "");
			return 1;
		}
		if (updateCol.length != updateVal.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tUpdate columns length and update values length are not equal." : "");
			return 2;
		}
		if (condition == null || conditionVal == null)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions or values null." : "");
			return 1;
		}
		if (condition.length != conditionVal.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions length and values length are not equal." : "");
			return 2;
		}
		
		try
		{
			Statement s1 = connection.createStatement();
			s1.executeUpdate("UPDATE " + TABLE_NAME + " SET "
			+ joinConditionStatement(updateCol, updateVal, ",", "=")
			+ " WHERE " + joinConditionStatement(condition, conditionVal, "and", "="));
			System.out.print(DEBUG_MODEL ? "Update success." : "");
		} catch (SQLException e) {
			e.printStackTrace();
			return 3;
		}
		
		return 0;
	}
	
	/**
	 * ��ѯ��¼��������condition[i] == conditionVal[i] �����ļ�¼��query[i]�е�ֵ ȫ������<br>
	 * ����ѯ������ص�ArrayList<HashMap<String, String>>�У�����HashMap��Key[i]����query[i]<br>
	 * @param query ����ѯ����(����new String [] { "*" }���൱��ѡ��������)
	 * @param condition ����(���ݿ��е�����)
	 * @param conditionVal ����ֵ����conditionӦһ һ��Ӧ
	 * @return
	 * null ʧ�ܣ���ʾ һ����������Ϊnull������ĳɶԵ����鳤�Ȳ�һ�¡����ݿ��������<br>
	 * not null �ɹ�<br>
	 */
	public ArrayList<HashMap<String, String>> select(String [] query, String [] condition, String [] conditionVal)
	{
		if (condition == null || conditionVal == null)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions or values null." : "");
			return null;
		}
		if (condition.length != conditionVal.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions length and values length are not equal." : "");
			return null;
		}
		
		ArrayList<HashMap<String, String>> list = null;
		
		try
		{
			String request = null;
			if (query.length == 1 && query[0].equals("*"))
				request = "*";
			else
				request = joinStatement(query, ",", false);
				
			Statement s1 = connection.createStatement();
			ResultSet rs = s1.executeQuery("SELECT " + request + " FROM " + TABLE_NAME + " WHERE " + joinConditionStatement(condition, conditionVal, "and", "="));
			
			System.out.println("SELECT " + request + " FROM " + TABLE_NAME + " WHERE " + joinConditionStatement(condition, conditionVal, "and", "="));
			
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
			
			System.out.print(DEBUG_MODEL ? "Select success. Return " + list.size() + " results." : "");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param condition ����(���ݿ��е�����)
	 * @param conditionVal ����ֵ����conditionӦһ һ��Ӧ
	 * @return false ��ʾ��ѯ�����������������ѯ�Ĳ�������<br>
	 * true ��ʾ��ѯ����������
	 */
	public boolean isConditionExist(String [] condition, String [] conditionVal)
	{
		if (condition == null || conditionVal == null)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions or values null." : "");
			return false;
		}
		if (condition.length != conditionVal.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions length and values length are not equal." : "");
			return false;
		}
		
		ArrayList<HashMap<String, String>> list = select(new String [] { "id" }, condition, conditionVal);
		if (list.size() == 0)
			return false;
		else
			return true;
	}
	
	
	/**
	 * ��ѯ��¼��������condition[i] == conditionVal[i] �����ļ�¼��query[i]�е�ֵ ȫ������<br>
	 * ����ѯ������ص�ArrayList<HashMap<String, String>>�У�����HashMap��Key[i]����query[i]<br>
	 * @param query ����ѯ����
	 * @param condition ����(���ݿ��е�����)
	 * @param conditionVal ����ֵ����conditionӦһ һ��Ӧ
	 * @param operator ��condition��conditionVal�Ĺ�ϵ��ʾ��"=",">",">="<br>
	 * ��operator����Ϊ"="ʱ���൱�ڵ���{@link #select(String[], String[], String[])}}
	 * @return
	 * null ʧ�ܣ���ʾ һ����������Ϊnull������ĳɶԵ����鳤�Ȳ�һ�¡����ݿ��������<br>
	 * not null �ɹ�<br>
	 */
	public ArrayList<HashMap<String, String>> select(String [] query, String [] condition, String [] conditionVal, String operator)
	{
		if (query == null || condition == null || conditionVal == null)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tQuery, conditions or values null." : "");
			return null;
		}
		if (condition.length != conditionVal.length)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tConditions length and values length are not equal." : "");
			return null;
		}
		
		if (operator == null)
		{
			operator = "=";
			System.out.print(DEBUG_MODEL ? "Warning: Default operator is used \"=\"" : "");
		}
		
		ArrayList<HashMap<String, String>> list = null;
		
		try
		{
			Statement s1 = connection.createStatement();
			ResultSet rs = s1.executeQuery("SELECT " + joinStatement(query, ",", false) + " FROM " + TABLE_NAME + " WHERE " + joinConditionStatement(condition, conditionVal, "and", operator));
			
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
			
			System.out.print(DEBUG_MODEL ? "Select success. Return " + list.size() + " results." : "");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	
	/**
	 * ����debugģʽ
	 * @param en true��ʾ����Ϊdebugģʽ����<b>��</b>��ӡ�����е�����Ϣ<br>
	 * false ��ʾ��ֹdebugģʽ����<b>����</b>��ӡ�κε�����Ϣ
	 */
	public void enableDebugModel(boolean en)
	{
		this.DEBUG_MODEL = en;
	}
	
	/**
	 * ִ��ԭʼ��SQL���<br>
	 * <b>����:</b><br>
	 * excecuteRawQuery("select id, name from tableA where age = 1", new String [] {"id", "name"})
	 * @param rawSqlStatment SQL���
	 * @param query ����ѯ����������Ӧ��SQL�����select�е�����һ һ��Ӧ��
	 * @return ��ѯ���<br>
	 * null ʧ�ܣ���ʾ һ����������Ϊnull������ĳɶԵ����鳤�Ȳ�һ�¡����ݿ��������<br>
	 * not null �ɹ�<br>
	 */
	public ArrayList<HashMap<String, String>> excecuteRawQuery(String rawSqlStatment, String [] query)
	{
		if (rawSqlStatment == null || query == null)
		{
			System.out.print(DEBUG_MODEL ? "Error:\tRawSqlStatment or query null." : "");
			return null;
		}
		
		ArrayList<HashMap<String, String>> list = null;
		
		try
		{
			Statement s1 = connection.createStatement();
			ResultSet rs = s1.executeQuery(rawSqlStatment);
			
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
				System.out.print(DEBUG_MODEL ? "\tRawSqlStatment success." : "");
			}
		} catch (SQLException e) {
			list = null;
			System.out.print(DEBUG_MODEL ? "Error:\tRawSqlStatment failed." : "");
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * ���ҵ�ǰ���е����µ�IDֵ
	 * @return ���µ�IDֵ<br>
	 * -1��ʾ����ʧ��
	 */
	public int getLatestID()
	{
		String sql = "SELECT IDENT_CURRENT('" + TABLE_NAME + "') as id";
		
		try 
		{
			Statement s1 = connection.createStatement();
			ResultSet rs = s1.executeQuery(sql);

			if (rs != null)
			{
				while (rs.next())
				{
					return rs.getInt("id");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
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
			System.out.print(list.get(i).get("id") + "\t" + list.get(i).get("name"));
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
        	System.out.print(rs.getString("username") + "\t" + rs.getString("plainPassword") + "\t" 
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
