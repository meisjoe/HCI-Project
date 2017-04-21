package application;

public class User {

	protected String username;
	protected String password;
	protected int userID;
	protected String contactString;
	protected User[] contactList;
	//protected int roomID;
	
	public User() {
		this.username = "";
		this.password = "";
		this.contactString = "";
		this.userID = -1;
		//this.roomID = -1;
	}
	
	public User(String username, String password) {
		this.userID = -1;
		this.username = username;
		this.password = password;
		this.contactString = "";
		//this.roomID = -1
	}
	
	public User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.contactString = "";
		//this.roomID = -1;
	}
	
	public User(int userID, String username, String password, String contactString) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.contactString = contactString;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setPassword(String pwd){
		this.password = pwd;
	}
	
	public void setUserID(int id){
		this.userID = id;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public int getUserID(){
		return this.userID;
	}
	
	public void setContactString(String contactString) {
		this.contactString = contactString;
	}
	
	public String getContactString() {
		return this.contactString;
	}

}
