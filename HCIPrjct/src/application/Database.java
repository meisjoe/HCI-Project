package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private Connection con;
	private String connectionURL;
	private String username;
	private String password;
	private int nbOfUsers;
	private int nbOfOnlineUsers;
	private User[] usersArray;
	private User[] usersConnected;
	private User currentConnectedUser;
	private User[] usersContactList;
	
	public Database() {
		this.connectionURL = "";
		this.username = "";
		this.password = "";
		this.nbOfUsers = 0;
	}
	
	public Database(String connectionURL, String username, String password) {
		this.connectionURL = connectionURL;
		this.username = username;
		this.password = password;
		this.nbOfUsers = 0;
	}
	
	public Connection connectToDB() throws SQLException, ClassNotFoundException {
		//Connection to the database
		Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(this.connectionURL, this.username, this.password);
		System.out.println(con);
		return con;
	}
	
	public void updateUsersList() throws SQLException, ClassNotFoundException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		Statement stmt2 = con.createStatement();
		Statement stmt3 = con.createStatement();
		Statement stmt4 = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM User;");
		rs.next();
		
		nbOfUsers = rs.getInt("total");
		
		usersArray = new User[nbOfUsers];
		
		ResultSet rsUsersID = stmt1.executeQuery("SELECT UserID FROM User");
		ResultSet rsUsersName = stmt2.executeQuery("SELECT Username FROM User");
		ResultSet rsUsersPassword = stmt3.executeQuery("SELECT Password FROM User");
		ResultSet rsUsersContactString = stmt4.executeQuery("SELECT ContactList FROM User");
		
		for (int i = 0; i < nbOfUsers; i++) {
			rsUsersID.next();
			rsUsersName.next();
			rsUsersPassword.next();
			rsUsersContactString.next();
			usersArray[i] = new User(rsUsersID.getInt("UserID"), rsUsersName.getString("Username"), rsUsersPassword.getString("Password"), rsUsersContactString.getString("ContactList"));
		}
	}
		
	public void updateContactString(User user) throws ClassNotFoundException, SQLException{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT ContactList FROM User WHERE UserID=" + user.getUserID() + ";");
		rs.next();
		user.setContactString(rs.getString("ContactList"));
		
		String tempContactID[] = user.getContactString().split(" ");
		int[] contactsID = new int[tempContactID.length];
		usersContactList = new User[tempContactID.length];
		
		for(int i =0 ; i<tempContactID.length;i++){
			contactsID[i] = Integer.parseInt(tempContactID[i]);
			usersContactList[i] = this.getUserByID(contactsID[i]);
			System.out.println(usersContactList[i].getUsername());
		}
	}
	
	public void updateConnectedUsersList() throws ClassNotFoundException, SQLException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM LoggedInUsers;");
		rs.next();
		
		nbOfOnlineUsers = rs.getInt("total");
		
		usersConnected = new User[nbOfOnlineUsers];
		
		ResultSet rsUsersID = stmt1.executeQuery("SELECT UserID FROM LoggedInUsers");
		
		for (int i = 0; i < nbOfOnlineUsers; i++) {
			rsUsersID.next();
			usersConnected[i] = this.getUserByID(rsUsersID.getInt("UserID"));
			System.out.println(usersConnected[i].getUsername());
		}
	}
	
	public User createUser(String name, String password) throws ClassNotFoundException, SQLException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO User (Username, Password) VALUES ('" + name + "', '" + password + "');");
		ResultSet rs = stmt.executeQuery("SELECT UserID FROM User WHERE Username = '" + name + "'");
		rs.next();
		User user = new User(rs.getInt("UserID"), name, password);
		return user;
	}
	
	public void connectUser(User user, Room room) throws SQLException, ClassNotFoundException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO LoggedInUsers (userID, roomID) VALUES ('" + user.getUserID() + "', '" + room.getRoomid() + "');");
	}
	
	public Message sendMessage(User sender, User receiver, Room room, String messageContent, String time) throws ClassNotFoundException, SQLException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Message (RoomID, UserID, toUserID, MessageContent, Time) VALUES ('" + room.getRoomid() + "', '" + sender.getUserID() + "', '" + receiver.getUserID() + "', '" + messageContent + "', '" + time + "');");
		ResultSet rs = stmt.executeQuery("SELECT MessageID FROM Message WHERE MessageContent = '" + messageContent + "'");
		rs.next();
		Message message = new Message(rs.getInt("MessageID"), room.getRoomid(), sender.getUserID(), receiver.getUserID(), messageContent, time);
		return message;
	}
	
	public void setCurrentConnectedUser(User connectedUser) {
		this.currentConnectedUser = connectedUser;
	}
	
	public User getCurrentConnectedUser() {
		return this.currentConnectedUser;
	}
	
	public User[] getUsersList() {
		return this.usersArray;
	}
	
	public User getUserByID(int userID) throws SQLException, ClassNotFoundException {
		Statement stmt = con.createStatement();
		Statement stmt2 = con.createStatement();
		
		ResultSet rsName = stmt.executeQuery("SELECT Username FROM User WHERE UserID = '" + userID + "'");
		rsName.next();
		
		ResultSet rsPassword = stmt2.executeQuery("SELECT Password FROM User WHERE UserID = '" + userID + "'");
		rsPassword.next();
		
		User temp = new User(userID, rsName.getString("Username"), rsPassword.getString("Password"));
		return temp;
	}
	
}
