package com.luv2code.springdemo.DAO;

import java.util.List;

import com.luv2code.springdemo.entity.MessageEntityModel;

public interface MessageDAO {
	
	public List<MessageEntityModel> getMessages();
	
	public MessageEntityModel getMessage(int theId);
	
	public Long getLastID();
	
	public void saveMessage(MessageEntityModel theMessage);

}
