package com.lj.driftbottle.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.testmobiledatabase.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.Toast;

public class Driftbottle_setting extends Activity {
	
	private  PopupWindow  popupWindow;
	private  View view;
	private  LayoutInflater inflater;
	
	private  TableRow  bottle_page_row0,bottle_page_row2,bottle_page_row3;
	
	private  Button  bottle_photo,bottle_localPic,bottle_cancel,driftbottle_set_back;

	ImageView show_bottle;
	int m=2;
	
	CameraUtil cameraUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.driftbottle_setting);
		
		bottle_page_row0=(TableRow) findViewById(R.id.bottle_page_row0);
		bottle_page_row2=(TableRow) findViewById(R.id.bottle_page_row2);
		bottle_page_row3=(TableRow) findViewById(R.id.bottle_page_row3);
		driftbottle_set_back=(Button) findViewById(R.id.driftbottle_set_back);
		show_bottle=(ImageView) findViewById(R.id.show_bottle);
		bottle_page_row0.setOnClickListener(listener2);
		bottle_page_row2.setOnClickListener(listener2);
		bottle_page_row3.setOnClickListener(listener2);
		driftbottle_set_back.setOnClickListener(listener2);
	}
	
	private  OnClickListener listener2=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bottle_page_row0:
				initphotoPop();
				popupWindow.showAtLocation(findViewById(R.id.bottle_page_row0), Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.bottle_page_row2:
				if(m%2==0){
					show_bottle.setImageResource(R.drawable.btn_check_on_normal);
				}else if(m%2!=0){
					show_bottle.setImageResource(R.drawable.btn_check_off_normal);
				}
				m++;
				break;
			case R.id.bottle_page_row3:
				initphotoPop();
				popupWindow.showAtLocation(findViewById(R.id.bottle_page_row0), Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.driftbottle_set_back:
				finish();
				break;

			}
			
		}
	};
	
	
	//选择头像pop
	public  void  initphotoPop(){
		inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		view=inflater.inflate(R.layout.bottle_setting_pop, null);
		popupWindow=new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
		ColorDrawable colorDrawable=new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height=view.findViewById(R.id.lj_setting_pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_DOWN){
					if(y<height){
						popupWindow.dismiss();
						popupWindow=null;
					}
					if(y>height){
						popupWindow.dismiss();
						popupWindow=null;
					}
				}
				return true;
			}
		});
		
		bottle_photo = (Button) view.findViewById(R.id.bottle_photo);
		bottle_localPic=(Button) view.findViewById(R.id.bottle_localPic);
		bottle_cancel=(Button) view.findViewById(R.id.bottle_cancel);
		bottle_photo.setOnClickListener(listener);
		bottle_localPic.setOnClickListener(listener);
		bottle_cancel.setOnClickListener(listener);
		
	}
	
	private  OnClickListener  listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bottle_photo:
				// open camera
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			     startActivityForResult(intent, 1);
			     popupWindow.dismiss();
			     popupWindow=null;
				break;
			case R.id.bottle_localPic:
				// 相册选择
				Intent picture = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(picture, 1);
				popupWindow.dismiss();
				popupWindow=null;
				break;
			case R.id.bottle_cancel:
				popupWindow.dismiss();
				popupWindow=null;
				break;
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			String sdStatus=Environment.getExternalStorageState();
			
			Bundle bundle=data.getExtras();
			Bitmap bitmap=(Bitmap) bundle.get("data");
			
			if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
				Toast.makeText(Driftbottle_setting.this, "SD card is not avaiable", Toast.LENGTH_SHORT).show();
			}
			else{
				FileOutputStream stream=null;
				File file=new File("/sdcard");
				file.mkdirs();
				String fileName="/sdcard/test.jpg";
				try {
					stream=new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//数据写入文件
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						stream.flush();
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
	};
	


}
