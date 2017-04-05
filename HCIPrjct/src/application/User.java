package application;

public class User {

	protected String username;
	protected String password;
	protected int userID;
	
	public User() {
		this.username = "";
		this.password = "";
		this.userID = -1;
	}
	
	public User(String username, String password) {
		this.userID = -1;
		this.username = username;
		this.password = password;
	}
	
	public User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	public void setPassword(String pwd){
		this.password = pwd;
	}
	public void setUserid(int id){
		this.userID = id;
	}
	public String getUsername(){
		return this.username;
	}
	public String getPassword(){
		return this.password;
	}
	public int getUserid(){
		return this.userID;
	}
}
