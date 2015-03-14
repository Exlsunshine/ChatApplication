package com.user;

public class FriendUser extends AbstractUser
{

	public FriendUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
					String sex, String birthday, byte[] portrait, String hometown)
	{
		super(id, loginAccount, nickName, email, phoneNumber, sex, birthday, portrait, hometown);
	}
}