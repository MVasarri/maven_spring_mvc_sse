 package com.luv2code.springdemo.dataTransfert;

public class ArticleModel {
	private String title;
    private String text;
    private String userID;
    
    
	public ArticleModel() {

	}
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	

}
