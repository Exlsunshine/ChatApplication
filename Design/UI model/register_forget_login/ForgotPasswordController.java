/**
 * 
 * @author LiJian
 *
 */
public class ForgotPasswordController
{
	/**
	 * 提示用户 邮件是否发送成功
	 * @param sendstatus 邮件发送状态。由函数 sendEmailStatus 获得。<br>
	 * SUCCESS：邮件发送成功<br>
	 * ERROR_NETWORK：网络错误<br>
	 * @see ConstantValues
	 */
	public void remindUserForEmail(int sendstatus) {
	}


	/**
	 * 提示用户 验证码是否正确
	 * @param codestatus 验证码正确性。由函数 checkCodeStatus 获得。<br>
	 * SUCCESS：验证码正确<br>
	 * ERROR_CAPTCHA：验证码错误<br>
	 * ERROR_NETWORK：网络错误<br>
	 * @see ConstantValues
	 */
	public void remindUserForCode(int codestatus);

	/**
	 * 提示用户 密码修改状态
	 * @param resetstatus 密码修改状态。由函数 resetPassword 获得。<br>
	 * SUCCESS：修改成功。<br>
	 * ERROR_PASSWOR_DIFFERENT：用户输入的两次密码不一致。<br>
	 * ERROR_NETWORK：网络错误<br>
	 * @see ConstantValues
	 */
	public void remindUserForResetPassword(int resetstatus);
}

public class ForgotPasswordModel
{

	/**
	 * 通知服务器向用户邮箱发送验证码邮件
	 * @param email 用户邮箱字符串
	 * @return 邮件发送状态.<br>
	 * SUCCESS:发送成功<br>
	 * ERROR_NETWORK：网络错误<br>
	 * @see ConstantValues
	 */
	public int sendEmail(String email) {
		return 0;
	}//发送邮件 返回值：true 发送成功；false：发送失败


	/**
	 * 检测验证码正确性
	 * @param code 验证码
	 * @return 验证码正确性。<br>
	 * SUCCESS：验证码正确<br>
	 * ERROR_CAPTCHA：验证码错误<br>
	 * ERROR_NETWORK：网络错误<br>
	 * @see ConstantValues
	 */
	public int checkCode(int code) {
		return 0;
	}


	/**
	 * 重置密码
	 * @param email 用户邮箱字符串
	 * @param password 用户密码字符串
	 * @param passwordagain 用户再次输入密码字符串
	 * @return 重置密码状态<br>
	 * SUCCESS：修改成功。<br>
	 * ERROR_PASSWOR_DIFFERENT：用户输入的两次密码不一致。<br>
	 * ERROR_NETWORK：网络错误<br>
	 * @see ConstantValues
	 */
	public int resetPassword(String email, String password, String passwordagain);
}