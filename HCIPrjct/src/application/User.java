package application;

public class User {

	protected String username;
	protected String password;
	protected int userid;
	
	public void setUsername(String username){
		this.username=username;
	}
	public void setPassword(String pwd){
		this.password = pwd;
	}
	public void setUserid(int id){
		this.userid = id;
	}
	public String getUsername(){
		return this.username;
	}
	public String getPassword(){
		return this.password;
	}
	public int getUserid(){
		return this.userid;
	}
}
