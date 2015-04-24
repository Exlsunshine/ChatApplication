package com.tp.ui;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.tp.adapter.PostCommentListAdapter;
import com.tp.messege.AbstractPost;
import com.tp.messege.Comment;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TextPostCommentListActivity extends Activity
{
	private Button sendBtn;
	private EditText commentEdtxt;
	private ListView commentListView;
	private PostCommentListAdapter listAdapter;
	private AbstractPost post;
	private ArrayList<Comment> commentList = new ArrayList<Comment>();
	private final int setAdpter = 1;
	private final int refresh = 2;
	private Comment empty = new Comment();
	private int commentUserID = ConstantValues.user.getID();
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tp_textpostcommentactivity);
		sendBtn = (Button) findViewById(R.id.textpostcomment_btn_send);
		commentEdtxt = (EditText) findViewById(R.id.textpostcomment_et_sendcommemt);
		commentListView = (ListView) findViewById(R.id.textpostcomment_textpostcommentlistview);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		initpost();
		
		sendBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				if (view.getId() == R.id.textpostcomment_btn_send)
				{
					final String commentText = commentEdtxt.getText().toString().trim();
					if (!commentText.equals(""))
					{
						new Thread()
						{
							public void run()
							{
								try
								{
									Log.d("setOnClickListener", commentText);
									int postID = post.getPostID();
									int postUserID = post.getPostUserID();
									String commentDate = CommonUtil.now();
									String sex = ConstantValues.user.getSex();
									Log.d("setOnClickListener", commentList.size() + "");
									ConstantValues.user.pm.addNewComment(-1, postID, postUserID, commentUserID, commentText, commentDate, sex);
									commentList.clear();
									commentList.addAll(post.getComments());
									Log.d("setOnClickListener", commentList.size() + "");
									Message message = Message.obtain();
									message.what = setAdpter;
									handler.sendMessage(message);
								}
								catch (Exception e)
				                {
				        			e.printStackTrace();
				                }
							}
						}.start();
					}
					else
					{
						Log.d("setOnClickListener", "edittext null");
						Toast.makeText(getApplicationContext(), " ‰»ÎŒ™ø’", 300).show();
					}
				}
			}
		});
	}
	
	private void initpost()
	{
		Bundle bundle = getIntent().getExtras();
		int postID = bundle.getInt("postid");
		post = ConstantValues.user.pm.getPostByID(postID);
		commentList.addAll(post.getComments());
		Message message = Message.obtain();
		message.what = setAdpter;
		handler.sendMessage(message);
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
			case setAdpter:
				commentEdtxt.setText("");
				Log.e("setAdpter", commentList.size() + "");
				listAdapter = new PostCommentListAdapter(TextPostCommentListActivity.this, post, commentList);
				commentListView.setAdapter(listAdapter);
				break;
			case refresh:
				commentEdtxt.setText("");
				Log.e("refresh", commentList.size() + "");
				listAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
}
