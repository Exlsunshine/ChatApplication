/**
 * 
 * @author LiJian
 *
 */
public abstract class LoginController
{
	/**
	 * 从本地获取用户先前输入并成功登陆的 账号&密码，并填入 账号&密码 文本框中
	 */
	public void autoFill();

	/**
	 * 提示用户密码错误
	 */
	public void remindUserForWrongInput() {
	}

	/**
	 * 跳转到 主Activity
	 */
	public void startMainActivity(); //checkUserIdentity 为 true 时，执行。跳转主界面
	
	/**
	 * 跳转到注册Activity
	 */
	public void startRegisterActivity();//跳转注册界面

	/**
	 * 跳转到密码找回Activity
	 */
	public void startForgotActivity();//跳转密码找回界面
}



public class LoginModel
{
	/**
	 * 从本地提取用户先前输入并成功登陆的 账号&密码
	 * @return [description] 
	 */
	/**
	 * 从本地提取用户先前输入并成功登陆的 账号&密码
	 * @return 长度为 2 的字符串数组。<br>
	 * Index[INDEX_LOGINACCOUNT]：账号字符串<br>
	 * Index[INDEX_PASSWORD]：密码字符串<br>
	 * @see ConstantValues
	 */
	public String[] findCookie() {
		return null;
	}


	/**
	 * 检测用户 账号密码的正确性
	 * @param loginaccount 用户账号字符串
	 * @param password 用户密码字符串
	 * @return 用户 账号密码的正确性；<br>
	 * SUCCESS：账号密码正确<br>
	 * ERROR_PASSWOR_WRONG：账号密码错误<br>
	 * ERROR_NETWORK：网络连接错误<br>
	 */
	public int checkUserIdentity(String loginaccount, String password) {
		return 0;
	} //检测账号密码



	/**
	 * 将用户账号&密码存入本机
	 * @param loginaccount 待存用户账号字符串
	 * @param password 待存用户密码字符串
	 */
	public void setCookie(String loginaccount, String password) {
	}


	/**
	 * 通过 loginaccount 初始化User类
	 * @param loginaccount 用户账号
	 * @see User
	 */
	public void initUserData(String loginaccount);
}