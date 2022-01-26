package com.luv2code.springdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	
    private static final Logger logger 
    = LoggerFactory.getLogger(HomeController.class);
	
	public static void main(String[] args) {
        logger.info("Example log from {}", HomeController.class.getSimpleName());

	}
	
	@RequestMapping("/")
	public String showPage( ) {
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
