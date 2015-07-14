package com.mail;


public class SendMailDemo
{
	public static int sendEmail(String content, String email)
	{
		System.out.println("Resting password for " + email);
		
		// 设置邮件服务器信息
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.sina.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		  
		// 邮箱用户名
		mailInfo.setUserName("tqhv8703");
		// 邮箱密码
		mailInfo.setPassword("tfggni64");
		// 发件人邮箱
		mailInfo.setFromAddress("tqhv8703@sina.com");
		// 收件人邮箱
		mailInfo.setToAddress(email);
		// 邮件标题
		mailInfo.setSubject("揭秘陌生人-找回密码");
		// 邮件内容
		StringBuffer buffer = new StringBuffer();
		buffer.append(content);
		//buffer.append("JAF 1.1.1 jar包下载地址：http://www.oracle.com/technetwork/java/javase/downloads/index-135046.html");
		mailInfo.setContent(buffer.toString());
		
		// 发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		// 发送文体格式
		sms.sendTextMail(mailInfo);
		// 发送html格式
		//SimpleMailSender.sendHtmlMail(mailInfo);
		System.out.println("邮件发送完毕");
		return 0;
	}
	
	public static void main(String[] args) {
		SendMailDemo.sendEmail("sss", "jmmsrbjut@126.com");
	}
}