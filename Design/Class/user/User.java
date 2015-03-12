package com.doc;
import java.util.ArrayList;

import com.Message.AbstractMessage;
import com.Message.Dialog;
import com.game.AbstractGame;

public class User
{
	public User(String loginAccount);

	/**
	 * �����û�����
	 * @param password ����
	 */
	public void setPassword(String password);
	
	/**
	 * �����û��ǳ�
	 * @param nickName �ǳ�
	 */
	public void setNickName(String nickName);
	
	/**
	 * �����û�����
	 * @param email ����
	 */
	public void setEmail(String email);
	
	/**
	 * �����û��Ա�
	 * @param sex �Ա�
	 */
	public void setSex(String sex);
	
	/**
	 * �����û�����
	 * @param birthday ����
	 */
	public void setBirthday(String birthday);
	
	/**
	 * �����û�ͷ��
	 * @param portraitPath �û�ͷ���·��
	 */
	public void setPortrait(String portraitPath);
	
	/**
	 * �����û�����
	 * @param hometown ����
	 */
	public void setHometown(String hometown);
	
	/**
	 * �����û��ֻ���
	 * @param phoneNumber �ֻ���
	 */
	public void setPhoneNumber(String phoneNumber);
	
	/**
	 * ��ȡ�û��ǳ�
	 * @return �ǳ�
	 */
	public String getNickName();
	
	/**
	 * ��ȡ�û�����
	 * @return ����
	 */
	public String getEmail();

	/**
	 * ��ȡ�û��Ա�
	 * @return �Ա�
	 */
	public String getSex();
	
	/**
	 * ��ȡ�û�����
	 * @return ����
	 */
	public String getBirthday();
	
	/**
	 * ��ȡ�û�ͷ��
	 * @return ͷ���ڷ������е�·��������ȶ��
	 */
	public String getPortrait();
	
	/**
	 * ��ȡ�û�����
	 * @return ����
	 */
	public String getHometown();
	
	/**
	 * ��ȡ�û��ֻ���
	 * @return �ֻ���
	 */
	public String getPhoneNumber();
	
	/**
	 * ��ȡ�û���½�˺�
	 * @return ��½�˺�
	 */
	public String getLoginAccount();

	/**
	 * ��½
	 */
	public void signin();
	
	/**
	 * ע��
	 */
	public void signoff();

	/**
	 * ��ʼҡһҡ
	 */
	public void shakeAround();
	
	/**
	 * ��other����û�����һ���Ի�
	 * @param other ��Ҫ��֮�����Ի���Ŀ���û�
	 */
	public void loadDialogWith(User other);
	
	/**
	 * ��msg������Ϣ���͸�other����û�
	 * @param other �����յ�Ŀ���û�
	 * @param msg �����͵���Ϣ
	 * @return true ���ͳɹ�<br>
	 * false ����ʧ��
	 */
	public boolean sendMsgTo(User other,AbstractMessage msg);
	

	/**
	 * ���³�ʼ���ö����������ݴ��´ӷ������򱾵ػ�ȡ��
	 */
	public void reloadEverything();
	
	/**
	 * ��other����û�����һ������alias
	 * @param other �����õ�Ŀ���û�
	 * @param alias ����
	 */
	public void setAlias(User other, String alias);
	
	/**
	 * ��other����û����õ�groupName�������
	 * @param other �����õ�Ŀ���û�
	 * @param groupName ��������
	 */
	public void moveFriendToGroup(User other, Stirng groupName);
	
	/**
	 * ��other����û���Ϊ�Ǳ��û�
	 * @param other �����õ�Ŀ���û�
	 */
	public void markCloseFriend(User other);
	
	/**
	 * ��other����û����������
	 * @param other �����õ�Ŀ���û�
	 */
	public void blockUser(User other);
	
	/**
	 * ��other����û�������ѹ�ϵ
	 * @param other �����õ�Ŀ���û�
	 */
	public void deleteUser(User other);
	
	/**
	 * ��ȡ�����б�
	 * @return ��ǰ�û������к���
	 */
	public ArrayList<User> getFriendList()
	{
		//str = get "JSON string" from server
		try
		{
			PackString ps = new PackString(str);
			ArrayList<Map<String, Object>> result = ps.jsonString2Arrylist("friends");
			for (int i = 0; i < result.size(); i++)
			{
				Map<String, Object> map = result.get(i);
				for (String key : map.keySet())
					System.out.print(key + "\t" + map.get(key) + "\t");
				System.out.println();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�����������б��ضԻ�
	 * @return �������жԻ�
	 */
	public ArrayList<Dialog> getRecentDialog();
	
	/**
	 * �ӷ�������֤�û����
	 * @return true �����֤ͨ��<br>
	 * false �����֤δͨ��
	 */
	public boolean identityVarified();


	private ArrayList<User> friendList;

	///////////////////////////////			Constant values			/////////////////////////////
}}