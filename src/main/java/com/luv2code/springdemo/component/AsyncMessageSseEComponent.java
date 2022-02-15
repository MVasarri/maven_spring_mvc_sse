package com.luv2code.springdemo.component;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.luv2code.springdemo.controller.HomeController;
import com.luv2code.springdemo.dataTransfert.MessageEntityModel;
import com.luv2code.springdemo.service.MessageService;

@Component
public class AsyncMessageSseEComponent {
    
	private static final Logger logger
    = LoggerFactory.getLogger(HomeController.class);
	
	// need to inject our message Service
	@Autowired
	private  MessageService messageService;
	

	@Async
    public void async100mesJSON(MessageEntityModel article) throws Exception {
        logger.debug("Execute method asynchronously. {}", Thread.currentThread().getName());
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000);
                messageService.dispatchEventJSON(article);
            } catch (final InterruptedException e) {
            	logger.error("Errore nell'invio del messaggio tipo di errore: {}", e);
            }        	
        }
    }

    @Async
    public Future<String> asyncMethodWithReturnType() {
        logger.debug("Execute method asynchronously. {}", Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
            return new AsyncResult<>("hello world !!!!  "+ Thread.currentThread().getName());
        } catch (final InterruptedException e) {
        	
        }

        return null;
    }

//
//    @Async
//    public void asyncMethodWithExceptions() throws Exception {
//        throw new Exception("Throw message from asynchronous method. ");
//    }

}
