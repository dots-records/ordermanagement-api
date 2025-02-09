package dev.pablito.dots.entity;

import java.util.List;

public class MessageManager {
	private String lastMessage;
	private List<Message> messages;
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
