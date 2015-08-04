package com.yg.ui.login;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.lj.networktest.ActivityNetworkError;
import com.lj.networktest.ThreadNetworkTest;
import com.yg.commons.ConstantValues;
import com.yg.ui.MainActivity;
import com.yg.ui.login.implementation.CrossingAnimation;
import com.yg.ui.login.implementation.ForgetImplementation;
import com.yg.ui.login.implementation.LoginImplementation;
import com.yg.ui.login.implementation.LoginInfo;
import com.yg.ui.signup.SignupActivity;

public class LoginGuideActivity extends Activity
{
//	private static final String DEBUG_TAG = "LoginGuideActivity______";
	private AlertDialog loginDialog, forgotDialog, signupDialog, testNetworkDialog;
	
	private ViewPager viewPager;
	private ImageView dot0;
	private ImageView dot1;
	private ImageView dot2;
	private ImageView dot3;
	ArrayList<View> pageViewItems = new ArrayList<View>();
	//private int currPageIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_loginguide_activity);
		
		setupLayout();
		setupLayoutListeners();
		loadViewpagerData();

		Bundle bundle = this.getIntent().getExtras();
		viewPager.setCurrentItem(bundle == null ? 0 : bundle.getInt("index", 0));
		
		if (bundle != null)
		{
			boolean autoLogin = bundle.getBoolean(WelcomeActivity.AUTO_LOGIN_FLAG, false);
			
			if (autoLogin)
			{
				showLoginDialog();
				Window window = loginDialog.getWindow();
				EditText email = (EditText)window.findViewById(R.id.yg_loginguide_page3_dialog_email);
				EditText password = (EditText)window.findViewById(R.id.yg_loginguide_page3_dialog_password);
				
				LoginInfo loginInfo = new LoginInfo(LoginGuideActivity.this);
				email.setText(loginInfo.getLoginAccount());
				password.setText(loginInfo.getLoginPassword());
			}
		}
	}
	
	private void showLoginDialog()
	{
		loginDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		
		loginDialog.setCanceledOnTouchOutside(true);
		loginDialog.show();
		loginDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		loginDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = loginDialog.getWindow();
		window.setContentView(R.layout.yg_loginguide_page3_login_dialog);
		
		Button forgot = (Button) window.findViewById(R.id.yg_loginguide_page3_dialog_forgot);
		forgot.setOnClickListener(new onForgotBtnClickListener());
		
		Button login = (Button) window.findViewById(R.id.yg_loginguide_page3_dialog_login);
		login.setOnClickListener(new onLoginBtnClickListener());
	}
	
	private void showForgotDialog()
	{
		forgotDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		
		forgotDialog.setCanceledOnTouchOutside(true);
		forgotDialog.show();
		forgotDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		forgotDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = forgotDialog.getWindow();
		window.setContentView(R.layout.yg_loginguide_page3_forget_dialog);
		
		Button cancel = (Button) window.findViewById(R.id.yg_loginguide_page3_forgot__dialog_cancel);
		cancel.setOnClickListener(new onCancelBtnClickListener());
		
		Button reset = (Button) window.findViewById(R.id.yg_loginguide_page3_forgot__dialog_reset);
		reset.setOnClickListener(new onResetBtnClickListener());
	}
	
	private void showSignupDialog()
	{
		signupDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		signupDialog.setCanceledOnTouchOutside(true);
		signupDialog.show();
		signupDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		signupDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		Window window = signupDialog.getWindow();
		window.setContentView(R.layout.yg_loginguide_page3_signup_dialog);

		setupSignupListeners(window);
	}
	
	private void setupSignupListeners(final Window window)
	{
		final Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
		final EditText email = (EditText)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_email);
		final EditText nickname = (EditText)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_nickname);
		final EditText password = (EditText)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_password);
		
		signup.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(LoginGuideActivity.this, SignupActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("email", email.getText().toString());
				bundle.putString("nickname", nickname.getText().toString());
				bundle.putString("password", password.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		
		email.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{
				if (email.getText().toString().length() > 0 && nickname.getText().toString().length() > 0 && password.getText().toString().length() > 0)
				{
					TextView hint = (TextView)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_hint);
					hint.setVisibility(View.GONE);
					
					Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
					signup.setVisibility(View.VISIBLE);
				}
				else
				{
					TextView hint = (TextView)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_hint);
					hint.setVisibility(View.VISIBLE);
					
					Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
					signup.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
			
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
		
		nickname.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{
				if (email.getText().toString().length() > 0 && nickname.getText().toString().length() > 0 && password.getText().toString().length() > 0)
				{
					TextView hint = (TextView)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_hint);
					hint.setVisibility(View.GONE);
					
					Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
					signup.setVisibility(View.VISIBLE);
				}
				else
				{
					TextView hint = (TextView)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_hint);
					hint.setVisibility(View.VISIBLE);
					
					Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
					signup.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
			
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
		
		password.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{
				if (email.getText().toString().length() > 0 && nickname.getText().toString().length() > 0 && password.getText().toString().length() > 0)
				{
					TextView hint = (TextView)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_hint);
					hint.setVisibility(View.GONE);
					
					Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
					signup.setVisibility(View.VISIBLE);
				}
				else
				{
					TextView hint = (TextView)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_hint);
					hint.setVisibility(View.VISIBLE);
					
					Button signup = (Button)window.findViewById(R.id.yg_loginguide_page3_signup_dialog_signup);
					signup.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
			
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
	}
	
	private class onCancelBtnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0)
		{
			forgotDialog.cancel();
		}
	}
	
	private class onResetBtnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			Window window = forgotDialog.getWindow();
			EditText email = (EditText)window.findViewById(R.id.yg_loginguide_page3_forgot__dialog_email);
			String emailStr = email.getText().toString();
			ForgetImplementation forget = new ForgetImplementation(emailStr);
			int result = forget.tryToReset();

			switch (result)
			{
			case 0:
				Toast.makeText(LoginGuideActivity.this, "新密码已经发送到 " + emailStr, Toast.LENGTH_SHORT).show();
				forgotDialog.cancel();
				break;
			case 1:
				Animation shake = AnimationUtils.loadAnimation(LoginGuideActivity.this, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
				email.startAnimation(shake);
				break;
			case 2:
				Toast.makeText(LoginGuideActivity.this, "邮箱尚未注册过，输错邮箱了?", Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		}
	}
	
	private class onForgotBtnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			loginDialog.cancel();
			showForgotDialog();
		}
	} 
	
	private void hideInputManager()
	{
		try
		{
			Window window = loginDialog.getWindow();
			View view = window.getCurrentFocus();
			if (view != null)
			{  
			    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}
	
	private class onLoginBtnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			final Window window = loginDialog.getWindow();
			final EditText email = (EditText)window.findViewById(R.id.yg_loginguide_page3_dialog_email);
			final EditText password = (EditText)window.findViewById(R.id.yg_loginguide_page3_dialog_password);
			final String emailStr = email.getText().toString();
			final String pwdStr = password.getText().toString();
			
			LoginImplementation login = new LoginImplementation(emailStr, pwdStr);
			int result = login.tryToLogin();
			
			Animation shake = AnimationUtils.loadAnimation(LoginGuideActivity.this, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
			switch (result)
			{
			case 0:
				hideInputManager();
				
				//add loading animation
				runOnUiThread(new Runnable() 
				{
					public void run()
					{
						loginDialog.setCancelable(false);
						LinearLayout buttonsLayout = (LinearLayout)window.findViewById(R.id.yg_loginguide_page3_dialog_login_button_layout);
						buttonsLayout.setVisibility(View.GONE);
						email.setVisibility(View.GONE);
						password.setVisibility(View.GONE);
						
						RelativeLayout loadingLayout = (RelativeLayout)window.findViewById(R.id.yg_loginguide_page3_dialog_login_loading_layout);
						loadingLayout.setVisibility(View.VISIBLE);
						ImageView leftImg = (ImageView)window.findViewById(R.id.yg_loginguide_page3_dialog_login_loading_layout_left_icon);
						ImageView rightImg = (ImageView)window.findViewById(R.id.yg_loginguide_page3_dialog_login_loading_layout_right_icon);
						
						CrossingAnimation ca = new CrossingAnimation(leftImg, rightImg);
						ca.startAnimation();
					}
				});
				
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						//load data first
						try {
							Thread.sleep(2000);
							ConstantValues.user.setContext(LoginGuideActivity.this);
							ConstantValues.user.getFriendList();
							ConstantValues.user.getRecentDialogs();
							
							LoginInfo loginInfo = new LoginInfo(LoginGuideActivity.this);
							loginInfo.saveLoginAccount(emailStr);
							loginInfo.saveLoginPassword(pwdStr);
							
							Intent intent = new Intent(LoginGuideActivity.this, MainActivity.class);
							startActivity(intent);
							LoginGuideActivity.this.finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				});
				td.start();
				
				break;
			case 1:
				email.startAnimation(shake);
				break;
			case 2:
				password.startAnimation(shake);
				break;
			case 3:
				email.startAnimation(shake);
				password.startAnimation(shake);
				break;
			case 4:
				email.startAnimation(shake);
				password.startAnimation(shake);
				Toast.makeText(LoginGuideActivity.this, "密码或邮箱错误!", Toast.LENGTH_LONG).show();
				break;
			case 5:
				Toast.makeText(LoginGuideActivity.this, "未知错误!", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}
	
	private void setupLayout()
	{
		viewPager = (ViewPager) findViewById(R.id.loginguide_viewpager);
		
		dot0 = (ImageView) findViewById(R.id.loginguide_dot0);
		dot1 = (ImageView) findViewById(R.id.loginguide_dot1);
		dot2 = (ImageView) findViewById(R.id.loginguide_dot2);
		dot3 = (ImageView) findViewById(R.id.loginguide_dot3);
		
		LayoutInflater inflater = LayoutInflater.from(LoginGuideActivity.this);
		View page0View = inflater.inflate(R.layout.yg_loginguide_page0, null);
		View page1View = inflater.inflate(R.layout.yg_loginguide_page1, null);
		View page2View = inflater.inflate(R.layout.yg_loginguide_page2, null);
		View page3View = inflater.inflate(R.layout.yg_loginguide_page3, null);
		pageViewItems.add(page0View);
		pageViewItems.add(page1View);
		pageViewItems.add(page2View);
		pageViewItems.add(page3View);
	}
	
	private void setupLayoutListeners()
	{
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		
		Button login = (Button)pageViewItems.get(3).findViewById(R.id.loginguide_page3_login_btn);
		login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
			//	showLoginDialog();
				showTestNetworkDialog(LOGIN_BUTTON);
			}
		});
		
		Button signup = (Button)pageViewItems.get(3).findViewById(R.id.loginguide_page3_signup_btn);
		signup.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
		//		showSignupDialog();
				showTestNetworkDialog(SIGNUP_BUTTON);
			}
		});
	}
	
	private void loadViewpagerData()
	{
		PagerAdapter mPagerAdapter = new PagerAdapter() 
		{
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) 
			{
				return arg0 == arg1;
			}

			@Override
			public int getCount() { return pageViewItems.size(); }

			@Override
			public void destroyItem(View container, int position, Object object)
			{
				((ViewPager) container).removeView(pageViewItems.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) 
			{
				((ViewPager) container).addView(pageViewItems.get(position));
				return pageViewItems.get(position);
			}
		};

		viewPager.setAdapter(mPagerAdapter);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageSelected(int arg0)
		{
			switch (arg0)
			{
			case 0:
				dot0.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot3.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			case 1:
				dot0.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot3.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			case 2:
				dot0.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot3.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			case 3:
				dot0.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot3.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				break;
			}
			//currPageIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageScrollStateChanged(int arg0) { }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	//---------------------------------- LJ -----------------------------------------------
	
	private static final int LOGIN_BUTTON = 0;
	private static final int SIGNUP_BUTTON = 1;
	
	private Handler gHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			if (msg.what == ThreadNetworkTest.NETWORK_ERROR)
			{
				Toast.makeText(getApplicationContext(), "当前网络环境不佳", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(LoginGuideActivity.this, ActivityNetworkError.class);
				startActivity(intent);
				testNetworkDialog.dismiss();
			}
			else if (msg.what == ThreadNetworkTest.NETWORK_CORRECT)
			{
				Toast.makeText(getApplicationContext(), "当前网络环境良好", Toast.LENGTH_SHORT).show();
				testNetworkDialog.dismiss();
				if (msg.arg1 == LOGIN_BUTTON)
					showLoginDialog();
				else if (msg.arg1 == SIGNUP_BUTTON)
					showSignupDialog();
			}
		}
	};
	
	
	private void showTestNetworkDialog(int buttonType)
	{
		testNetworkDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		
		testNetworkDialog.setCanceledOnTouchOutside(true);
		testNetworkDialog.show();
		testNetworkDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		testNetworkDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		testNetworkDialog.setCancelable(false);
		Window window = testNetworkDialog.getWindow();
		window.setContentView(R.layout.lj_testnetworkconnect_dialog);
		RelativeLayout loadingLayout = (RelativeLayout)window.findViewById(R.id.lj_loginguide_page3_dialog_login_loading_layout);
		loadingLayout.setVisibility(View.VISIBLE);
		ImageView leftImg = (ImageView)window.findViewById(R.id.lj_loginguide_page3_dialog_login_loading_layout_left_icon);
		ImageView rightImg = (ImageView)window.findViewById(R.id.lj_loginguide_page3_dialog_login_loading_layout_right_icon);
		
		CrossingAnimation ca = new CrossingAnimation(leftImg, rightImg);
		ca.startAnimation();
		Toast.makeText(getApplicationContext(), "正在测试网络环境，请稍后", Toast.LENGTH_SHORT).show();
		
		new ThreadNetworkTest(gHandler, buttonType).start();
	}
}