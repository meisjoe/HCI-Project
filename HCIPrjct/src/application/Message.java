package application;

public class Message {
	
	protected int messageid;
	protected int roomid;
	protected int senderid;
	protected int receiverid;
	protected String messagecontent;
	
	public void setMessageid(int id){
		this.messageid = id;
	}
	public void setRoomid(int id){
		this.roomid = id;
	}
	public void setSenderid(int id){
		this.senderid=id;
	}
	public void setReceiverid(int id){
		this.receiverid=id;
	}
	public void setMessagecontent(String content){
		this.messagecontent=content;
	}
	public int getMessageid(){
		return this.messageid;
	}
	public int getRoomid(){
		return this.roomid;
	}
	public int getSenderid(){
		return this.senderid;
	}
	public int getReceiverid(){
		return this.receiverid;
	}
	public String getMessagecontent(){
		return this.messagecontent;
	}

}
