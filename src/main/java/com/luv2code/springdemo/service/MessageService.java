package com.luv2code.springdemo.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.luv2code.springdemo.dataTransfert.MessageEntityModel;

public interface MessageService {
    public SseEmitter subscribe(String userID, Integer nNews);  
    
    public void /*String*/ dispatchEventJSON(MessageEntityModel article) throws Exception;
    
    public void unsubscribe(String userID);
    

   




    

	
}
