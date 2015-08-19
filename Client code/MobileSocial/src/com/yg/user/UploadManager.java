package com.yg.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.yg.commons.ConstantValues;
import com.yg.network.openfire.OpenfireHandler;

public class UploadManager 
{
	private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";  
	private File uploadFile;
    private String newFileName;
    private String serverIP;
    private String serverPort;
    
    private OpenfireHandler handler = null;
    private String flag = null;
    private String url = null;
    private String oID = null;
    
    public void setHandler(OpenfireHandler handler, String url, String oID, String flag)
    {
    	this.handler = handler;
    	this.url = url;
    	this.oID = oID;
    	this.flag = flag;
    }
    
    /**
     * @param uploadFile 需要上传的文件
     * @param newFileName 上传至服务器后的文件名
     * @param serverIP 服务器地址
     * @param serverPort 服务器端口(默认填写8887)
     */
    public UploadManager(File uploadFile, String newFileName, String serverIP, String serverPort)
    {
    	this.uploadFile = uploadFile;
    	this.newFileName = newFileName;
    	this.serverIP = serverIP;
    	this.serverPort = serverPort;
    }
    
    /** 
     * @param params 传递的普通参数 
     * @param uploadFile 需要上传的文件名 
     * @param fileFormName 需要上传文件表单中的名字 
     * @param newFileName 上传的文件名称，不填写将为uploadFile的名称 
     * @param urlStr 上传的服务器的路径 
     * @throws IOException 
     */  
    private void uploadForm(Map<String, String> params, String fileFormName,  
            File uploadFile, String newFileName, String urlStr) throws IOException
    {  
        if (newFileName == null || newFileName.trim().equals(""))
            newFileName = uploadFile.getName();  
  
        StringBuilder sb = new StringBuilder();  
        
        /** 
         * 普通的表单数据 
         */  
        if (params != null)
        {
	        for (String key : params.keySet())
	        {  
	            sb.append("--" + BOUNDARY + "\r\n");  
	            sb.append("Content-Disposition: form-data; name=\"" + key + "\""  
	                    + "\r\n");  
	            sb.append("\r\n");  
	            sb.append(params.get(key) + "\r\n");  
	        }  
        }
        
        /** 
         * 上传文件的头 
         */  
        sb.append("--" + BOUNDARY + "\r\n");  
        sb.append("Content-Disposition: form-data; name=\"" + fileFormName  
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");  
        sb.append("Content-Type: image/jpeg" + "\r\n");
        // 如果服务器端有文件类型的校验，必须明确指定ContentType  
        sb.append("\r\n");  
  
        byte[] headerInfo = sb.toString().getBytes("UTF-8");  
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");  
        System.out.println(sb.toString());  
        URL url = new URL(urlStr);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("Content-Type",  
                "multipart/form-data; boundary=" + BOUNDARY);  
        conn.setRequestProperty("Content-Length", String  
                .valueOf(headerInfo.length + uploadFile.length()  
                        + endInfo.length));  
        conn.setDoOutput(true);  
  
        OutputStream out = conn.getOutputStream();  
        InputStream in = new FileInputStream(uploadFile);  
        out.write(headerInfo);  
  
        byte[] buf = new byte[1024];  
        int len;  
        while ((len = in.read(buf)) != -1)  
            out.write(buf, 0, len);  
  
        out.write(endInfo);  
        in.close();  
        out.close();  
        if (conn.getResponseCode() == 200) 
        {
        	System.out.println("上传成功");  
        	
        	if (handler != null)
        	{
        		final String finalURL = this.flag + this.url;
        		new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						handler.send(finalURL
								, oID);
					}
				}).start();
        	}
        }
    }  
    
    /**
     * 上传指定文件到服务器
     * @param uploadFile 需要上传的文件
     * @param newFileName 上传至服务器后的文件名
     * @param serverIP 服务器地址
     * @param serverPort 服务器端口
     * @throws IOException
     */
    private void uploadForm(File uploadFile, String newFileName, String serverIP, String serverPort) throws IOException
    {
    	this.uploadForm(null, "uploadFile", uploadFile, newFileName, "http://" + serverIP + ":" + serverPort);
    }
    
    public void excecuteUpload()
    {
    	Thread uploadThread = new Thread(new Runnable()
    	{
			@Override
			public void run()
			{
				try 
				{
					uploadForm(uploadFile, newFileName, serverIP, serverPort);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error occurs during uploading.");
				}
			}
		});
    	uploadThread.start();
    }
    
	public static void main(String[] args)
	{
		File file = new File("D:/a/a.jpg"); 
		
		UploadManager um = new UploadManager(file, "ddddd111.jpg", "172.18.8.30", "8887");
		um.excecuteUpload();
	}
}