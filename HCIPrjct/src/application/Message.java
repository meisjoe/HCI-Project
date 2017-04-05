package application;

public class Message {
	
	protected int messageID;
	protected int roomID;
	protected int senderID;
	protected int receiverID;
	protected String messageContent;
	protected String time;
	
	public Message() {
		this.messageID = -1;
		this.roomID = -1;
		this.senderID = -1;
		this.receiverID = -1;
		this.messageContent = "";
		this.time = "";
	}
	
	public Message(int roomID, int senderID, int receiverID, String messageContent, String time) {
		this.messageID = -1;
		this.roomID = roomID;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.messageContent = messageContent;
		this.time = time;
	}
	
	public Message(int messageID, int roomID, int senderID, int receiverID, String messageContent, String time) {
		this.messageID = messageID;
		this.roomID = roomID;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.messageContent = messageContent;
		this.time = time;
	}
	
	public void setmessageID(int ID){
		this.messageID = ID;
	}
	public void setRoomID(int ID){
		this.roomID = ID;
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
	public int getRoomID(){
		return this.roomID;
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

}
