
package com.tp.adapter;

import java.io.File;
import java.util.List;

import com.example.testmobiledatabase.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tp.messege.AbstractPost;
import com.tp.messege.ImagePost;
import com.tp.ui.ImageZoomInActivity;
import com.tp.ui.TextPostCommentListActivity;
import com.tp.views.CircularImage;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.ui.dialog.implementation.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PublicActivityAdapter extends BaseAdapter 
{
	private static final String DEBUG_TAG = "PublicActivityAdapter______";
    private Context context;
    private List<AbstractPost> posts;
    private int likedNumber = 0;
    private ImageLoader imageLoader = ImageLoader.getInstance();
	
    public PublicActivityAdapter(Context context, List<AbstractPost> posts) 
    {
		super();
		this.context = context;
		this.posts = posts;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).diskCacheExtraOptions(480, 800, null)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();// 开始构建
				
		imageLoader.init(config);
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
                cover.setImageResource(R.drawable.tp_cover);
                CircularImage avatar = (CircularImage) convertView.findViewById(R.id.publicactivityadpter_cover_user_photo);
                avatar.setImageBitmap((ConstantValues.user.getPortraitBmp()));
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
                String currentTime = CommonUtil.now();
                
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
							likedNumber = post.getLikedNumber();
							Thread td = new Thread(new Runnable() 
							{
								@Override
								public void run() 
								{
									int result = post.modifyLikedNumber(ConstantValues.user.getID());
									likedNumber += result;
								}
							});
							td.start();
							try 
							{
								td.join();
								likedNumberTV.setText(Integer.toString(likedNumber));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
						}
					});
                    View divider = view.findViewById(R.id.tp_feed_comments_comment_divider);
                    View divider1 = view.findViewById(R.id.tp_feed_comments_comment_divider1);
                    View divider2 = view.findViewById(R.id.tp_feed_comments_comment_divider2);
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
                    LinearLayout commentstextpost = (LinearLayout) view.findViewById(R.id.publicactivityadpter_feed_comments);
                    thought_main.setText(post.getContent().toString());
                    
                    int size = post.getComments().size();
                    switch (size)
                    {
                    case 0:
                    	view.findViewById(R.id.tp_text_post_divider).setVisibility(View.INVISIBLE);
                    	ll.setVisibility(View.GONE);
                    	RL.setVisibility(View.GONE);
                    	break;
                    case 1:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	String earlyTimecase1 = post.getComments().get(0).getCommentDate();
                        earlyTimecase1 = earlyTimecase1.replace(" ", "-");
                        earlyTimecase1 = earlyTimecase1.replace(":", "-");
                        String suggestiontime = DateUtil.getSuggestion(currentTime, earlyTimecase1.substring(0, 19));
                    	commentDate1.setText(suggestiontime);
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	feedComments2.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	divider.setVisibility(View.INVISIBLE);
                    	break;
                    case 2:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 1).getComment());
                    	String earlyTimecase2_1 = post.getComments().get(0).getCommentDate();
                        earlyTimecase2_1 = earlyTimecase2_1.replace(" ", "-");
                        earlyTimecase2_1 = earlyTimecase2_1.replace(":", "-");
                        String suggestiontimecase2_1 = DateUtil.getSuggestion(currentTime, earlyTimecase2_1.substring(0, 19));
                    	commentDate1.setText(suggestiontimecase2_1);
                    	
                    	String earlyTimecase2_2 = post.getComments().get(size - 1).getCommentDate();
                        earlyTimecase2_2 = earlyTimecase2_2.replace(" ", "-");
                        earlyTimecase2_2 = earlyTimecase2_2.replace(":", "-");
                        String suggestiontimecase2_2 = DateUtil.getSuggestion(currentTime, earlyTimecase2_2.substring(0, 19));
                    	commentDate2.setText(suggestiontimecase2_2);
                    	
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(1).getSex().equals("male"))
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	divider1.setVisibility(View.INVISIBLE);
                    	break;
                    case 3:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	
                    	String earlyTimecase3_1 = post.getComments().get(0).getCommentDate();
                        earlyTimecase3_1 = earlyTimecase3_1.replace(" ", "-");
                        earlyTimecase3_1 = earlyTimecase3_1.replace(":", "-");
                        String suggestiontimecase3_1 = DateUtil.getSuggestion(currentTime, earlyTimecase3_1.substring(0, 19));
                    	commentDate1.setText(suggestiontimecase3_1);
                    	
                    	String earlyTimecase3_2 = post.getComments().get(size - 2).getCommentDate();
                        earlyTimecase3_2 = earlyTimecase3_2.replace(" ", "-");
                        earlyTimecase3_2 = earlyTimecase3_2.replace(":", "-");
                        String suggestiontimecase3_2 = DateUtil.getSuggestion(currentTime, earlyTimecase3_2.substring(0, 19));
                    	commentDate2.setText(suggestiontimecase3_2);
                    	
                    	String earlyTimecase3_3 = post.getComments().get(size - 1).getCommentDate();
                        earlyTimecase3_3 = earlyTimecase3_3.replace(" ", "-");
                        earlyTimecase3_3 = earlyTimecase3_3.replace(":", "-");
                        String suggestiontimecase3_3 = DateUtil.getSuggestion(currentTime, earlyTimecase3_3.substring(0, 19));
                    	commentDate3.setText(suggestiontimecase3_3);
                    	
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(1).getSex().equals("male"))
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(2).getSex().equals("male"))
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	divider2.setVisibility(View.INVISIBLE);
                    	break;
                    default :
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	
                    	String earlyTimecasedefault_1 = post.getComments().get(0).getCommentDate();
                        earlyTimecasedefault_1 = earlyTimecasedefault_1.replace(" ", "-");
                        earlyTimecasedefault_1 = earlyTimecasedefault_1.replace(":", "-");
                        String suggestiontimecasedefault_1 = DateUtil.getSuggestion(currentTime, earlyTimecasedefault_1.substring(0, 19));
                    	commentDate1.setText(suggestiontimecasedefault_1);
                    	
                    	String earlyTimecasedefault_2 = post.getComments().get(size - 2).getCommentDate();
                        earlyTimecasedefault_2 = earlyTimecasedefault_2.replace(" ", "-");
                        earlyTimecasedefault_2 = earlyTimecasedefault_2.replace(":", "-");
                        String suggestiontimecasedefault_2 = DateUtil.getSuggestion(currentTime, earlyTimecasedefault_2.substring(0, 19));
                    	commentDate2.setText(suggestiontimecasedefault_2);
                    	
                    	String earlyTimecasedefault_3 = post.getComments().get(size - 1).getCommentDate();
                        earlyTimecasedefault_3 = earlyTimecasedefault_3.replace(" ", "-");
                        earlyTimecasedefault_3 = earlyTimecasedefault_3.replace(":", "-");
                        String suggestiontimecasedefault_3 = DateUtil.getSuggestion(currentTime, earlyTimecasedefault_3.substring(0, 19));
                    	commentDate3.setText(suggestiontimecasedefault_3);
                    	
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(post.getcommentsize() - 2).getSex().equals("male"))
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(post.getcommentsize() - 1).getSex().equals("male"))
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	commentsEllipsisText.setText("查看全部评论(" + size + ')');
                    	RL.setVisibility(View.VISIBLE);
                    	divider2.setVisibility(View.INVISIBLE);
                    }
                    contentLayout.addView(view);
                }
                // Img
                else if (post.getPostType() == 2) 
                {
                    View view = LayoutInflater.from(context).inflate(R.layout.tp_moment_photo_partial,null);
                    View divider = view.findViewById(R.id.tp_feed_comments_comment_divider);
                    View divider1 = view.findViewById(R.id.tp_feed_comments_comment_divider1);
                    View divider2 = view.findViewById(R.id.tp_feed_comments_comment_divider2);
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
                    //ImageView portrait = (ImageView) view.findViewById(R.id.publicactivityadpter_comment_portrait);
                    
                    final TextView likedNumberTV = (TextView) view.findViewById(R.id.publicactivityadpter_comment_button_text);
                	likedNumber = post.getLikedNumber();
                    likedNumberTV.setText(Integer.toString(likedNumber));
                    likedNumberTV.setOnClickListener(new OnClickListener() 
                    {
						@Override
						public void onClick(View arg0) 
						{
							likedNumber = post.getLikedNumber();
							Thread td = new Thread(new Runnable() 
							{
								@Override
								public void run() 
								{
									int result = post.modifyLikedNumber(ConstantValues.user.getID());
									likedNumber += result;
								}
							});
							td.start();
							try 
							{
								td.join();
								likedNumberTV.setText(Integer.toString(likedNumber));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
						}
					});
                    feed_post_type.setImageResource(R.drawable.tp_moment_icn_place);
                    ImageView photoView = (ImageView) view.findViewById(R.id.publicactivityadpter_photo);
                    
                    ImagePost ip = (ImagePost) post;
					if (ip.isImgPathEmpty() == true) 
					{
						// 显示图片的配置
						DisplayImageOptions options = new DisplayImageOptions.Builder()
								.showImageOnLoading(R.drawable.tp_loading_picture)
								.showImageOnFail(R.drawable.tp_loading_failed)
								.cacheInMemory(true).cacheOnDisk(true)
								.bitmapConfig(Bitmap.Config.RGB_565).build();
						imageLoader.displayImage(ip.getImageURL(), photoView, options);
						String path = imageLoader.getInstance().getDiscCache().get(ip.getImageURL()).getAbsolutePath();
						ip.setImagePath(path);
					} 
					else 
					{
						DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.tp_loading_picture)
						.showImageOnFail(R.drawable.tp_loading_failed)
						.cacheInMemory(true).cacheOnDisk(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
						File imageFile = new File(ip.getImagePath());
						if (imageFile.exists())
							imageLoader.displayImage("file://" + ip.getImagePath(), photoView, options);
						else
							imageLoader.displayImage(ip.getImageURL(), photoView, options);
					}
                    /*GetImageTask task = new GetImageTask(photoView, ip);
                    task.execute(0);*/
                     
                    photoView.setOnLongClickListener(new OnLongClickListener() 
                    {
						@Override
						public boolean onLongClick(View v) 
						{
							Log.e("onLongClick", "onLongClick");
							ImagePost ipLongClick = (ImagePost) post;
							if (ipLongClick.isImgPathEmpty())
							{
								String URL = ipLongClick.getImageURL();
								Log.e("publicpostadpter", "geturl");
								Intent intent = new Intent(context, ImageZoomInActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("Path", URL);
								intent.putExtras(bundle); 
								context.startActivity(intent);
							}
							else
							{
								String Path = "file://" + ipLongClick.getImagePath();
								Log.e("publicpostadpter", "getImagePath");
								Intent intent = new Intent(context, ImageZoomInActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("Path", Path);
								intent.putExtras(bundle); 
								context.startActivity(intent);
							}
							return true;
						}
					});
                    
                    photoView.setOnClickListener(new OnClickListener()
                    {
						@Override
						public void onClick(View v) 
						{
							Intent intent = new Intent(context, TextPostCommentListActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("postid", post.getPostID());
							intent.putExtras(bundle); 
							context.startActivity(intent);
						}
					});
                    
                    int size = post.getComments().size();
                    switch (size)
                    {
                    case 0:
                    	ll.setVisibility(View.GONE);
                    	RL.setVisibility(View.GONE);
                    	break;
                    case 1:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	String earlyTimecase1 = post.getComments().get(0).getCommentDate();
                        earlyTimecase1 = earlyTimecase1.replace(" ", "-");
                        earlyTimecase1 = earlyTimecase1.replace(":", "-");
                        String suggestiontime = DateUtil.getSuggestion(currentTime, earlyTimecase1.substring(0, 19));
                    	commentDate1.setText(suggestiontime);
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	feedComments2.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	divider.setVisibility(View.INVISIBLE);
                    	break;
                    case 2:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 1).getComment());
                    	
                    	String earlyTimecase2_1 = post.getComments().get(0).getCommentDate();
                        earlyTimecase2_1 = earlyTimecase2_1.replace(" ", "-");
                        earlyTimecase2_1 = earlyTimecase2_1.replace(":", "-");
                        String suggestiontimecase2_1 = DateUtil.getSuggestion(currentTime, earlyTimecase2_1.substring(0, 19));
                    	commentDate1.setText(suggestiontimecase2_1);
                    	
                    	String earlyTimecase2_2 = post.getComments().get(size - 1).getCommentDate();
                        earlyTimecase2_2 = earlyTimecase2_2.replace(" ", "-");
                        earlyTimecase2_2 = earlyTimecase2_2.replace(":", "-");
                        String suggestiontimecase2_2 = DateUtil.getSuggestion(currentTime, earlyTimecase2_2.substring(0, 19));
                    	commentDate2.setText(suggestiontimecase2_2);
                    	
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(1).getSex().equals("male"))
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	feedComments3.setVisibility(View.GONE);
                    	divider1.setVisibility(View.INVISIBLE);
                    	break;
                    case 3:
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	
                    	String earlyTimecase3_1 = post.getComments().get(0).getCommentDate();
                        earlyTimecase3_1 = earlyTimecase3_1.replace(" ", "-");
                        earlyTimecase3_1 = earlyTimecase3_1.replace(":", "-");
                        String suggestiontimecase3_1 = DateUtil.getSuggestion(currentTime, earlyTimecase3_1.substring(0, 19));
                    	commentDate1.setText(suggestiontimecase3_1);
                    	
                    	String earlyTimecase3_2 = post.getComments().get(size - 2).getCommentDate();
                        earlyTimecase3_2 = earlyTimecase3_2.replace(" ", "-");
                        earlyTimecase3_2 = earlyTimecase3_2.replace(":", "-");
                        String suggestiontimecase3_2 = DateUtil.getSuggestion(currentTime, earlyTimecase3_2.substring(0, 19));
                    	commentDate2.setText(suggestiontimecase3_2);
                    	
                    	String earlyTimecase3_3 = post.getComments().get(size - 1).getCommentDate();
                        earlyTimecase3_3 = earlyTimecase3_3.replace(" ", "-");
                        earlyTimecase3_3 = earlyTimecase3_3.replace(":", "-");
                        String suggestiontimecase3_3 = DateUtil.getSuggestion(currentTime, earlyTimecase3_3.substring(0, 19));
                    	commentDate3.setText(suggestiontimecase3_3);
                    	
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(1).getSex().equals("male"))
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(2).getSex().equals("male"))
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	RL.setVisibility(View.GONE);
                    	divider2.setVisibility(View.INVISIBLE);
                    	break;
                    default :
                    	comment1.setText(post.getComments().get(0).getComment());
                    	comment2.setText(post.getComments().get(size - 2).getComment());
                    	comment3.setText(post.getComments().get(size - 1).getComment());
                    	
                    	String earlyTimecasedefault_1 = post.getComments().get(0).getCommentDate();
                        earlyTimecasedefault_1 = earlyTimecasedefault_1.replace(" ", "-");
                        earlyTimecasedefault_1 = earlyTimecasedefault_1.replace(":", "-");
                        String suggestiontimecasedefault_1 = DateUtil.getSuggestion(currentTime, earlyTimecasedefault_1.substring(0, 19));
                    	commentDate1.setText(suggestiontimecasedefault_1);
                    	
                    	String earlyTimecasedefault_2 = post.getComments().get(size - 2).getCommentDate();
                        earlyTimecasedefault_2 = earlyTimecasedefault_2.replace(" ", "-");
                        earlyTimecasedefault_2 = earlyTimecasedefault_2.replace(":", "-");
                        String suggestiontimecasedefault_2 = DateUtil.getSuggestion(currentTime, earlyTimecasedefault_2.substring(0, 19));
                    	commentDate2.setText(suggestiontimecasedefault_2);
                    	
                    	String earlyTimecasedefault_3 = post.getComments().get(size - 1).getCommentDate();
                        earlyTimecasedefault_3 = earlyTimecasedefault_3.replace(" ", "-");
                        earlyTimecasedefault_3 = earlyTimecasedefault_3.replace(":", "-");
                        String suggestiontimecasedefault_3 = DateUtil.getSuggestion(currentTime, earlyTimecasedefault_3.substring(0, 19));
                    	commentDate3.setText(suggestiontimecasedefault_3);
                    	
                    	if (post.getComments().get(0).getSex().equals("male"))
                    		commentprotrait1.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait1.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(post.getcommentsize() - 2).getSex().equals("male"))
                    		commentprotrait2.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait2.setImageResource(R.drawable.tp_female);
                    	if (post.getComments().get(post.getcommentsize() - 1).getSex().equals("male"))
                    		commentprotrait3.setImageResource(R.drawable.tp_male);
                    	else
                    		commentprotrait3.setImageResource(R.drawable.tp_female);
                    	commentsEllipsisText.setText("查看全部评论(" + size + ')');
                    	RL.setVisibility(View.VISIBLE);
                    	divider2.setVisibility(View.INVISIBLE);
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
    
   /* public class GetImageTask extends AsyncTask<Integer, Integer, String> 
    {  
    	private ImageView iv;
    	private ImagePost post;
    	private Drawable dr;
    	
        public GetImageTask(ImageView imageView,ImagePost Post) 
        {  
            super();  
            this.iv = imageView;
            this.post = Post;
        }  
      
        @Override  
        protected String doInBackground(Integer... params) 
        {  
        	Bitmap bm = post.getImage();
        	Log.e("doInBackground___", "get image" + post.getPostID());
            dr = new BitmapDrawable(bm);
            return "1";  
        }  
        
        @Override  
        protected void onPostExecute(String result) 
        {  	
        	Log.e("onPostExecute_____", result);
        	int maxHeight = dp2px(context, 300);
            int height = (int) ((float) iv.getWidth() / dr.getMinimumWidth() * dr.getMinimumHeight());
            if (height > maxHeight) height = maxHeight;
            Log.e("paa____", height + " " + maxHeight + " " + iv.getWidth() + " " + dr.getMinimumWidth() + " " + dr.getMinimumHeight());
            iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
        	iv.setBackground(dr); 
        }  
    }
    
    private int dp2px(Context context, int dp)
    {
    	float scale = context.getResources().getDisplayMetrics().density;
    	return (int) (dp * scale + 0.5f);
    }*/
}