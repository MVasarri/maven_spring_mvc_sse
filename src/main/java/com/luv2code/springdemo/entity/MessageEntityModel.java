 package com.luv2code.springdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
 @Table(name="message" )
 public class MessageEntityModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private String messageID;
	
	@Column(name="title")
	private String title;
	
	@Column(name="text")
    private String text;
	
	@Column(name="userID")
    private String userID;
    
    
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public MessageEntityModel() {

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

//	@Override
//	public String toString() {
//		return "MessageEntityModel [messageID=" + messageID + ", title=" + title + ", text=" + text + ", userID="
//				+ userID + "]";
//	}

	
	
	

}
