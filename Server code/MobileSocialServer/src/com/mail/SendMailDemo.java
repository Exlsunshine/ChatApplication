package com.mail;


public class SendMailDemo
{
	public static int sendEmail(String content, String email)
	{
		// �����ʼ���������Ϣ
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.126.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		  
		// �����û���
		mailInfo.setUserName("jmmsrbjut@126.com");
		// ��������
		mailInfo.setPassword("jmmsr123456");
		// ����������
		mailInfo.setFromAddress("jmmsrbjut@126.com");
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
}