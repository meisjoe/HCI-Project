package application;

public class Room {

	protected String roomname;
	protected int roomid;
	
	public void setRoomname(String name){
		this.roomname=name;
	}
	public void setRoomid(int id){
		this.roomid = id;
	}
	public String getRoomname(){
		return this.roomname;
	}
	public int getRoomid(){
		return this.roomid;
	}
}
