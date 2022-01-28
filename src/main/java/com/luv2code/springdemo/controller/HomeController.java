package com.luv2code.springdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	
    private static final Logger logger 
    	= LoggerFactory.getLogger(HomeController.class);
		
	@RequestMapping("/")
	public String showPage( ) {
		
		logger.trace("1. Log a message at the TRACE level");
		logger.debug("2. Log a message at the DEBUG level");
		logger.info("3. Log a message at the INFO level");
		logger.warn("4. Log a message at the WARN level");
		logger.error("5. Log a message at the ERROR level");

		return "main-menu";
	}
	
	@RequestMapping("/sseNews")
	public String showPageNews( ) {
		return "sse-news";
	}
	
	@RequestMapping("/newsSender")
	public String showPageNewsSender( ) {
		return "news-sender";
	}

}
