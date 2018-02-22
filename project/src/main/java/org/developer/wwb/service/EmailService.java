package org.developer.wwb.service;


public interface EmailService {



	public void sendAsynEmailService(String receiver, String content, String title)throws Exception  ;


	public void sendEmailService(String receiver, String content, String title)throws Exception ;

}
