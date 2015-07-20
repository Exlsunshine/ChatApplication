package com.lj.driftbottle.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraUtil extends SurfaceView implements SurfaceHolder.Callback{
	
	SurfaceHolder surfaceHolder;
	Camera camera;
	Bitmap bitmap;

	public CameraUtil(Context context) {
		super(context);
		surfaceHolder=getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters=camera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPreviewSize(320, 480);
        parameters.setPictureSize(320, 480);
        camera.setParameters(parameters);
        camera.startPreview();
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		try {
			camera=Camera.open();
			camera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			camera.release();
            camera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
        camera = null;
	}
	
	//拍照
	public void takePictrue(){
		if(camera!=null){
			camera.takePicture(null, null, jpegCallback);
		}
	}
	
	//拍照并且保存的回调函数

    private PictureCallback jpegCallback = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            try
            {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            File file = new File("/sdcard/camera1.jpg");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
             Canvas  canvas= surfaceHolder.lockCanvas();
             canvas.drawBitmap(bitmap, 0,0, null);
             surfaceHolder.unlockCanvasAndPost(canvas);
             }
             catch(Exception e)
             {
                 e.printStackTrace();
             }
         }
     };

}
