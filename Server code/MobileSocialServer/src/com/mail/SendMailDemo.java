package com.mail;


public class SendMailDemo
{
	public static int sendEmail(String content, String email)
	{
		System.out.println("Resting password for " + email);
		
		// �����ʼ���������Ϣ
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.sina.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		  
		// �����û���
		mailInfo.setUserName("tqhv8703");
		// ��������
		mailInfo.setPassword("tfggni64");
		// ����������
		mailInfo.setFromAddress("tqhv8703@sina.com");
		// �ռ�������
		mailInfo.setToAddress(email);
		// �ʼ�����
		mailInfo.setSubject("����İ����-�һ�����");
		// �ʼ�����
		StringBuffer buffer = new StringBuffer();
		buffer.append(content);
		//buffer.append("JAF 1.1.1 jar�����ص�ַ��http://www.oracle.com/technetwork/java/javase/downloads/index-135046.html");
		mailInfo.setContent(buffer.toString());
		
		// �����ʼ�
		SimpleMailSender sms = new SimpleMailSender();
		// ���������ʽ
		sms.sendTextMail(mailInfo);
		// ����html��ʽ
		//SimpleMailSender.sendHtmlMail(mailInfo);
		System.out.println("�ʼ��������");
		return 0;
	}
	
	public static void main(String[] args) {
		SendMailDemo.sendEmail("sss", "jmmsrbjut@126.com");
	}
}