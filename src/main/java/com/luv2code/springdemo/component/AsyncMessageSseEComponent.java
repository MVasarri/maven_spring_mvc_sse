package com.luv2code.springdemo.component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.luv2code.springdemo.controller.HomeController;

import java.util.concurrent.Future;

@Component
public class AsyncMessageSseEComponent {
    
	private static final Logger logger
    = LoggerFactory.getLogger(HomeController.class);
	
    @Async
    public void asyncMethodWithVoidReturnType() {
        logger.debug("Execute method asynchronously. {}", Thread.currentThread().getName());
    }

    @Async
    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously " + Thread.currentThread().getName());
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
