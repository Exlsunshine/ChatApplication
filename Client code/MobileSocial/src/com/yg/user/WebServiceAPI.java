package com.yg.user;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.yg.commons.ConstantValues;

public class WebServiceAPI
{
	private String nameSpace;
	private String endPoint;
	private SoapSerializationEnvelope envelope;
	private final String host = "http://172.18.8.142:8080/WebServiceProject/services/";
	private final String space = "http://";

	/**
	 * API构�?函数
	 * @param packagename webservice 服务 包名
	 * @param classname   webservice 服务 类名
	 */
	public WebServiceAPI(String packagename, String classname)
	{
		nameSpace = space + packagename;
		endPoint = host + classname;
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
	}
	
	/**
	 * webservice 服务调用函数
	 * @param methodName 被调用的函数�?
	 * @param parameters 函数 参数名与实�?的映射表
	 * @return 函数返回结果
	 */
	public Object callFuntion(String methodName, String[] name, Object[] values)
	{
		SoapObject rpc = new SoapObject(nameSpace, methodName);  
		for (int i = 0; i < name.length; i++)
			rpc.addProperty(name[i], values[i]);
		
	    envelope.bodyOut = rpc;
	    
	    envelope.setOutputSoapObject(rpc);
	    HttpTransportSE transport = new HttpTransportSE(endPoint);  
	    Object result= null;
	    try {
			transport.call("", envelope);
			result = envelope.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return ConstantValues.InstructionCode.ERROR_NETWORK;
		}
	    return result;
	}
	
	public Object callFuntion(String methodName)
	{
		SoapObject rpc = new SoapObject(nameSpace, methodName); 
		envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);
	    HttpTransportSE transport = new HttpTransportSE(endPoint);  
	    Object result= null;
	    try {
			transport.call("", envelope);
			result = envelope.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return 0xffff;
		}
	    return result;
	}
}