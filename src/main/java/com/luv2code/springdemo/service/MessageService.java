package com.luv2code.springdemo.service;

//import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.luv2code.springdemo.entity.Message;

public interface MessageService {
    public SseEmitter subscribe(String userID, Integer nNews);  
    
    public void dispatchEventJSON(Message message) throws Exception;
    
    public void unsubscribe(String userID);
    
//	public void saveMessage(MessageEntityModel theMessage);
//
//    public List<MessageEntityModel> getMessage();
   




    

	
}
