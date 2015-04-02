package com.lj.shake;

import android.os.Handler;
import android.os.Message;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.yg.commons.ConstantValues;

public class mapStatusChangeListener implements OnMapStatusChangeListener
{
	private Handler myHandler;
	private float zoom;
	
	public mapStatusChangeListener(Handler handler) 
	{
		myHandler = handler;
		zoom = ConstantValues.InstructionCode.MAP_ZOOM_INITIALIZATION;
	}
	
	@Override
	public void onMapStatusChange(MapStatus arg0) 
	{
	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) 
	{
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) 
	{
		float z = arg0.zoom;
		if (Math.abs(z - zoom) > ConstantValues.InstructionCode.MAP_ZOOM_CHANGE_VALUE)
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_STATUS_CHANGE;
			myHandler.sendMessage(msg);
		}
		zoom = z;
	}
}
