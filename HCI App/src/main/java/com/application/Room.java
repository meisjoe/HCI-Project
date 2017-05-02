package com.application;

public class Room {

	protected String roomName;
	protected int roomID;
	
	public Room() {
		this.roomName = "";
		this.roomID = -1;
	}
	
	public Room(String name) {
		this.roomID = -1;
		this.roomName = name;
	}
	
	public Room(int ID, String name) {
		roomID = ID;
		roomName = name;
	} 
	
	public void setRoomname(String name){
		this.roomName = name;
	}
	public void setRoomid(int id){
		this.roomID = id;
	}
	public String getRoomname(){
		return this.roomName;
	}
	public int getRoomid(){
		return this.roomID;
	}
}
