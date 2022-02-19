package com.luv2code.springdemo.DAO;

import java.util.List;

import com.luv2code.springdemo.entity.MessageEntityModel;

public interface MessageDAO {
	
	public List<MessageEntityModel> getMessage();
	
	public void saveMessage(MessageEntityModel theMessage);

}
