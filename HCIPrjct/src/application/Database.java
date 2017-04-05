package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private String connectionURL;
	private String username;
	private String password;
	
	public Database() {
		this.connectionURL = "";
		this.username = "";
		this.password = "";
	}
	
	public Database(String connectionURL, String username, String password) {
		this.connectionURL = connectionURL;
		this.username = username;
		this.password = password;
	}
	
	public Connection connectToDB() throws SQLException, ClassNotFoundException {
		//Connection to the database
		Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(this.connectionURL, this.username, this.password);
		System.out.println(con);
		return con;
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
		stmt.executeUpdate("INSERT INTO LoggedInUsers (userID, roomID) VALUES ('" + user.getUserid() + "', '" + room.getRoomid() + "');");
	}
	
	public Room createRoom(String name) throws SQLException, ClassNotFoundException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Room (Name) VALUES ('" + name + "');");
		ResultSet rs = stmt.executeQuery("SELECT RoomID FROM Room WHERE Name = '" + name + "'");
		rs.next();
		Room room = new Room(rs.getInt("RoomID"), name);
		return room;
	}
	
	public Message sendMessage(User sender, User receiver, Room room, String messageContent, String time) throws ClassNotFoundException, SQLException {
		Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Message (RoomID, UserID, toUserID, MessageContent, Time) VALUES ('" + room.getRoomid() + "', '" + sender.getUserid() + "', '" + receiver.getUserid() + "', '" + messageContent + "', '" + time + "');");
		ResultSet rs = stmt.executeQuery("SELECT MessageID FROM Message WHERE MessageContent = '" + messageContent + "'");
		rs.next();
		Message message = new Message(rs.getInt("MessageID"), room.getRoomid(), sender.getUserid(), receiver.getUserid(), messageContent, time);
		return message;
	}
	
}
