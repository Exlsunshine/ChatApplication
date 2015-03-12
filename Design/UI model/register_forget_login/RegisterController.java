/**
 * 
 * @author LiJian
 *
 */
public class RegisterController
{
	/**
	 * 提示用户注册状态
	 * @param registerStatus 用户注册状态由函数 registerUser 获得。<br>
	 * SUCCESS：注册成功<br>
	 * ERROR_REG_LOGINACCOUNT_OCCUPY：用户名被占用<br>
	 * ERROR_PASSWOR_DIFFERENT：两次输入密码不一致<br>
	 * ERROR_NETWORK：网络问题<br>
	 * @see ConstantValues
	 */
	public void remindUserForRegister(int registerStatus) {
	}

	/**
	 * 返回登陆Activity
	 */
	public void returnLoginActivity();
}

public class RegisterModel
{
	/**
	 * 注册新用户
	 * @param loginaccount 用户登陆账号。与email相同
	 * @param email 邮箱
	 * @param password 密码
	 * @param passwordagain 第二次密码
	 * @param nickname 昵称
	 * @param sex 性别
	 * @param portrait 头像
	 * @return 注册状态
	 * SUCCESS：注册成功<br>
	 * ERROR_REG_LOGINACCOUNT_OCCUPY：用户名被占用<br>
	 * ERROR_PASSWOR_DIFFERENT：两次输入密码不一致<br>
	 * ERROR_NETWORK：网络问题<br>
	 * @see ConstantValues
	 */
	public int registerUser(String loginaccount, String email, String password, String passwordagain, String nickname, String sex, BitMap portrait);//注册新用户，返回值：注册状态： 0：注册成功；1：用户名重复；2：密码两次不一致

	/**
	 * 对密码加密
	 * @param password 待加密密码
	 * @return 加密后字符串
	 */
	private String encryptPassword(String password);
}