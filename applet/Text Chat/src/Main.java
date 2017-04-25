

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends FXApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3158410116546795662L;
	Timer timer;
	
	public String parsetospace(String s){
		if (s != null) {
			String[] parts = s.split(" ");
			s = parts[0];	
		}
		return s;
	}
	
	public void updateMessageList(ListView<String> displaymessages,  ObservableList<String> messdisp, int friendid, int currentid, Database chatDB, int nbrnewmess) throws ClassNotFoundException, SQLException {
    	List<String> tempDisplay = new ArrayList<String>();
    	tempDisplay.clear();
    	int tempMessagesToDisplayLength = chatDB.getMessagesToDisplay().length;
    	for(int i = 0; i < tempMessagesToDisplayLength; i++) {
    		String print = "";
    		String mystringdate = (chatDB.getMessagesToDisplay())[i].getTime();
    		String formattedDate = "";
    		if((((chatDB.getMessagesToDisplay())[i].getSenderID()==currentid)&&((chatDB.getMessagesToDisplay())[i].getReceiverID()==friendid))||(((chatDB.getMessagesToDisplay())[i].getSenderID()==friendid)&&((chatDB.getMessagesToDisplay())[i].getReceiverID()==currentid))){
    			SimpleDateFormat parser = new SimpleDateFormat("MMddyyyyHHmmss");
    			Date date;
				try {
					date = parser.parse(mystringdate);
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
    		    	formattedDate = formatter.format(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
    			if((chatDB.getMessagesToDisplay())[i].getSenderID()==currentid){
    				print=print+chatDB.getUserByID(currentid).getUsername();	
    			}
    			else if((chatDB.getMessagesToDisplay())[i].getSenderID()==friendid){
    				print=print+ chatDB.getUserByID(friendid).getUsername();
    			}
    			print = print + " on " + formattedDate + " : " + (chatDB.getMessagesToDisplay())[i].getmessageContent();
    			tempDisplay.add(print);
    		}
    	}
    	messdisp.clear();
    	messdisp.addAll(tempDisplay);
		displaymessages.setItems(messdisp);
		displaymessages.scrollTo(displaymessages.getItems().size()-1);
	}
	
	public void updateContactUsernames(Database chatDB, ListView<String> list, Button buttonClicked){
		String usernames[] = new String[chatDB.getUserContactList().length];
		for(int j = 0; j < chatDB.getUserContactList().length; j++) {
			usernames[j] = (chatDB.getUserContactList()[j]).getUsername();
			if(chatDB.getUserContactList()[j].getUsername().equals(chatDB.getCurrentConnectedUser().getUsername())){
				usernames[j]=usernames[j]+" (MYSELF)";
			}
		}
		ObservableList<String> friendlist = FXCollections.observableArrayList (usernames);
		list.setItems(friendlist);
		if (list.getItems().size() <= 1) {
			buttonClicked.setDisable(true);
		}
		else {
			buttonClicked.setDisable(false);
		}
	}
			
	public void initApplet() {
		try {		
			Database chatDB = new Database("jdbc:mysql://31.220.20.83:3306/u433658471_hcipj?useUnicode=true&characterEncoding=UTF-8&connectTimeout=10000&socketTimeout=10000", "u433658471_hugo", "Hm211195");
			chatDB.connectToDB();
			
			HBox topbox = new HBox();
			HBox bot = new HBox();
			
			VBox vroot = new VBox(topbox, bot);
			
			Scene scene = new Scene(vroot, fxPanel.getWidth(), fxPanel.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			topbox.setPrefWidth(scene.getWidth());
			topbox.setMinHeight(scene.getHeight()/6);
			topbox.getStyleClass().add("topbx"); // initial style

			bot.setPrefWidth(scene.getWidth());
			//bot.setPrefWidth(scene.getWidth());
			
			Label log = new Label("");
			log.setStyle("-fx-background-image: url('logo.png');"+"\n"+"-fx-background-size:"+scene.getWidth()/2+";"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
			log.setPrefWidth(scene.getWidth());
			log.setPrefHeight(scene.getHeight()/6);

			VBox signin = new VBox();
			signin.getStyleClass().add("style1"); // initial style
			
			VBox signup = new VBox();
			signup.getStyleClass().add("style1"); // initial style

			signin.setMinWidth(scene.getWidth()/2-7);
			signup.setMinWidth(scene.getWidth()/2-7);
			
			topbox.getChildren().add(log);
			bot.getChildren().addAll(signin, signup);
			bot.setPrefHeight(5*scene.getWidth()/6);
			
			Text signintext = new Text("Sign in.");
			signintext.getStyleClass().add("signtitle"); // initial style
			
			Text signuptext = new Text("Create an account.");
			signuptext.getStyleClass().add("signtitle"); // initial style

			Text usernamesignintext = new Text("Username: ");
			usernamesignintext.getStyleClass().add("style2"); // initial style

			TextField usernamesignin = new TextField();
			usernamesignin.setMinWidth(scene.getWidth()/4);
			usernamesignin.getStyleClass().add("style2"); // initial style

			Text pwdsignintext = new Text("Password: ");
			pwdsignintext.getStyleClass().add("style2"); // initial style

			PasswordField pwdsignin = new PasswordField();
			pwdsignin.setMinWidth(scene.getWidth()/4);
			pwdsignin.getStyleClass().add("style2"); // initial style

			Button signinbutton = new Button( "Sign in" );
			signinbutton.getStyleClass().add("signbutton"); // initial style
			
			Text usernamesignuptext = new Text("Username: ");
			usernamesignuptext.getStyleClass().add("style2"); // initial style

			TextField usernamesignup = new TextField();
			usernamesignup.setMinWidth(scene.getWidth()/4);
			usernamesignup.getStyleClass().add("style2"); // initial style

			Text pwdsignuptext = new Text("Password: ");
			pwdsignuptext.getStyleClass().add("style2"); // initial style

			PasswordField pwdsignup = new PasswordField();
			pwdsignup.setMinWidth(scene.getWidth()/4);
			pwdsignup.getStyleClass().add("style2"); // initial style

			Button signupbutton = new Button ("Sign up");
			signupbutton.getStyleClass().add("signbutton"); // initial style
			
			Text incorrectpwd = new Text ("The combination username/password is incorrect");
			incorrectpwd.setVisible(false);
			incorrectpwd.getStyleClass().add("incorrtext"); // initial style
			
			Text errsignup = new Text("");
			errsignup.getStyleClass().add("incorrtext"); // initial style
		
			signin.getChildren().addAll(signintext,usernamesignintext,usernamesignin,pwdsignintext, pwdsignin, signinbutton, incorrectpwd);
			signup.getChildren().addAll(signuptext,usernamesignuptext,usernamesignup,pwdsignuptext, pwdsignup, signupbutton, errsignup);

			HBox top = new HBox();
			HBox middle = new HBox();
			HBox bottom = new HBox();
			VBox mainpage = new VBox(top, middle, bottom);
			Scene scene2 = new Scene(mainpage, 1200, 800);
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			top.setPrefHeight(scene2.getHeight()/8);
			middle.setPrefHeight(6*scene2.getHeight()/8);
			bottom.setPrefHeight(scene2.getHeight()/8);
			
			Label welcomelogo = new Label("");
			welcomelogo.setStyle("-fx-background-image: url('logo2.png');"+"\n"+"-fx-background-size: 500px;"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
			welcomelogo.setPrefWidth(scene2.getWidth()/2);
			welcomelogo.setPrefHeight(scene2.getHeight()/4);
			
			Label gaptop = new Label("");
			gaptop.setPrefWidth(13*scene2.getWidth()/32);
			Button logout = new Button("");
			logout.setPrefWidth(3*scene2.getWidth()/32);
			logout.setPrefHeight(scene2.getHeight()/8);
			logout.setStyle("-fx-background-image: url('logoutsymb.png');"+"\n"+"-fx-background-size: 60px;"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 50%;");

			logout.getStyleClass().add("signbutton");

			ListView<String> list = new ListView<>();
			list.getStyleClass().add("contlist"); 

			list.setPrefWidth(scene2.getWidth()/4);
			
			ListView<String> displaymessages = new ListView<>();
			displaymessages.getStyleClass().add("messlist"); 

			ObservableList<String> messdisp = FXCollections.observableArrayList();
			displaymessages.setPrefWidth(3*scene2.getWidth()/4);
			
			Button addcontact = new Button("");
			addcontact.setStyle("-fx-background-image: url('addcontactsymb.png');"+"\n"+"-fx-background-size: 80px;"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 50%;");

			addcontact.setPrefWidth(scene2.getWidth()/8);
			addcontact.setPrefHeight(scene2.getHeight()/8);
			addcontact.getStyleClass().add("signbutton"); 

			Button removecontact=new Button("");
			removecontact.setStyle("-fx-background-image: url('remcontactsymb.png');"+"\n"+"-fx-background-size: 80px;"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 50%;");

			removecontact.setPrefWidth(scene2.getWidth()/8);
			removecontact.setPrefHeight(scene2.getHeight()/8);
			removecontact.getStyleClass().add("signbutton"); 

			TextField typingmessage = new TextField("Type your message here");
			typingmessage.setPrefWidth(3*scene2.getWidth()/4*7/8);
			typingmessage.setPrefHeight(scene2.getHeight()/8);
			
			Button sendbutton = new Button("");
			sendbutton.setPrefWidth(3*scene2.getWidth()/32);
			sendbutton.setPrefHeight(scene2.getHeight()/8);
			sendbutton.setStyle("-fx-background-image: url('sendsymb.png');"+"\n"+"-fx-background-size: 80px;"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 50%;");
			sendbutton.getStyleClass().add("signbutton"); 

			top.getChildren().addAll(welcomelogo, gaptop, logout);	
			middle.getChildren().addAll(list, displaymessages);
			bottom.getChildren().addAll(addcontact, removecontact, typingmessage, sendbutton);
			
			TextField contacttoadd = new TextField();
			contacttoadd.getStyleClass().add("txtaddrem"); 

			Button addconfirmation = new Button("Add");
			addconfirmation.getStyleClass().add("signbutton"); 

			Button canceladd = new Button("Cancel");
			canceladd.getStyleClass().add("signbutton"); 

			Text addindic = new Text ("Type the username of the contact you want to add: ");
			addindic.getStyleClass().add("txtaddrem"); 

			Text doesnotexist = new Text ("This user does not exist or he is already in your Contact List");
			doesnotexist.setVisible(false);	
			doesnotexist.getStyleClass().add("txtaddrem"); 
			
			final ComboBox<String> contacttoremove = new ComboBox<String>();

			Button removeconfirmation = new Button("Remove");
			removeconfirmation.getStyleClass().add("signbutton"); 

			Button cancelrem = new Button("Cancel");
			cancelrem.getStyleClass().add("signbutton"); 

			Text remindic = new Text ("Choose the contact you want to remove: ");
			remindic.getStyleClass().add("txtaddrem"); 

			//final Stage dialog = new Stage();
			//dialog.initModality(Modality.APPLICATION_MODAL);
			//dialog.initOwner(primaryStage);
			VBox dialogVbox = new VBox(20);
		    dialogVbox.getChildren().addAll(addindic,contacttoadd,addconfirmation,canceladd,doesnotexist);
		    Scene dialogScene = new Scene(dialogVbox, 600, 300);
		    dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    addconfirmation.setPrefWidth(dialogScene.getWidth());
			canceladd.setPrefWidth(dialogScene.getWidth());
		    
		    //final Stage dialog2 = new Stage();
			//dialog2.initModality(Modality.APPLICATION_MODAL);
			//dialog2.initOwner();
			VBox dialogVbox2 = new VBox(20);
		    dialogVbox2.getChildren().addAll(remindic,contacttoremove ,removeconfirmation ,cancelrem);
		    Scene dialogScene2 = new Scene(dialogVbox2, 600, 300);
		    dialogScene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    removeconfirmation.setPrefWidth(dialogScene2.getWidth());
		    cancelrem.setPrefWidth(dialogScene2.getWidth());
		    
			fxPanel.setScene(scene);
			fxPanel.setName("mymessenger");
		    
		    timer = new Timer();
        	TimerTask myTask = new TimerTask() {
			    @Override
			    public void run() {
			    	Platform.runLater(new Runnable() {
			    		public void run() {
					    	try {
								chatDB.getUserByID(1);
								if (chatDB.getIfUserIsConnected()) {
									System.out.println("CURRENTLY UPDATING MESSAGES...");
									int nbMessagesBeforeUpdate = chatDB.getNbMessages();
									chatDB.updateMessages(chatDB.getCurrentConnectedUser());
									if (chatDB.getNbMessages() > nbMessagesBeforeUpdate) {
										for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
											for (int j = 0; j < list.getItems().size(); j++) {
												System.out.println("list.getItems().get(j): " + list.getItems().get(j));
												System.out.println("chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername(): " + chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
												if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
													System.out.println("Gars clické: " + chatDB.getMessageReceiver());
													System.out.println("Message reçu de: " + chatDB.getMessagesToDisplay()[i].getSenderID());
													if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
														list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
													}
												}
											}
										}
										if (chatDB.getMessageReceiver() != null) {
											updateMessageList(displaymessages, messdisp, chatDB.getMessageReceiver().getUserID(), chatDB.getCurrentConnectedUser().getUserID(), chatDB, chatDB.getNbMessages()-nbMessagesBeforeUpdate);	
											System.out.println("UPDATINGLIST, NEW MESSAGES");
										}
									}
									System.out.println("MESSAGES UPDATED");
								}
					        } catch (ClassNotFoundException | SQLException e) {
								e.printStackTrace();
							}
			    		}
			    	});
			    }
			};
        	timer.schedule(myTask, 2000, 2000);
			
			//listeners
			
        	scene.widthProperty().addListener(new ChangeListener<Number>() {
        	    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
        	    	topbox.setPrefWidth(newSceneWidth.intValue());
        	        bot.setPrefWidth(newSceneWidth.intValue());
        	        //log.setPrefWidth(newSceneWidth.intValue());
        			log.setStyle("-fx-background-image: url('logo.png');"+"\n"+"-fx-background-size:"+newSceneWidth.intValue()/2+";"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
        	        signin.setPrefWidth(newSceneWidth.intValue()/2);
        			signup.setPrefWidth(newSceneWidth.intValue()/2);
        			usernamesignin.setPrefWidth(newSceneWidth.intValue()/5);
        			usernamesignup.setPrefWidth(newSceneWidth.intValue()/5);
        			pwdsignin.setPrefWidth(newSceneWidth.intValue()/5);
        			pwdsignup.setPrefWidth(newSceneWidth.intValue()/5);
        	    }
        	});
        	scene.heightProperty().addListener(new ChangeListener<Number>() {
        	    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
        	        System.out.println("Height: " + newSceneHeight);
        			topbox.setPrefHeight(newSceneHeight.intValue()/6);
        			bot.setPrefHeight(5*newSceneHeight.intValue()/6);
        			//log.setPrefHeight(newSceneHeight.intValue()/4);
        	    }
        	});
        	
			signinbutton.setOnMouseClicked((e)-> {
				try {
					chatDB.updateUsersList();
				} catch (ClassNotFoundException | SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				boolean found = false;
				int i=0;
				while(i!=chatDB.getUsersList().length){
					//System.out.println((chatDB.getUsersList())[i].getPassword());
					if((usernamesignin.getText().equals((chatDB.getUsersList())[i].getUsername()))&&(pwdsignin.getText().equals((chatDB.getUsersList())[i].getPassword()))){
						chatDB.setCurrentConnectedUser((chatDB.getUsersList())[i]);
						found = true;
						break;
					}
					i++;
				}
				
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				        list.getSelectionModel().select(0);
				        list.getFocusModel().focus(0);
				    }
				});
				
				if(found){
					try {
						chatDB.connectUser(chatDB.getCurrentConnectedUser());
						chatDB.updateContactString(chatDB.getCurrentConnectedUser());
						updateContactUsernames(chatDB, list, removecontact);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					incorrectpwd.setVisible(false);
					fxPanel.setScene(scene2);
					fxPanel.setName("mymessenger - connected as "+ chatDB.getCurrentConnectedUser().getUsername());
					//primaryStage.show();
				}
				else{
					incorrectpwd.setVisible(true);
				}
			});
			
			signupbutton.setOnMouseClicked((e)-> {
				try {
					chatDB.updateUsersList();
					if(usernamesignup.getText().equals("")){
						errsignup.setText("Please enter a valid username");
					}
					else if(pwdsignup.getText().length()<6){
						errsignup.setText("The password must contain at least 6 characters");

					}
					else {
						
						int i = 0;
						boolean exists=false;
						while (i != chatDB.getUsersList().length) {
							if((chatDB.getUsersList())[i].getUsername().equals(usernamesignup.getText())){
								exists=true;
								break;
							}
							i++;
						}
						if(!exists) {
							try {
								chatDB.createUser(usernamesignup.getText(), pwdsignup.getText());
								errsignup.setText("The user has been created. You can now sign in.");
								usernamesignup.clear();
								pwdsignup.clear();
							} catch (ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else{
							errsignup.setText("This username already exists");
						}
					}
				} catch (ClassNotFoundException | SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			});
			
			logout.setOnMouseClicked((e)-> {
				chatDB.setUserIsConnected(false);
				usernamesignin.clear();
				pwdsignin.clear();
				usernamesignup.clear();
				pwdsignup.clear();
				fxPanel.setScene(scene);
				//primaryStage.show();
				errsignup.setText("");
				incorrectpwd.setVisible(false);
				chatDB.setMessageReceiver(null);
			});

			
			typingmessage.setOnMouseClicked((e)-> {
				if(typingmessage.getText().contentEquals("Type your message here")){
					typingmessage.clear();
				}
			});
			
			scene2.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
				if ((!evt.getPickResult().equals(typingmessage))&&(typingmessage.getText().trim().isEmpty())) {
					typingmessage.setText("Type your message here");
				}
			});	
			
			addcontact.setOnMouseClicked((e)-> {				 			    
				fxPanel.setName("Add a contact");
			    fxPanel.setScene(dialogScene);
			});
			
			removecontact.setOnMouseClicked((e)->{
				int k=0;
				String usernames[] = new String[chatDB.getUserContactList().length-1];
				for(int j = 0; j < chatDB.getUserContactList().length; j++) {
					if(chatDB.getUserContactList()[j].getUsername().equals(chatDB.getCurrentConnectedUser().getUsername())) {
						j++;
					}
					usernames[k]=(chatDB.getUserContactList()[j]).getUsername();
					k++;
				}
				ObservableList<String> contlist = FXCollections.observableArrayList (usernames);
				contacttoremove.setItems(contlist);
				contacttoremove.getSelectionModel().selectFirst();
				fxPanel.setName("Remove a contact");
				fxPanel.setScene(dialogScene2);
			});
			
			addconfirmation.setOnMouseClicked((e)-> {
				boolean firstfound=false;
				boolean secondfound=false;
				int i=0;
				int j=0;
				int idtoadd=0;
				while(i!=chatDB.getUsersList().length){
					if((chatDB.getUsersList())[i].getUsername().equals(contacttoadd.getText())){
						idtoadd=(chatDB.getUsersList())[i].getUserID();
						firstfound = true;
						break;
					}
					i++;
				}
				while(j!=chatDB.getUserContactList().length){
					if((chatDB.getUserContactList())[j].getUsername().equals(contacttoadd.getText())){
						secondfound = true;
						break;
					}
					j++;
				}
				if(firstfound &&(!secondfound)){
					try {
						chatDB.addContact(chatDB.getCurrentConnectedUser(), idtoadd);
						doesnotexist.setVisible(false);
						chatDB.updateContactString(chatDB.getCurrentConnectedUser());
						updateContactUsernames(chatDB, list, removecontact);
						//System.out.println("added");
						fxPanel.setScene(scene2);
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}
				}
				else {
					doesnotexist.setVisible(true);
				}
			});
			
			canceladd.setOnMouseClicked((e)->{
				contacttoadd.clear();
				doesnotexist.setVisible(false);
				fxPanel.setScene(scene2);
				//dialog.hide();
				//dialog.close();
			});
			
			
			removeconfirmation.setOnMouseClicked((e) -> {
				int j=0;
				int idtoremove = -1;
				while(j < chatDB.getUserContactList().length){
		        	if((chatDB.getUserContactList()[j]).getUsername()==contacttoremove.getValue().toString()) {
		        		idtoremove = (chatDB.getUserContactList()[j]).getUserID();
		        		break;
		        	}
			        j++;
				}
				try {
					chatDB.removeContact(chatDB.getCurrentConnectedUser(), idtoremove);
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				try {
					chatDB.updateContactString(chatDB.getCurrentConnectedUser());
					//dialog2.close();
					fxPanel.setScene(scene2);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				updateContactUsernames(chatDB, list, removecontact);
			});
			
			cancelrem.setOnMouseClicked((e) -> {
				//dialog2.hide();
				//dialog2.close();
				fxPanel.setScene(scene2);
			});
			
			sendbutton.setOnMouseClicked((e) -> {
				timer.cancel();
				DateFormat myformat = new SimpleDateFormat("MMddyyyyHHmmss");
				Date date = new Date();
				String time = myformat.format(date);
				try {
					chatDB.sendMessage(chatDB.getCurrentConnectedUser(), chatDB.getMessageReceiver(), typingmessage.getText(), time);
					int nbMessagesBeforeUpdate = chatDB.getNbMessages();
					chatDB.updateMessages(chatDB.getCurrentConnectedUser());
					updateMessageList(displaymessages, messdisp, chatDB.getMessageReceiver().getUserID(), chatDB.getCurrentConnectedUser().getUserID(), chatDB, chatDB.getNbMessages()-nbMessagesBeforeUpdate);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				typingmessage.clear();
				typingmessage.setText("Type your message here");
				timer = new Timer();
	        	TimerTask myNewNewTask = new TimerTask() {
				    @Override
				    public void run() {
				    	Platform.runLater(new Runnable() {
				    		public void run() {
						    	try {
									chatDB.getUserByID(1);
									if (chatDB.getIfUserIsConnected()) {
										System.out.println("CURRENTLY UPDATING MESSAGES...");
										int nbMessagesBeforeUpdate = chatDB.getNbMessages();
										chatDB.updateMessages(chatDB.getCurrentConnectedUser());
										if (chatDB.getNbMessages() > nbMessagesBeforeUpdate) {
											for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
												for (int j = 0; j < list.getItems().size(); j++) {
													System.out.println("list.getItems().get(j): " + list.getItems().get(j));
													System.out.println("chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername(): " + chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
													if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
														System.out.println("Gars clické: " + chatDB.getMessageReceiver());
														System.out.println("Message reçu de: " + chatDB.getMessagesToDisplay()[i].getSenderID());
														if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
															list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
														}
													}
												}
											}
											if (chatDB.getMessageReceiver() != null) {
												updateMessageList(displaymessages, messdisp, chatDB.getMessageReceiver().getUserID(), chatDB.getCurrentConnectedUser().getUserID(), chatDB, chatDB.getNbMessages()-nbMessagesBeforeUpdate);	
												System.out.println("UPDATINGLIST, NEW MESSAGES");
											}
										}
										System.out.println("MESSAGES UPDATED");
									}
						        } catch (ClassNotFoundException | SQLException e) {
									e.printStackTrace();
								}
				    		}
				    	});
				    }
				};
	        	timer.schedule(myNewNewTask, 2000, 2000);
			});
			
			list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					timer.cancel();
			    	// Your action here
			    	messdisp.clear();
			        //System.out.println("Selected item: " + newValue);
			        int currentid = -1;
			        int friendid = -1;
			        int j = 0;
			        
			        while(j < chatDB.getUserContactList().length){
			        	if((chatDB.getUserContactList()[j]).getUsername().equals(parsetospace(newValue))) {
		        			friendid = (chatDB.getUserContactList()[j]).getUserID();
		        			break;
			        	}
			        	j++;
			        }
			        
			        j=0;
			        currentid = chatDB.getCurrentConnectedUser().getUserID();
			        
			        if((currentid != -1) && (friendid != -1)){
			        	try {
			        		chatDB.setMessageReceiver(chatDB.getUserByID(friendid));
			        		System.out.println("Message Receiver: " + chatDB.getMessageReceiver().getUserID());
			        		chatDB.setUserIsConnected(true);
			        		int nbMessageBeforeUpdate = chatDB.getNbMessages();
							chatDB.updateMessages(chatDB.getCurrentConnectedUser());
							updateMessageList(displaymessages, messdisp, friendid, currentid, chatDB, chatDB.getNbMessages()-nbMessageBeforeUpdate);
							list.getItems().set(list.getSelectionModel().getSelectedIndex(), list.getItems().get(list.getSelectionModel().getSelectedIndex()).replaceAll(" " + "\\(NEW MESSAGE\\)", ""));		
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        	timer = new Timer();
			        	TimerTask myNewTask = new TimerTask() {
						    @Override
						    public void run() {
						    	Platform.runLater(new Runnable() {
						    		public void run() {
								    	try {
											chatDB.getUserByID(1);
											if (chatDB.getIfUserIsConnected()) {
												System.out.println("CURRENTLY UPDATING MESSAGES...");
												int nbMessagesBeforeUpdate = chatDB.getNbMessages();
												chatDB.updateMessages(chatDB.getCurrentConnectedUser());
												if (chatDB.getNbMessages() > nbMessagesBeforeUpdate) {
													for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
														for (int j = 0; j < list.getItems().size(); j++) {
															System.out.println("list.getItems().get(j): " + list.getItems().get(j));
															System.out.println("chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername(): " + chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
															if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
																System.out.println("Gars clické: " + chatDB.getMessageReceiver().getUserID());
																System.out.println("Message reçu de: " + chatDB.getMessagesToDisplay()[i].getReceiverID());
																if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
																	list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
																}
															}
														}
													}
													if (chatDB.getMessageReceiver() != null) {
														updateMessageList(displaymessages, messdisp, chatDB.getMessageReceiver().getUserID(), chatDB.getCurrentConnectedUser().getUserID(), chatDB, chatDB.getNbMessages()-nbMessagesBeforeUpdate);	
														System.out.println("UPDATINGLIST, NEW MESSAGES");
													}
												}
												System.out.println("MESSAGES UPDATED");
											}
								        } catch (ClassNotFoundException | SQLException e) {
											e.printStackTrace();
										}
						    		}
						    	});
						    }
						};
			        	timer.schedule(myNewTask, 2000, 2000);
			        }
			    }
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}