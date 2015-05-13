package com.tp.adapter;

import java.util.ArrayList;

import org.kobjects.base64.Base64;


import com.example.testmobiledatabase.R;
import com.tp.messege.AbstractPost;
import com.tp.messege.Comment;
import com.tp.messege.ImagePost;
import com.yg.commons.CommonUtil;
import com.yg.message.ConvertUtil;
import com.yg.ui.dialog.implementation.DateUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostCommentListAdapter extends BaseAdapter
{
	private Context context;
	private LayoutInflater inflater = null;
	private AbstractPost post;
	private ArrayList<Comment> commentList;
	private Comment empty = new Comment();
	public PostCommentListAdapter(Context contextSend, AbstractPost postSend, ArrayList<Comment> comments)
	{
		context = contextSend;
		post = postSend;
		commentList = new ArrayList<Comment>();
		commentList.add(empty);
		commentList.addAll(comments);
		inflater = LayoutInflater.from(contextSend);
	}
	@Override
	public int getCount() 
	{
		return commentList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return commentList.get(position);
	}
	
	@Override
	public long getItemId(int position) 
	{
		return commentList.get(position).getPostID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
        ViewHolder holder;
        if (convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != position) 
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tp_textpostcommentlistview, null);
            holder.commentContent = (TextView) convertView.findViewById(R.id.textpostcommentlistview_commentContent);
            holder.commentTime = (TextView) convertView.findViewById(R.id.textpostcommentlistview_commentTime);
            holder.postphoto = (ImageView) convertView.findViewById(R.id.textpostcommentlistview_postphoto);
            holder.userPortrait = (ImageView) convertView.findViewById(R.id.textpostcommentlistview_tp_userportrait);
            if (position == 0) 
            {
                holder.flag = position;
                if (post.getSex().equals("male"))
                	holder.userPortrait.setImageResource(R.drawable.tp_male);
                else
                	holder.userPortrait.setImageResource(R.drawable.tp_female);
                if (post.getPostType() == 1)
                {
                	String postContent = post.getContent().toString();
                    String earlyTime = post.getPostDate();
                    String currentTime = CommonUtil.now();
                    earlyTime = earlyTime.replace(" ", "-");
                    earlyTime = earlyTime.replace(":", "-");
                    
                    Log.i("_____________________________",  earlyTime.substring(0, 19));
                    
                    String suggestiontime = DateUtil.getSuggestion(currentTime, earlyTime.substring(0, 19));
                    holder.commentContent.setText(postContent);
                    holder.commentTime.setText(suggestiontime);
                }
                else
                {
                	ImagePost ip = (ImagePost) post;
                	Bitmap bm = ip.getImage();
                    Drawable dr = new BitmapDrawable(bm);
                    holder.postphoto.setBackground(dr);
                    holder.postphoto.setVisibility(View.VISIBLE);
                    holder.commentContent.setVisibility(View.GONE);
                    holder.commentTime.setVisibility(View.GONE);
                }
            }
            else
            {
            	Comment comment = commentList.get(position);
            	if (comment.getSex().equals("male"))
                	holder.userPortrait.setImageResource(R.drawable.tp_male);
                else
                	holder.userPortrait.setImageResource(R.drawable.tp_female);
            	holder.commentContent.setVisibility(View.VISIBLE);
                holder.commentTime.setVisibility(View.VISIBLE);
                holder.postphoto.setVisibility(View.GONE);
            	holder.flag = position;
            	holder.commentContent.setText(comment.getComment());
            	
            	String earlyTime = comment.getCommentDate();
                String currentTime = CommonUtil.now();
                earlyTime = earlyTime.replace(" ", "-");
                earlyTime = earlyTime.replace(":", "-");
                String suggestiontime = DateUtil.getSuggestion(earlyTime, currentTime);
                holder.commentTime.setText(suggestiontime);
            }
            convertView.setTag(holder);
        }
		return convertView;
	}
	
	static class ViewHolder 
	{
        TextView commentContent;
        TextView commentTime;
        ImageView postphoto;
        TextView status;
        ImageView userPortrait;
        int flag = -1;
    }
}
