package com.lj.shake;

import java.util.ArrayList;
import com.baidu.location.BDLocation;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;



import android.os.Handler;
import android.os.Message;

public class ThreadGetNearbyUser extends Thread
{
	private WebMapLocation webMapLocation;
	private int userId;
	private Handler myhandle;
	private BDLocation mylocation;
	
	public ThreadGetNearbyUser(Handler handle, int userid, BDLocation location) {
		// TODO Auto-generated constructor stub
		webMapLocation = new WebMapLocation();
		userId = userid;
		myhandle = handle;
		mylocation = location;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		//上传位置坐标
		int result = webMapLocation.uploadLocation(userId, (float)mylocation.getLongitude(), (float)mylocation.getLatitude());
		if (result == ConstantValues.InstructionCode.ERROR_NETWORK)
		{
			CommonUtil.sendNetWorkErrorMessage(myhandle);
			return;
		}
		//获取周围用户
		ArrayList<UserShakeData> userShakeDataList = webMapLocation.getNearbyUser(userId);
		if (userShakeDataList == null)
		{
			CommonUtil.sendNetWorkErrorMessage(myhandle);
			return;
		}
		Message msg = new Message();
		msg.obj = userShakeDataList;
		msg.what = ConstantValues.InstructionCode.HANDLER_SUCCESS_GET_DATA;
		myhandle.sendMessage(msg);
	}
}
