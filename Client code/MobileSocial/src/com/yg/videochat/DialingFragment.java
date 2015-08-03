package com.yg.videochat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.yg.user.WebServiceAPI;

public class DialingFragment extends Fragment
{
	private View view;
	private static final String DEBUG_TAG = "DialingFragment______";
	private LinearLayout back;
	private ImageView hangupOrFind;
	private TextView hint;
	private TextView status;
	private ImageView portrait;
	private Timer portraitTimer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.yg_video_chat_dial_fragmet, container, false);
		setupLayout();
		setupListeners();
		
		return view;
	}
	
	private void setupLayout()
	{
		back = (LinearLayout) view.findViewById(R.id.yg_activity_video_chat_back);
		hangupOrFind = (ImageView) view.findViewById(R.id.yg_activity_video_chat_hangup_or_find);
		hint = (TextView) view.findViewById(R.id.yg_activity_video_chat_inner_hint);
		status = (TextView) view.findViewById(R.id.yg_activity_video_chat_inner_status);
		portrait = (ImageView) view.findViewById(R.id.yg_activity_video_chat_inner_portrait);
	}
	
	private void setupListeners()
	{
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if (QBChatService.isInitialized()) 
				{
					try 
					{
						QBRTCClient.getInstance().close(true);
						QBChatService.getInstance().logout();
						QBChatService.getInstance().destroy();
					} catch (SmackException.NotConnectedException e) {
						e.printStackTrace();
					}
				}
				getActivity().finish();
			}
		});
		
		hangupOrFind.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch (currentStatus)
				{
				case GET_ACCOUNT_REQUEST:
					getAccountTask = new GetAccountFromServerTask();
					getAccountTask.execute();
					break;
				case ABORT_GET_ACCOUNT_REQUEST:
					status.setText("开启狂欢之旅");
					hint.setText("开始寻找新朋友");
					getAccountTask.cancel(true);
					currentStatus = GET_ACCOUNT_REQUEST;
					break;
				case LOGIN_REQUEST:
					loginTask = new LoginTask();
					loginTask.execute();
					break;
				case ABORT_LOGIN_REQUEST:
					status.setText("请先登陆至服务器");
					hint.setText("登陆");
					loginTask.cancel(true);
					currentStatus = LOGIN_REQUEST;
					break;
				case ACCEPT_REQUEST:
					acceptMatchingTask = new AcceptMatchingTask();
					acceptMatchingTask.execute();
					break;
				case ABORT_ACCEPT_REQUEST:
					status.setText("已进入房间!");
					hint.setText("开始入座");
					acceptMatchingTask.cancel(true);
					currentStatus = ACCEPT_REQUEST;
				case MATCH_REQUEST:
					matchTask = new MatchTask();
					matchTask.execute();
					break;
				case ABORT_MATCH_REQUEST:
					status.setText("已就坐!");
					hint.setText("开始寻找Ta");
					matchTask.cancel(true);
					matchTask = null;
					currentStatus = MATCH_REQUEST;
					break;
				default:
					break;
				}
			}
		});
		
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				
				switch (msg.what)
				{
				case UPDATE_UI_LOGIN_FAILED:
					status.setText("登陆失败!");
					hint.setText("点击重新登陆");
					hangupOrFind.setClickable(true);
					break;
				case UPDATE_UI_LOGIN_SUCCESS:
					status.setText("登陆成功!");
					hint.setText("点击开始接收匹配");
					hangupOrFind.setClickable(true);
					hangupOrFind.performClick();
					break;
				case UPDATE_UI_PORTRAIT:
					excecutePortraitAnim();
					break;
				default:
					break;
				}
			}
		};
		
		portraitTimer = new Timer();
		portraitTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(UPDATE_UI_PORTRAIT);
			}
		}, 1000, 4500);
	}
	
	private AnimatorSet portraitFlipOutAnim;
	private AnimatorSet portraitFlipInAnim;
	private void excecutePortraitAnim()
	{
		if (getActivity() == null)
			return;
		
		portraitFlipOutAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.yg_video_chat_portrait_flip_out);
		portraitFlipOutAnim.setTarget(portrait);
		portraitFlipOutAnim.start();
		
		portraitFlipOutAnim.addListener(new AnimatorListener()
		{
			@Override
			public void onAnimationEnd(Animator arg0)
			{
				portrait.setImageResource(PortraitAnimation.getInstance().getNextSelection());
				if (getActivity() == null)
					return;
				portraitFlipInAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.yg_video_chat_portrait_flip_in);
				portraitFlipInAnim.setTarget(portrait);
				portraitFlipInAnim.start();
			}
			
			@Override
			public void onAnimationStart(Animator arg0) {}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {}
			
			@Override
			public void onAnimationCancel(Animator arg0) {}
		});
	}
	
	private GetAccountFromServerTask getAccountTask;
	private LoginTask loginTask;
	private AcceptMatchingTask acceptMatchingTask;
	private MatchTask matchTask;
	
	private int currentStatus = GET_ACCOUNT_REQUEST;
	private Handler handler;
	private String login;
	private String password;
	private boolean acceptMatching = false;
	public final static String PACKAGE_NAME = "network.com";
	public final static String CLASS_NAME = "NetworkHandler";
	public final static String LOGIN_ACCOUNT = "login";
	public final static String LOGIN_PASSWORD = "password";

	private static final int GET_ACCOUNT_REQUEST = 0x01;
	private static final int LOGIN_REQUEST = 0x02;
	private static final int ACCEPT_REQUEST = 0x03;
	private static final int MATCH_REQUEST = 0x04;
	private static final int ABORT_GET_ACCOUNT_REQUEST = 0x05;
	private static final int ABORT_LOGIN_REQUEST = 0x06;
	private static final int ABORT_ACCEPT_REQUEST = 0x07;
	private static final int ABORT_MATCH_REQUEST = 0x08;
	
	private static final int SUCCESS = 0x01;
	private static final int MATCH_ERROR = 0x0A;
	private static final int JSON_ERROR = 0x0B;
	private static final int UPDATE_UI_LOGIN_FAILED = 0x0C;
	private static final int UPDATE_UI_LOGIN_SUCCESS = 0x0d;
	private static final int UPDATE_UI_PORTRAIT = 0x0e;
	
	private boolean isStringEmpty(String str)
	{
		if (str == null || str.length() == 0 || str.equals(""))
			return true;
		
		return false;
	}
	
	private class GetAccountFromServerTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
			status.setText("正在获取账号...");
			hint.setText("停止获取账号");
			
			currentStatus = ABORT_GET_ACCOUNT_REQUEST;
		}
		
		@Override
		protected Boolean doInBackground(Void... params)
		{
			if (!isStringEmpty(login) && !isStringEmpty(password))
				return true;
			
			WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
			Object ret = wsAPI.callFuntion("getAvailableAccount");
			
			if (isStringEmpty(ret.toString()))
				return false;
			
			try 
			{
				JSONObject obj = new JSONObject(ret.toString());
				login = (String) obj.get(LOGIN_ACCOUNT);
				password = (String) obj.get(LOGIN_PASSWORD);
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			
			if (result)
			{
				status.setText("已获得账号!");
				hint.setText("点击登陆");
				Log.i(DEBUG_TAG, "Login: " + login);
				Log.i(DEBUG_TAG, "Password: " + password);
				currentStatus = LOGIN_REQUEST;
				
				hangupOrFind.performClick();
			}
			else
			{
				status.setText("获取账号失败!");
				hint.setText("重新获取账号");
				currentStatus = GET_ACCOUNT_REQUEST;
			}
		}
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
			status.setText("正在拼命登陆中...");
			hint.setText("请您耐心等待:)");
			hangupOrFind.setClickable(false);
			
			currentStatus = ABORT_LOGIN_REQUEST;
			
			((VideoChatActivity) getActivity()).login(login, password, new LoginFeedbackListener()
			{
				@Override
				public void onGotLoginFeedback(int result)
				{
					if (result == VideoChatActivity.LOGIN_SUCCESS)
					{
						Log.i(DEBUG_TAG, "Success login.");
						currentStatus = ACCEPT_REQUEST;
						handler.sendEmptyMessage(UPDATE_UI_LOGIN_SUCCESS);
					}
					else
					{
						Log.i(DEBUG_TAG, "Failed login.");
						currentStatus = LOGIN_REQUEST;
						handler.sendEmptyMessage(UPDATE_UI_LOGIN_FAILED);
					}
				}
			});
		}
		
		@Override
		protected Boolean doInBackground(Void... params) 
		{
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
		}
	}
	
	private class AcceptMatchingTask extends AsyncTask<Void, Void, Integer>
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			hint.setText("取消");
			status.setText("正在入座...");
			
			currentStatus = ABORT_ACCEPT_REQUEST;
		}
		
		@Override
		protected Integer doInBackground(Void... params) 
		{
			if (!acceptMatching)
			{
				try
				{
					JSONObject obj = new JSONObject();
					obj.put(LOGIN_ACCOUNT, login);
					obj.put(LOGIN_PASSWORD, password);
					
					String [] param = new String[1];
					Object [] vlaue = new Object[1];
					param[0] = "callerUserInfo";
					vlaue[0] = obj.toString();
					
					WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
					Object ret = wsAPI.callFuntion("acceptMatching", param, vlaue);
					
					int result = Integer.parseInt(ret.toString());
					
					if (result != SUCCESS)
						return MATCH_ERROR;
					else
					{
						acceptMatching = true;
						return SUCCESS;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return JSON_ERROR;
				}
			}
			
			return SUCCESS;
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			super.onPostExecute(result);
			
			switch (result)
			{
			case MATCH_ERROR:
				status.setText("寻找座位失败!");
				hint.setText("开始入座");
				currentStatus = ACCEPT_REQUEST;
				break;
			case SUCCESS:
				status.setText("已就坐!");
				hint.setText("开始寻找TA");
				currentStatus = MATCH_REQUEST;
				hangupOrFind.performClick();
				break;
			case JSON_ERROR:
				status.setText("内部错误!");
				hint.setText("开始入座");
				currentStatus = ACCEPT_REQUEST;
				break;
			default:
				break;
			}
		}
	}
	
	private Timer timer;
	private class MatchTask extends AsyncTask<Void, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
			status.setText("正在寻找Mr/Miss right...");
			hint.setText("取消");
			currentStatus = ABORT_MATCH_REQUEST;
		}
		
		@Override
		protected String doInBackground(Void... params) 
		{
			String result;
			for (int i = 0; i < 15; i++)
			{
				result = matchOnline();
				
				if (isCancelled())
					return null;
				
				if (!isStringEmpty(result))
					return result;
				else
				{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			
			if (result == null)
			{
				status.setText("坐席忙，请稍后再试...");
				hint.setText("开始寻找Ta");
				currentStatus = MATCH_REQUEST;
				matchTask.cancel(true);
			}
			else
			{
				portraitFlipInAnim.end();
				portraitFlipOutAnim.end();
				List<String> logins = new LinkedList<String>();
				logins.add(result);
				QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
				requestBuilder.setPerPage(10);
				QBUsers.getUsersByLogins(logins, requestBuilder, new QBEntityCallback<ArrayList<QBUser>>()
				{
					@Override
					public void onError(List<String> arg0) {
					}

					@Override
					public void onSuccess() {
					}

					@Override
					public void onSuccess(ArrayList<QBUser> arg0,
							Bundle arg1)
					{
						QBRTCTypes.QBConferenceType qbConferenceType = null;
						qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
						Map<String, String> userInfo = new HashMap<String, String>();
						userInfo.put("any_custom_data", "some data");
						userInfo.put("my_avatar_url", "avatar_reference");
						List<Integer> opponents = new ArrayList<Integer>();
						opponents.add(arg0.get(0).getId());
						((VideoChatActivity) getActivity()).addConversationFragmentStartCall(
						opponents, qbConferenceType, userInfo);
					}
				}); 
			}
		}
	}
	
	private String matchOnline()
	{
		try 
		{
			JSONObject obj = new JSONObject();
			obj.put(LOGIN_ACCOUNT, login);
			obj.put(LOGIN_PASSWORD, password);
			
			String [] param = new String[1];
			Object [] vlaue = new Object[1];
			param[0] = "callerUserInfo";
			vlaue[0] = obj.toString();
			
			WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
			Object ret = wsAPI.callFuntion("getOnlineUser", param, vlaue);
			
			JSONObject result = new JSONObject(ret.toString());
			if (!result.has(LOGIN_ACCOUNT))
				return null;
			
			return (String) result.get(LOGIN_ACCOUNT);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{
					JSONObject obj = new JSONObject();
					obj.put(LOGIN_ACCOUNT, login);
					obj.put(LOGIN_PASSWORD, password);
					String [] param = new String[1];
					Object [] vlaue = new Object[1];
					param[0] = "callerUserInfo";
					vlaue[0] = obj.toString();
					
					WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
					wsAPI.callFuntion("logoffAccount", param, vlaue);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		td.start();
		
		Thread td2 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{
					JSONObject obj = new JSONObject();
					obj.put(LOGIN_ACCOUNT, login);
					obj.put(LOGIN_PASSWORD, password);
					String [] param = new String[1];
					Object [] vlaue = new Object[1];
					param[0] = "callerUserInfo";
					vlaue[0] = obj.toString();
					
					WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
					wsAPI.callFuntion("stopMatching", param, vlaue);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		td2.start();
	}
}