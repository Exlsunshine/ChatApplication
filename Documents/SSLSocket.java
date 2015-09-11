package com.sslsocket.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import org.xml.sax.InputSource;

public class ConnectServer {
	private SSLSocket mSocket;   
	public DataInputStream dInputStream;
	
	private String CLIENT_KET_PASSWORD = "222111";//私钥密码   
    private String CLIENT_TRUST_PASSWORD = "222111";//信任证书密码   
    private String CLIENT_AGREEMENT = "TLS";		//使用协议   
    private String CLIENT_KEY_MANAGER = "X509";	//密钥管理器   
    private String CLIENT_TRUST_MANAGER = "X509";	//信任证书管理器   
    private String CLIENT_KEY_KEYSTORE = "JKS";	//"JKS";//密库，这里用的是BouncyCastle密库   
    private String CLIENT_TRUST_KEYSTORE = "JKS";	//"JKS";//   
    private String ENCONDING = "utf-8";			//字符集
    
	private String host;
	private int port;
	private String message = "";
	private String strResult = "";
	private InputSource inputSource;
	
	public ConnectServer(String message) {
		this.host = FileName.getFileName("BuyIP");
		this.port = Integer.parseInt(FileName.getFileName("BuyPort"));
		this.message = message;
		ConnectToServer();
	}
	
	public ConnectServer(String host, int port,String message) {
		this.host = host;
		this.port = port;
		this.message = message;
		ConnectToServer();
	}

	// 用于连接服务器端
	public void ConnectToServer() {
		byte[] inData = null;
		try {
			inData = message.getBytes(ENCONDING);
			
			//取得SSL的SSLContext实例   ---构造SSL环境，指定SSL版本为3.0，也可以使用TLSv1，但是SSLv3更加常用。
			SSLContext sslContext=SSLContext.getInstance(CLIENT_AGREEMENT);
			//取得KeyManagerFactory实例   -------- 创建用于管理JKS密钥库的X.509密钥管理器。
			KeyManagerFactory keyManager=KeyManagerFactory.getInstance(CLIENT_KEY_MANAGER);
			//取得TrustManagerFactory的X509密钥管理器
			TrustManagerFactory trustManager=TrustManagerFactory.getInstance(CLIENT_TRUST_MANAGER);
			//取得BKS密库实例      ------- 访问Java密钥库，JKS是keytool创建的Java密钥库，保存密钥。
			KeyStore keyKeyStore=KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
			KeyStore trustKeyStore=KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
			//加载证书和私钥,通过读取资源文件的方式读取密钥和信任证书（kclient:密钥;t_client:信任证书）
			InputStream is1 = "kclient.jks";
			keyKeyStore.load(is1,CLIENT_KET_PASSWORD.toCharArray());
			
			InputStream is2 = "tclient.jks";
			trustKeyStore.load(is2,CLIENT_TRUST_PASSWORD.toCharArray());
			
			//初始化密钥管理器、信任证书管理器-------- 初始化SSL环境。
			keyManager.init(keyKeyStore,CLIENT_KET_PASSWORD.toCharArray());
			trustManager.init(trustKeyStore);
			//初始化SSLContext   第二个参数是告诉JSSE使用的可信任证书的来源，设置为null是从javax.net.ssl.trustStore中获得证书。第三个参数是JSSE生成的随机数，这个参数将影响系统的安全性，设置为null是个好选择，可以保证JSSE的安全性。
			sslContext.init(keyManager.getKeyManagers(),trustManager.getTrustManagers(),null);
			//生成客户端SSLSocket			
			mSocket =(SSLSocket)sslContext.getSocketFactory().createSocket(InetAddress.getByName(host), port);
			dInputStream = new DataInputStream(mSocket.getInputStream());
		    DataOutputStream localDataOutputStream = new DataOutputStream(mSocket.getOutputStream());		    
		    int i3 = dInputStream.available();
		    if (i3 > 0)
		      {
		        dInputStream.readFully(new byte[i3]);
		      }
		      if (inData != null)
		      {
		        localDataOutputStream.write(inData);
		        localDataOutputStream.flush();
		      }
		      
		      inData = new byte[1000];
		      byte[] arrayOfByte2 = null;
		      
		      while (true)
		      {
		        int i4 = dInputStream.read(inData);
		        if (i4 <= 0) break;
		        arrayOfByte2 = getByteConnect(arrayOfByte2, inData, i4);
		      }
		      if (arrayOfByte2 != null)
		      {
		    	  strResult = new String(arrayOfByte2, ENCONDING);
		      }
		      dInputStream.close();
		      localDataOutputStream.close();
		      mSocket.close();
		      
		} catch (Exception e) {
			Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
		}
	}

	private static byte[] getByteConnect(byte[] b1, byte[] b2, int len) {
		if (b1 != null) {
			if (b2 == null)
				len = 0;
			else if (b2.length < len)
				len = b2.length;
			byte[] b = new byte[b1.length + len];
			System.arraycopy(b1, 0, b, 0, b1.length);
			System.arraycopy(b2, 0, b, b1.length, len);
			return b;
		} else {
			if (b2 == null)
				return b1;
			else if (b2.length < len)
				len = b2.length;
			byte[] b = new byte[len];
			System.arraycopy(b2, 0, b, 0, len);
			return b;
		}
	}
	
	//返回字符串
	public String getString(){
		return strResult;
	}
	
}
