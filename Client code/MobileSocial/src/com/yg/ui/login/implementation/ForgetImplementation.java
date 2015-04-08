package com.yg.ui.login.implementation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.util.Log;

import com.yg.user.WebServiceAPI;

public class ForgetImplementation 
{
	private static final String DEBUG_TAG = "ForgetImplementation______";
	private String emailAccount;
	private WebServiceAPI wsAPI = new WebServiceAPI("network.com",
			"NetworkHandler");

	public ForgetImplementation(String emailAccount)
	{
		this.emailAccount = emailAccount;
	}

	/**
	 * 尝试向服务器发起重置密码请求
	 * 
	 * @return 服务器处理结果：<br>
	 *         1 表示指定的邮箱为null或长度为零<br>
	 *         2 表示指定的邮箱尚未注册<br>
	 *         3 表示未知异常<br>
	 *         0 表示重置密码成功
	 */
	public int tryToReset() 
	{
		ExecutorService pool = Executors.newFixedThreadPool(1);

		Callable<Integer> callable = new SendRequestToServer(emailAccount);
		Future<Integer> future = pool.submit(callable);
		int result = 3;
		try {
			result = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.printf("result is : ", result);

		return result;
	}

	private class SendRequestToServer implements Callable<Integer> 
	{
		private String emailAccount;

		public SendRequestToServer(String emailAccount) 
		{
			this.emailAccount = emailAccount;
		}

		public Integer call() 
		{
			int status = -1;

			if (emailAccount == null || emailAccount.length() == 0)
			{
				// Toast.makeText(Forget.this, "Please specify your email.",
				// Toast.LENGTH_SHORT).show();
				status = 1;
			} 
			else 
			{
				String[] params = new String[1];
				Object[] vlaues = new Object[1];
				params[0] = "email";
				vlaues[0] = emailAccount;

				Object ret = wsAPI.callFuntion("sendResetPwdRequestMail", params, vlaues);
				int code = Integer.parseInt(ret.toString());
				Log.i(DEBUG_TAG, "Reset password status: " + String.valueOf(code));
				if (code != 0)
					status = 2;// Toast.makeText(Forget.this,
								// "This email has not been registered.",
								// Toast.LENGTH_SHORT).show();
				else
					status = 0;// Toast.makeText(Forget.this,
								// "Reset instruction has been sent to " +
								// emailAccount, Toast.LENGTH_SHORT).show();
			}
			return status;
		}
	}
}