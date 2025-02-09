package dev.pablito.dots.entity;

public class Message {
	private String message;
	private Actor actor;
	private From from;
	private String timestamp;
	private String type;
	
	private static class Actor {
		private String username;
		private String resource_url;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getResource_url() {
			return resource_url;
		}

		public void setResource_url(String resource_url) {
			this.resource_url = resource_url;
		}
		
	}
	
	public static class From {
		private String id;
		private String username;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
		
	
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
	
}
