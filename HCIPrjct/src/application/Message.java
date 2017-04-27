package application;

public class Message {
	
	protected int messageID;
	protected int senderID;
	protected int receiverID;
	protected String messageContent;
	protected String time;
	
	public Message() {
		this.messageID = -1;
		this.senderID = -1;
		this.receiverID = -1;
		this.messageContent = "";
		this.time = "";
	}
	
	public Message(int senderID, int receiverID, String messageContent, String time) {
		this.messageID = -1;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.messageContent = messageContent;
		this.time = time;
	}
	
	public Message(int messageID, int senderID, int receiverID, String messageContent, String time) {
		this.messageID = messageID;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.messageContent = messageContent;
		this.time = time;
	}
	
	public void setmessageID(int ID){
		this.messageID = ID;
	}
	
	public void setSenderID(int ID){
		this.senderID = ID;
	}
	
	public void setReceiverID(int ID){
		this.receiverID = ID;
	}
	
	public void setmessageContent(String content){
		this.messageContent = content;
	}
	
	public int getmessageID(){
		return this.messageID;
	}
	
	public int getSenderID(){
		return this.senderID;
	} 
	
	public int getReceiverID(){
		return this.receiverID;
	}
	
	public String getmessageContent(){
		return this.messageContent;
	}

	public String getTime() {
		return this.time;
	}
	
}
