package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

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
	private Message[] messagesToDisplay;
	private User messageReceiver;
	private boolean aUserIsConnected;
	private int nbMessages;
	private List<ObservableList<String>> listOfMessagesByUser;
	private List<Integer> userIDAlreadyAddedToList;
	
	public Database() {
		this.connectionURL = "";
		this.username = "";
		this.password = "";
		this.nbOfUsers = 0;
		this.aUserIsConnected = false;
		this.nbMessages = 0;
	}
	
	public Database(String connectionURL, String username, String password) {
		this.connectionURL = connectionURL;
		this.username = username;
		this.password = password;
		this.nbOfUsers = 0;
		this.aUserIsConnected = false;
		this.nbMessages = 0;
	}
	
	public Connection connectToDB() throws SQLException, ClassNotFoundException {
		//Connection to the database
		Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(this.connectionURL, this.username, this.password);
		System.out.println(con);
		this.aUserIsConnected = false;
		return con;
	}
	
	public void updateUsersList() throws SQLException, ClassNotFoundException {
		//Connection con = this.connectToDB();
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
		//Connection con = this.connectToDB();
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
			//System.out.println(usersContactList[i].getUsername());
		}
	}
	
	public void updateMessages(User currentUser) throws SQLException, ClassNotFoundException {
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		
		ResultSet rsNumMessagesAsSender = stmt.executeQuery("SELECT COUNT(*) AS total FROM Message WHERE UserID=" + currentUser.getUserID() + " OR toUserID=" + currentUser.getUserID() + ";");
		rsNumMessagesAsSender.next();
		
		ResultSet rsMessages = stmt1.executeQuery("SELECT * FROM Message WHERE UserID=" + currentUser.getUserID() + " OR toUserID=" + currentUser.getUserID() + ";");
		
		int numMessages = rsNumMessagesAsSender.getInt("total");
		nbMessages = numMessages;
		
		//System.out.println(numMessages);
		messagesToDisplay = new Message[numMessages];
		
		for (int i = 0; i < numMessages; i++) {
			rsMessages.next();
			messagesToDisplay[i] = new Message(rsMessages.getInt("MessageID"), rsMessages.getInt("UserID"), rsMessages.getInt("toUserID"), rsMessages.getString("MessageContent"), rsMessages.getString("Time"));
		}
	}
	
	public void updateConnectedUsersList() throws ClassNotFoundException, SQLException {
		//Connection con = this.connectToDB();
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
			//System.out.println(usersConnected[i].getUsername());
		}
	}
	
	public User createUser(String name, String password) throws ClassNotFoundException, SQLException {
		//Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		stmt.executeUpdate("INSERT INTO User (Username, Password) VALUES ('" + name + "', '" + password + "');");
		ResultSet rs = stmt.executeQuery("SELECT UserID FROM User WHERE Username = '" + name + "'");
		rs.next();
		stmt1.executeUpdate("UPDATE User SET ContactList = '" + String.valueOf(rs.getInt("UserID")) + "' WHERE UserID = '" + rs.getInt("UserID") + "';");
		User user = new User(rs.getInt("UserID"), name, password, String.valueOf(rs.getInt("UserID")));
		return user;
	}
	
	public void connectUser(User user) throws SQLException, ClassNotFoundException {
		//Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO LoggedInUsers (UserID) VALUES ('" + user.getUserID() + "');");
	} 
	
	public Message sendMessage(User sender, User receiver, String messageContent, String time) throws ClassNotFoundException, SQLException {
		//Connection con = this.connectToDB();
		messageContent = messageContent.replaceAll("'","''");
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Message (UserID, toUserID, MessageContent, Time) VALUES ('" + sender.getUserID() + "', '" + receiver.getUserID() + "', '" + messageContent + "', '" + time + "');");
		ResultSet rs = stmt.executeQuery("SELECT MessageID FROM Message WHERE MessageContent = '" + messageContent + "'");
		rs.next();
		Message message = new Message(rs.getInt("MessageID"), sender.getUserID(), receiver.getUserID(), messageContent, time);
		return message;
	}
	
	public void addContact(User userToAddContact, int userID) throws ClassNotFoundException, SQLException {
		//Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		Statement stmt2 = con.createStatement();
		Statement stmt3 = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT ContactList FROM User WHERE UserID = '" + userToAddContact.getUserID() + "'");
		rs.next();
		
		ResultSet rsSecondUser = stmt2.executeQuery("SELECT ContactList FROM User WHERE UserID = '" + userID + "'");
		rsSecondUser.next();
		
		String newContactList = rs.getString("ContactList") + " " + String.valueOf(userID);
	
		String newContactListSecondUser = rsSecondUser.getString("ContactList") + " " + String.valueOf(userToAddContact.getUserID());
		
		stmt1.executeUpdate("UPDATE User SET ContactList = '" + newContactList + "' WHERE UserID = '" + userToAddContact.getUserID() + "';");
		stmt3.executeUpdate("UPDATE User SET ContactList = '" + newContactListSecondUser + "' WHERE UserID = '" + userID + "';");
		//System.out.println(newContactList);
	}
	
	public void removeContact(User userToRemoveContact, int userID) throws SQLException {
		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		Statement stmt2 = con.createStatement();
		Statement stmt3 = con.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT ContactList FROM User WHERE UserID = '" + userToRemoveContact.getUserID() + "'");
		rs.next();
		
		ResultSet rsSecondUser = stmt1.executeQuery("SELECT ContactList FROM User WHERE UserID = '" + userID + "'");
		rsSecondUser.next();
		
		String newContactList = rs.getString("ContactList");
		
		if (newContactList.contains(String.valueOf(userID))) {
			newContactList = newContactList.replaceAll(" " + String.valueOf(userID), "");
		}
		else {
			System.out.println("Error: User to remove is not in the Contact List.");
		}
		
		String newContactListSecondUser = rsSecondUser.getString("ContactList") + " " + String.valueOf(userToRemoveContact.getUserID());
		
		if (newContactListSecondUser.contains(String.valueOf(userToRemoveContact.getUserID()))) {
			newContactListSecondUser = newContactListSecondUser.replaceAll(" " + String.valueOf(userToRemoveContact.getUserID()), "");
		}
		else {
			System.out.println("Error: User to remove is not in the Contact List.");
		}
		
		stmt2.executeUpdate("UPDATE User SET ContactList = '" + newContactList + "' WHERE UserID = '" + userToRemoveContact.getUserID() + "';");
		stmt3.executeUpdate("UPDATE User SET ContactList = '" + newContactListSecondUser + "' WHERE UserID = '" + userID + "';");
		
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
	
	public User[] getUserContactList() {
		return usersContactList;
	}
	
	public User getUserByID(int userID) throws SQLException, ClassNotFoundException {
		//Connection con = this.connectToDB();
		Statement stmt = con.createStatement();
		Statement stmt2 = con.createStatement();
		
		ResultSet rsName = stmt.executeQuery("SELECT Username FROM User WHERE UserID = '" + userID + "'");
		rsName.next();
		
		ResultSet rsPassword = stmt2.executeQuery("SELECT Password FROM User WHERE UserID = '" + userID + "'");
		rsPassword.next();
		
		User temp = new User(userID, rsName.getString("Username"), rsPassword.getString("Password"));
		return temp;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public Message[] getMessagesToDisplay() {
		return this.messagesToDisplay;
	}
	
	public User getMessageReceiver() {
		return this.messageReceiver;
	}
	
	public void setMessageReceiver(User receiver) {
		this.messageReceiver = receiver;
	}
	
	public void setUserIsConnected(boolean isConnected) {
		this.aUserIsConnected = isConnected;
	}
	
	public boolean getIfUserIsConnected() {
		return this.aUserIsConnected;
	}
	
	public int getNbMessages() {
		return this.nbMessages;
	}
	
	public void createMessageListByUser(User connectedUser) {
		userIDAlreadyAddedToList = new ArrayList<Integer>();
		listOfMessagesByUser = new ArrayList<ObservableList<String>>();
		for (int i = 0; i < getNbMessages(); i++) {
			if (getMessagesToDisplay()[i].getReceiverID() != getCurrentConnectedUser().getUserID()) {
				//the Receiver ID is the contact of currentConnectedUser
				if (!userIDAlreadyAddedToList.contains(getMessagesToDisplay()[i].getReceiverID())) {
					//if receiver not yet in the list of observable list
					userIDAlreadyAddedToList.add(getMessagesToDisplay()[i].getReceiverID());
					listOfMessagesByUser.add(FXCollections.observableArrayList());
//					System.out.println("User added:" + getMessagesToDisplay()[i].getReceiverID());
				}
				listOfMessagesByUser.get(userIDAlreadyAddedToList.indexOf(getMessagesToDisplay()[i].getReceiverID())).add(getMessagesToDisplay()[i].messageContent);		
			}
			else {
				//the Sender ID is the contact of currentConnectedUser
				if (!userIDAlreadyAddedToList.contains(getMessagesToDisplay()[i].getSenderID())) {
					//if receiver not yet in the list of observable list
					userIDAlreadyAddedToList.add(getMessagesToDisplay()[i].getSenderID());
					listOfMessagesByUser.add(FXCollections.observableArrayList());
//					System.out.println("User added:" + getMessagesToDisplay()[i].getSenderID());
				}
				listOfMessagesByUser.get(userIDAlreadyAddedToList.indexOf(getMessagesToDisplay()[i].getSenderID())).add(getMessagesToDisplay()[i].messageContent);		
			}
		}
	}

	public void updateMessageListByUser(User connectedUser) {
		try {
			int nbMessagesBeforeUpdate = this.getNbMessages();
			this.updateMessages(connectedUser);
			if (nbMessagesBeforeUpdate != this.getNbMessages()) {
				for (int i = nbMessagesBeforeUpdate; i < this.getNbMessages(); i++) {
					if (getMessagesToDisplay()[i].getReceiverID() != getCurrentConnectedUser().getUserID()) {
						//the Receiver ID is the contact of currentConnectedUser
						if (!userIDAlreadyAddedToList.contains(getMessagesToDisplay()[i].getReceiverID())) {
							//if receiver not yet in the list of observable list
							userIDAlreadyAddedToList.add(getMessagesToDisplay()[i].getReceiverID());
							listOfMessagesByUser.add(FXCollections.observableArrayList());
						}
						listOfMessagesByUser.get(userIDAlreadyAddedToList.indexOf(getMessagesToDisplay()[i].getReceiverID())).add(getMessagesToDisplay()[i].messageContent);		
					}
					else {
						//the Sender ID is the contact of currentConnectedUser
						if (!userIDAlreadyAddedToList.contains(getMessagesToDisplay()[i].getSenderID())) {
							//if receiver not yet in the list of observable list
							userIDAlreadyAddedToList.add(getMessagesToDisplay()[i].getSenderID());
							listOfMessagesByUser.add(FXCollections.observableArrayList());
						}
						listOfMessagesByUser.get(userIDAlreadyAddedToList.indexOf(getMessagesToDisplay()[i].getSenderID())).add(getMessagesToDisplay()[i].messageContent);		
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ObservableList<String> getMessageObservableListFromUser(int userID) {
		return listOfMessagesByUser.get(userIDAlreadyAddedToList.indexOf(userID));
	}
	
	public List<Integer> getUsersWithMessage() {
		return userIDAlreadyAddedToList;
	}
	
}