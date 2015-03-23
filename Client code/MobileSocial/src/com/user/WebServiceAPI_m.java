package com.user;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceAPI_m
{
	private String nameSpace;
	private String endPoint;
	private SoapSerializationEnvelope envelope;

	WebServiceAPI_m(String nameSpace, String endPoint)
	{
		this.nameSpace = nameSpace;
		this.endPoint = endPoint;
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
	}
	
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
			return 0xffff;
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
	
	public static Map<String,Object> creatParametersMap(String[] name, Object[] value) 
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (int i = 0; i < name.length; i++)
			parameters.put(name[i], value[i]);
		return parameters;
	}
}