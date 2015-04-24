
package com.tp.adapter;

import java.util.List;

import org.kobjects.base64.Base64;

import com.example.testmobiledatabase.R;
import com.tp.messege.AbstractPost;
import com.tp.views.CircularImage;
import com.yg.message.ConvertUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyselfPostAdpter extends BaseAdapter 
{
    private Context context;
    private List<AbstractPost> posts;
    private int likedNumber = 0;
    private boolean isLiked = false;
    public MyselfPostAdpter(Context context, List<AbstractPost> posts) 
    {
        super();
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() 
    {
        return posts.size();
    }

    @Override
    public Object getItem(int position) 
    {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) 
    {
        return posts.get(position).getPostID();
    }
    
    public AbstractPost getpost(int position)
    {
    	return posts.get(position);
    }
    
    public int getposttype(int position)
    {
    	return posts.get(position).getPostType();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	final AbstractPost post = posts.get(position);
        ViewHolder holder;
        if (convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != position) 
        {
            holder = new ViewHolder();
            if (position == 0) 
            {
                holder.flag = position;
                convertView = LayoutInflater.from(context).inflate(R.layout.tp_mixed_feed_cover_row, null);
                ImageView cover = (ImageView) convertView.findViewById(R.id.publicactivityadpter_cover_image);
                cover.setImageResource(R.drawable.tp_mypost_cover);
                CircularImage avatar = (CircularImage) convertView.findViewById(R.id.publicactivityadpter_cover_user_photo);
                avatar.setImageResource(R.drawable.ic_launcher);
                holder.userportraitIV = (ImageView) convertView.findViewById(R.id.publicactivityadpter_userportrait);
            }

            else
            {
                holder.flag = position;
                // Item layout
                convertView = LayoutInflater.from(context).inflate(R.layout.tp_mixed_feed_activity_item, null);
                // author text
                ImageView authorView = (ImageView) convertView.findViewById(R.id.publicactivityadpter_mixed_feed_author_photo);
                Log.d("PAA____", post.getLocation() +"");
                if (post.getSex().equals("male"))
                	authorView.setImageResource(R.drawable.tp_male);
                else
                	authorView.setImageResource(R.drawable.tp_female);

                // big circle
                ImageView big = (ImageView) convertView.findViewById(R.id.publicactivityadpter_moment_bigdot);
                // big smallcircle
                ImageView smal = (ImageView) convertView.findViewById(R.id.publicactivityadpter_moment_smalldot);
                big.setVisibility(View.INVISIBLE);
                smal.setVisibility(View.INVISIBLE);

                // feed type image
                ImageView feed_post_type = (ImageView) convertView.findViewById(R.id.publicactivityadpter_feed_post_type);
                // content layout
                LinearLayout contentLayout = (LinearLayout) convertView.findViewById(R.id.publicactivityadpter_feed_post_body);
                
                // Text
                if (post.getPostType() == 1) 
                {
                    View view = LayoutInflater.from(context).inflate(R.layout.tp_moment_thought_partial, null);
                    final TextView likedNumberTV = (TextView) view.findViewById(R.id.publicactivityadpter_comment_button_text);
                    likedNumber = post.getLikedNumber();
                    likedNumberTV.setText(Integer.toString(likedNumber));
                    likedNumberTV.setOnClickListener(new OnClickListener() 
                    {
						@Override
						public void onClick(View arg0) 
						{
							if (isLiked != true)
							{
								likedNumber = post.getLikedNumber();
								likedNumberTV.setText(Integer.toString(++likedNumber));
								Thread td = new Thread(new Runnable() 
								{
									@Override
									public void run() 
									{
										post.increaseLikedNumber();
									}
							});
							td.start();
							isLiked = true;
							}
						}
					});
                   
                    TextView comment1 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_body);
                    TextView comment2 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_body1);
                    TextView comment3 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_body2);
                    TextView commentDate1 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_sub);
                    TextView commentDate2 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_sub1);
                    TextView commentDate3 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_sub2);
                    TextView thought_main = (TextView) view.findViewById(R.id.publicactivityadpter_thought_main);
                    ImageView commentprotrait1 = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_profile_photo);
                    ImageView commentprotrait2 = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_profile_photo1);
                    ImageView commentprotrait3 = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_profile_photo2);
                    TextView commentsEllipsisText = (TextView) view.findViewById(R.id.publicactivityadpter_comments_ellipsis_text);
                    RelativeLayout RL = (RelativeLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_ellipsis);
                    RelativeLayout feedComments2 = (RelativeLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_2);
                    RelativeLayout feedComments3 = (RelativeLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_3);
                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_thread);
                    thought_main.setText(ConvertUtil.bytes2String(post.getContent()));
                    
                    int size = post.getComments().size();
                    switch (size)
                    {
                    case 0:
                    	ll.setVisibility(View.GONE);
                    	RL.setVisibility(View.GONE);
                    	break;
                    case 1:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	feedComments2.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	break;
                    case 2:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 1).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	commentDate2.setText(post.getComments().get(size - 1).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	}
                    	else
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	}
                    	RL.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	break;
                    case 3:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	commentDate2.setText(post.getComments().get(size - 2).getCommentDate());
                    	commentDate3.setText(post.getComments().get(size - 1).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	}
                    	else
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	}
                    	RL.setVisibility(View.GONE);
                    default :
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	commentDate2.setText(post.getComments().get(size - 2).getCommentDate());
                    	commentDate3.setText(post.getComments().get(size - 1).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	}
                    	else
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	}
                    	commentsEllipsisText.setText("查看全部评论(" + size + ')');
                    	RL.setVisibility(View.VISIBLE);
                    }
                    contentLayout.addView(view);
                }
                // Img
                else if (post.getPostType() == 2) 
                {
                    View view = LayoutInflater.from(context).inflate(R.layout.tp_moment_photo_partial,null);
                    TextView comment1 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_body);
                    TextView comment2 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_body1);
                    TextView comment3 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_body2);
                    TextView commentDate1 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_sub);
                    TextView commentDate2 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_sub1);
                    TextView commentDate3 = (TextView) view.findViewById(R.id.publicactivityadpter_comment_sub2);
                    ImageView commentprotrait1 = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_profile_photo);
                    ImageView commentprotrait2 = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_profile_photo1);
                    ImageView commentprotrait3 = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_profile_photo2);
                    TextView commentsEllipsisText = (TextView) view.findViewById(R.id.publicactivityadpter_comments_ellipsis_text);
                    RelativeLayout RL = (RelativeLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_ellipsis);
                    RelativeLayout feedComments2 = (RelativeLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_2);
                    RelativeLayout feedComments3 = (RelativeLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_3);
                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.publicactivityadpter_feed_comments_thread);
                    ImageView portrait = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_portrait);
                    
                    final TextView likedNumberTV = (TextView) view.findViewById(R.id.publicactivityadpter_comment_button_text);
                	likedNumber = post.getLikedNumber();
                    likedNumberTV.setText(Integer.toString(likedNumber));
                    likedNumberTV.setOnClickListener(new OnClickListener() 
                    {
						@Override
						public void onClick(View arg0) 
						{
							if (isLiked != true)
							{
								likedNumberTV.setText(Integer.toString(++likedNumber));
								Thread td = new Thread(new Runnable() 
								{
									@Override
									public void run() 
									{
										post.increaseLikedNumber();
									}
							});
							td.start();
							isLiked = true;
							}
						}
					});
                    
                    feed_post_type.setImageResource(R.drawable.tp_moment_icn_place);
                    ImageView photoView = (ImageView) view.findViewById(R.id.publicactivityadpter_photo);
                    byte[] buffer = new Base64().decode(ConvertUtil.bytes2String(post.getContent()));
                    Log.e("getview__", post.getContent().toString());
                    Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    Drawable dr = new BitmapDrawable(bm);
                    photoView.setBackground(dr);
                    //photoView.setImageResource(R.drawable.coffe0);
                    
                    int size = post.getComments().size();
                    switch (size)
                    {
                    case 0:
                    	ll.setVisibility(View.GONE);
                    	RL.setVisibility(View.GONE);
                    	break;
                    case 1:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	feedComments2.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	break;
                    case 2:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 1).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	commentDate2.setText(post.getComments().get(size - 1).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	}
                    	else
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	}
                    	RL.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	break;
                    case 3:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	commentDate2.setText(post.getComments().get(size - 2).getCommentDate());
                    	commentDate3.setText(post.getComments().get(size - 1).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	}
                    	else
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	}
                    	RL.setVisibility(View.GONE);
                    default :
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	commentDate1.setText(post.getComments().get(0).getCommentDate());
                    	commentDate2.setText(post.getComments().get(size - 2).getCommentDate());
                    	commentDate3.setText(post.getComments().get(size - 1).getCommentDate());
                    	if (post.getComments().get(0).getSex().equals("male"))
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	}
                    	else
                    	{
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	}
                    	commentsEllipsisText.setText("查看全部评论(" + size + ')');
                    	RL.setVisibility(View.VISIBLE);
                    }
                    contentLayout.addView(view);
                }
            }
            convertView.setTag(holder);
        }
        return convertView;
    }
    
    static class ViewHolder 
    {
        TextView text;
        TextView time;
        ImageView userportraitIV;
        TextView status;
        int flag = -1;
    }
}
