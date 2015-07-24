package com.yg.videochat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.testmobiledatabase.R;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialingFragment extends Fragment
{
	private View view;
	private static final String DEBUG_TAG = "DialingFragment______";
	private List<Integer> opponents;
	private LinearLayout back;
	private ImageView hangup;
	private boolean isCalling = true;
	
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
		hangup = (ImageView) view.findViewById(R.id.yg_activity_video_chat_hangup);
	}
	
	private void setupListeners()
	{
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				((VideoChatActivity) getActivity()).exit();
			}
		});
		
		hangup.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				/*if (isCalling == true)
				{
					hangup();
					isCalling = false;
				}
				else
				{
					findSomeone();
					isCalling = true;
				}*/
				if (isFirst)
				{
					((VideoChatActivity) getActivity()).loginAsUserA();
					isFirst = false;
					Log.i(DEBUG_TAG, "Signin indicates firstA");
				}
				else
				{
					autoRandomCall("userB");
					Log.i(DEBUG_TAG, "Not signin indicate logedinA");
				}
			}
		});
		
		TextView tv = (TextView) view.findViewById(R.id.yg_activity_video_chat_inner_hint);
		tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (isFirst)
				{
					((VideoChatActivity) getActivity()).loginAsUserB();
					isFirst = false;
					Log.i(DEBUG_TAG, "Signin indicates firstB");
				}
				else
				{
					Log.i(DEBUG_TAG, "Not signin indicate logedinB");
					autoRandomCall("userA");
				}
			}
		});
	}
	
	private static boolean isFirst = true;
	
	private void hangup()
	{
		
	}
	
	private void findSomeone()
	{
		
	}
	
	private void autoRandomCall(String login)
	{
		List<String> logins = new LinkedList<String>();
		logins.add(login);
		QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
		requestBuilder.setPerPage(100);
		QBUsers.getUsersByLogins(logins, requestBuilder, new QBEntityCallback<ArrayList<QBUser>>() 
		{
			@Override
			public void onError(List<String> arg0) 
			{
				Log.e(DEBUG_TAG, "1");
			}

			@Override
			public void onSuccess()
			{
				Log.i(DEBUG_TAG, "2");
			}

			@Override
			public void onSuccess(ArrayList<QBUser> arg0,
					Bundle arg1)
			{
				Log.i(DEBUG_TAG, "3");

				Log.i(DEBUG_TAG, "User id is " + arg0.get(0).getId());
				Log.i(DEBUG_TAG, "User login is " + arg0.get(0).getLogin());
				
				
				QBRTCTypes.QBConferenceType qbConferenceType = null;
				qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
				Map<String, String> userInfo = new HashMap<String, String>();
				userInfo.put("any_custom_data", "some data");
				userInfo.put("my_avatar_url", "avatar_reference");
				opponents = new ArrayList<Integer>();
				opponents.add(arg0.get(0).getId());
				((VideoChatActivity) getActivity()).addConversationFragmentStartCall(
						opponents, qbConferenceType, userInfo);
			}
		});
	}
}