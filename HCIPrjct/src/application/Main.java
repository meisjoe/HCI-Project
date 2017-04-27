package application;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
	
	private Timer timer;
	private int friendid = -1;
	
	public String parsetospace(String s){
		if (s != null) {
			String[] parts = s.split(" ");
			s = parts[0];	
		}
		return s;
	}
	
	public void updateContactUsernames(Database chatDB, ListView<String> list, Button buttonClicked) {
		String usernames[] = new String[chatDB.getUserContactList().length];
		for(int j = 0; j < chatDB.getUserContactList().length; j++) {
			usernames[j] = (chatDB.getUserContactList()[j]).getUsername();
			if(chatDB.getUserContactList()[j].getUsername().equals(chatDB.getCurrentConnectedUser().getUsername())) {
				usernames[j] = usernames[j] + " (MYSELF)";
			}
		}
		list.getItems().clear();
		ObservableList<String> friendlist = FXCollections.observableArrayList (usernames);
		list.setItems(friendlist);
		if (list.getItems().size() <= 1) {
			buttonClicked.setDisable(true);
		}
		else {
			buttonClicked.setDisable(false);
		}
	}
			
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setWidth(800);
			primaryStage.setHeight(600);
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			
			Database chatDB = new Database("jdbc:mysql://31.220.20.83:3306/u433658471_hcipj?useUnicode=true&characterEncoding=UTF-8&connectTimeout=10000&socketTimeout=10000", "u433658471_hugo", "Hm211195");
			chatDB.connectToDB();
			
			HBox topbox = new HBox();
			HBox bot = new HBox();
			
			VBox root = new VBox(topbox, bot);
			
			Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			topbox.setPrefWidth(primaryStage.getWidth());
			topbox.setMinHeight(primaryStage.getHeight()/6);
			topbox.getStyleClass().add("topbx"); 

			bot.setPrefWidth(primaryStage.getWidth());
			//bot.setPrefWidth(scene.getWidth());
			
			Label log = new Label("");
			log.setStyle("-fx-background-image: url('logo.png');"+"\n"+"-fx-background-size:"+scene.getWidth()/2+";"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
			log.setPrefWidth(primaryStage.getWidth());
			log.setPrefHeight(primaryStage.getHeight()/6);

			VBox signin = new VBox();
			signin.getStyleClass().add("style1"); 
			
			VBox signup = new VBox();
			signup.getStyleClass().add("style1"); 

			signin.setMinWidth(primaryStage.getWidth()/2-7);
			signup.setMinWidth(primaryStage.getWidth()/2-7);
			
			topbox.getChildren().add(log);
			bot.getChildren().addAll(signin, signup);
			bot.setPrefHeight(5*primaryStage.getWidth()/6);
			
			Text signintext = new Text("Sign in.");
			signintext.getStyleClass().add("signtitle"); 
			
			Text signuptext = new Text("Create an account.");
			signuptext.getStyleClass().add("signtitle"); 

			Text usernamesignintext = new Text("Username: ");
			usernamesignintext.getStyleClass().add("style2"); 

			TextField usernamesignin = new TextField();
			usernamesignin.setMinWidth(primaryStage.getWidth()/4);
			usernamesignin.getStyleClass().add("style2"); 

			Text pwdsignintext = new Text("Password: ");
			pwdsignintext.getStyleClass().add("style2"); 

			PasswordField pwdsignin = new PasswordField();
			pwdsignin.setMinWidth(primaryStage.getWidth()/4);
			pwdsignin.getStyleClass().add("style2"); 

			Button signinbutton = new Button("Sign in");
			signinbutton.getStyleClass().add("signbutton");
			
			Text usernamesignuptext = new Text("Username: ");
			usernamesignuptext.getStyleClass().add("style2"); 

			TextField usernamesignup = new TextField();
			usernamesignup.setMinWidth(primaryStage.getWidth()/4);
			usernamesignup.getStyleClass().add("style2"); 

			Text pwdsignuptext = new Text("Password: ");
			pwdsignuptext.getStyleClass().add("style2"); 

			PasswordField pwdsignup = new PasswordField();
			pwdsignup.setMinWidth(primaryStage.getWidth()/4);
			pwdsignup.getStyleClass().add("style2"); 

			Button signupbutton = new Button ("Sign up");
			signupbutton.getStyleClass().add("signbutton"); 
			
			Text incorrectpwd = new Text ("The combination username/password is incorrect");
			incorrectpwd.setVisible(false);
			incorrectpwd.getStyleClass().add("incorrtext"); 
			
			Text errsignup = new Text("");
			errsignup.getStyleClass().add("incorrtext");
		
			signin.getChildren().addAll(signintext,usernamesignintext,usernamesignin,pwdsignintext, pwdsignin, signinbutton, incorrectpwd);
			signup.getChildren().addAll(signuptext,usernamesignuptext,usernamesignup,pwdsignuptext, pwdsignup, signupbutton, errsignup);

			HBox top = new HBox();
			HBox middle = new HBox();
			HBox bottom = new HBox();
			VBox mainpage = new VBox(top, middle, bottom);
			Scene scene2 = new Scene(mainpage, primaryStage.getWidth(), primaryStage.getHeight());
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			top.setPrefHeight(primaryStage.getHeight()/9.5);
			top.getStyleClass().add("backgreen");
			
			bottom.setPrefHeight(primaryStage.getHeight()/8);
			bottom.getStyleClass().add("backgreen");

			Label welcomelogo = new Label("");
			welcomelogo.setStyle("-fx-background-image: url('logo2.png');"+"\n"+"-fx-background-size:"+primaryStage.getWidth()/2+";"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
			welcomelogo.setPrefWidth(primaryStage.getWidth()/2);
			welcomelogo.setPrefHeight(primaryStage.getHeight()/9);
			
			Label gaptop = new Label("");
			gaptop.setPrefWidth(13*primaryStage.getWidth()/32);
			
			Button logout = new Button("");
			logout.setMinWidth(3*primaryStage.getWidth()/32);
			logout.setPrefHeight(primaryStage.getHeight()/8);
			logout.setStyle("-fx-background-image: url('logoutsymb.png');"+"\n"+"-fx-background-size:"+3*primaryStage.getWidth()/64+" "+ primaryStage.getHeight()/16+";\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 50%;");
			logout.getStyleClass().add("signbutton");

			final Tooltip logoutTooltip = new Tooltip();
			logoutTooltip.setText(
			    "Log Out"
			);
			logout.setTooltip(logoutTooltip);
			
			ListView<String> list = new ListView<>();
			list.getStyleClass().add("contlist"); 
			list.setPrefWidth(primaryStage.getWidth()/4);
			list.setMinHeight(6*primaryStage.getHeight()/9);

			ListView<String> displaymessages = new ListView<>();
			displaymessages.getStyleClass().add("messlist"); 

			ObservableList<String> messdisp = FXCollections.observableArrayList();
			displaymessages.setPrefWidth(3*primaryStage.getWidth()/4.1);
			displaymessages.setMinHeight(6*primaryStage.getHeight()/8.5);
			
			middle.setMinWidth(primaryStage.getWidth()-500);
			
			Button addcontact = new Button("");
			addcontact.setStyle("-fx-background-image: url('addcontactsymb.png');"+"\n"+"-fx-background-size:"+primaryStage.getWidth()/16+" "+ primaryStage.getHeight()/16+";\n"+"-fx-background-repeat: no-repeat;"+"-fx-background-position: 50%;");
			addcontact.setPrefWidth(primaryStage.getWidth()/8);
			addcontact.setMinHeight(primaryStage.getHeight()/8);
			addcontact.getStyleClass().add("signbutton"); 

			final Tooltip addContactTooltip = new Tooltip();
			addContactTooltip.setText(
			    "Add a contact"
			);
			addcontact.setTooltip(addContactTooltip);
			
			Button removecontact = new Button("");
			removecontact.setStyle("-fx-background-image: url('remcontactsymb.png');"+"\n"+"-fx-background-size:"+primaryStage.getWidth()/16+" "+ primaryStage.getHeight()/16+";\n"+"-fx-background-repeat: no-repeat;"+"-fx-background-position: 50%;");
			removecontact.setPrefWidth(primaryStage.getWidth()/8);
			removecontact.setMinHeight(primaryStage.getHeight()/8);
			removecontact.getStyleClass().add("signbutton"); 

			final Tooltip removeContactTooltip = new Tooltip();
			removeContactTooltip.setText(
			    "Remove a contact"
			);
			removecontact.setTooltip(removeContactTooltip);
			
			TextField typingmessage = new TextField("Type your message here");
			typingmessage.setPrefWidth(3*primaryStage.getWidth()/4*6/7);
			typingmessage.setMinHeight(primaryStage.getHeight()/8);
			
			Button sendbutton = new Button("");
			sendbutton.defaultButtonProperty();
			sendbutton.setMinWidth(3*primaryStage.getWidth()/32);
			sendbutton.setPrefHeight(primaryStage.getHeight()/8);
			sendbutton.setStyle("-fx-background-image: url('sendsymb.png');"+"\n"+"-fx-background-size:"+3*primaryStage.getWidth()/64+" "+ primaryStage.getHeight()/16+";\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 50%;");
			sendbutton.getStyleClass().add("signbutton"); 

			final Tooltip sendMessageTooltip = new Tooltip();
			sendMessageTooltip.setText(
			    "Send a message"
			);
			sendbutton.setTooltip(sendMessageTooltip);
			
			top.getChildren().addAll(welcomelogo, gaptop, logout);	
			middle.getChildren().addAll(list, displaymessages);
			bottom.getChildren().addAll(addcontact, removecontact, typingmessage, sendbutton);
			
			TextField contacttoadd = new TextField();
			contacttoadd.getStyleClass().add("style2"); 

			Button addconfirmation = new Button("Add");
			addconfirmation.getStyleClass().add("signbutton"); 

			Button canceladd = new Button("Cancel");
			canceladd.getStyleClass().add("signbutton"); 

			Text addindic = new Text ("Type the username of the contact you want to add: ");
			addindic.getStyleClass().add("txtaddrem"); 

			Text doesnotexist = new Text ("This user does not exist or he is already in your Contact List");
			doesnotexist.setVisible(false);	
			doesnotexist.getStyleClass().add("txtaddrem"); 
			
			ComboBox contacttoremove = new ComboBox();
			contacttoremove.getStyleClass().add("signbutton");
			
			Button removeconfirmation = new Button("Remove");
			removeconfirmation.getStyleClass().add("signbutton"); 

			Button cancelrem = new Button("Cancel");
			cancelrem.getStyleClass().add("signbutton"); 

			Text remindic = new Text ("Choose the contact you want to remove: ");
			remindic.getStyleClass().add("txtaddrem"); 
			
			final Stage dialog = new Stage();
		    dialog.setResizable(false);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(primaryStage);
			
			VBox dialogVbox = new VBox(20);
			dialogVbox.getStyleClass().add("backgreen"); 
		    dialogVbox.getChildren().addAll(addindic,contacttoadd,addconfirmation,canceladd,doesnotexist);
		    
		    Scene dialogScene = new Scene(dialogVbox, 600, 300);
		    dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    addconfirmation.setPrefWidth(dialogScene.getWidth());
			canceladd.setPrefWidth(dialogScene.getWidth());
		    
		    final Stage dialog2 = new Stage();
		    dialog2.setResizable(false);
			dialog2.initModality(Modality.APPLICATION_MODAL);
			dialog2.initOwner(primaryStage);
			
			VBox dialogVbox2 = new VBox(20);
			dialogVbox2.getStyleClass().add("backgreen"); 
		    dialogVbox2.getChildren().addAll(remindic,contacttoremove ,removeconfirmation ,cancelrem);
		   
		    Scene dialogScene2 = new Scene(dialogVbox2, 600, 300);
		    dialogScene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    removeconfirmation.setPrefWidth(dialogScene2.getWidth());
		    cancelrem.setPrefWidth(dialogScene2.getWidth());
			contacttoremove.setPrefWidth(dialogScene2.getWidth());
		    
			primaryStage.setScene(scene);
			primaryStage.setTitle("mymessenger");
		    primaryStage.show();
		    
		    timer = new Timer();
        	TimerTask myTask = new TimerTask() {
			    @Override
			    public void run() {
			    	Platform.runLater(new Runnable() {
			    		public void run() {
					    	try {
								chatDB.getUserByID(0);
								if (chatDB.getIfUserIsConnected()) {
									System.out.println("CURRENTLY UPDATING MESSAGES...");
									int nbMessagesBeforeUpdate = chatDB.getNbMessages();
									chatDB.updateMessageListByUser(chatDB.getCurrentConnectedUser());
									if (chatDB.getNbMessages() > nbMessagesBeforeUpdate) {
										for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
											for (int j = 0; j < list.getItems().size(); j++) {
												if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
													if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
														list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
														Popup notif = new Popup();
														Text mynotif = new Text("You have a new message from "+ chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
														mynotif.getStyleClass().add("popup");
														notif.getContent().add(mynotif);
														notif.setOnShown(new EventHandler<WindowEvent>() {
													        @Override
													        public void handle(WindowEvent e) {
													            notif.setX(primaryStage.getX() + primaryStage.getWidth()/2 - notif.getWidth()/2);
													            notif.setY(primaryStage.getY() + primaryStage.getHeight()/2 - notif.getHeight()/2);
													        }
													    });        
														notif.show(primaryStage);
														PauseTransition delay = new PauseTransition(Duration.seconds(5));
														delay.setOnFinished( event -> notif.hide() );
														delay.play();
													}
													else {
														displaymessages.scrollTo(displaymessages.getItems().size()-1);
													}
												}
											}
										}
										if (chatDB.getMessageReceiver() != null) {
											if (chatDB.getMessageObservableListFromUser(friendid) != null) {
												displaymessages.setVisible(true);
							        			displaymessages.setItems(chatDB.getMessageObservableListFromUser(friendid));
							        		}
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
        	timer.schedule(myTask, 4000, 4000);
			
			//listeners
			
        	primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
        	    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
        	    	topbox.setPrefWidth(newSceneWidth.intValue());
        	        bot.setPrefWidth(newSceneWidth.intValue());
        	        log.setStyle("-fx-background-image: url('logo.png');"+"\n"+"-fx-background-size:"+newSceneWidth.intValue()/2+";"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
        	        signin.setPrefWidth(newSceneWidth.intValue()/2);
        			signup.setPrefWidth(newSceneWidth.intValue()/2);
        			usernamesignin.setPrefWidth(newSceneWidth.intValue()/5);
        			usernamesignup.setPrefWidth(newSceneWidth.intValue()/5);
        			pwdsignin.setPrefWidth(newSceneWidth.intValue()/5);
        			pwdsignup.setPrefWidth(newSceneWidth.intValue()/5);

        			welcomelogo.setPrefWidth(newSceneWidth.intValue()/2);
        			gaptop.setPrefWidth(13*newSceneWidth.intValue()/32);
        			logout.setPrefWidth(3*newSceneWidth.intValue()/32);
        			list.setPrefWidth(newSceneWidth.intValue()/4);
        			displaymessages.setPrefWidth(3*newSceneWidth.intValue()/4);
        			addcontact.setPrefWidth(newSceneWidth.intValue()/8);
        			removecontact.setPrefWidth(newSceneWidth.intValue()/8);
        			typingmessage.setPrefWidth(3*newSceneWidth.intValue()/4*7/8);
        			sendbutton.setPrefWidth(3*newSceneWidth.intValue()/32);
        	    }
        	});
        	
        	primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
        	    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
        	        topbox.setPrefHeight(newSceneHeight.intValue()/6);
        			bot.setPrefHeight(5*newSceneHeight.intValue()/6);
        			displaymessages.setMinHeight(6*newSceneHeight.intValue()/9);       			
        			top.setMinHeight(newSceneHeight.intValue()/8);
        			bottom.setMinHeight(newSceneHeight.intValue()/8);
        			
        			welcomelogo.setPrefHeight(newSceneHeight.intValue()/8);
        			logout.setPrefHeight(newSceneHeight.intValue()/8);
        			addcontact.setMinHeight(newSceneHeight.intValue()/8);
        			removecontact.setMinHeight(newSceneHeight.intValue()/8);
        			typingmessage.setMinHeight(newSceneHeight.intValue()/8);
        			sendbutton.setMinHeight(newSceneHeight.intValue()/8);
        			list.setMinHeight(6*newSceneHeight.intValue()/9);
        	    }
        	});
        	
			signinbutton.setOnAction((e)-> {
				try {
					chatDB.updateUsersList();
				} catch (ClassNotFoundException | SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				boolean found = false;
				int i = 0;
				while(i != chatDB.getUsersList().length){
					if((usernamesignin.getText().equals((chatDB.getUsersList())[i].getUsername())) && (pwdsignin.getText().equals((chatDB.getUsersList())[i].getPassword()))) {
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
						chatDB.updateMessages(chatDB.getCurrentConnectedUser());
						chatDB.createMessageListByUser(chatDB.getCurrentConnectedUser());
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					incorrectpwd.setVisible(false);
					primaryStage.setScene(scene2);
					primaryStage.setTitle("mymessenger - connected as "+ chatDB.getCurrentConnectedUser().getUsername());
				}
				else{
					incorrectpwd.setVisible(true);
				}
			});
			
			signupbutton.setOnAction((e)-> {
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
						boolean exists = false;
						while (i != chatDB.getUsersList().length) {
							if((chatDB.getUsersList())[i].getUsername().equals(usernamesignup.getText())){
								exists = true;
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
								e1.printStackTrace();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
						else{
							errsignup.setText("This username already exists");
						}
					}
				} catch (ClassNotFoundException | SQLException e2) {
					e2.printStackTrace();
				}
			});
			
			logout.setOnMouseClicked((e)-> {
				timer.cancel();
				list.getSelectionModel().clearSelection();
				//chatDB.getMessageObservableListFromUser(chatDB.getCurrentConnectedUser().getUserID()).clear();
				chatDB.setUserIsConnected(false);
				usernamesignin.clear();
				pwdsignin.clear();
				usernamesignup.clear();
				pwdsignup.clear();
				primaryStage.setScene(scene);
				errsignup.setText("");
				incorrectpwd.setVisible(false);
				primaryStage.setTitle("mymessenger");
				chatDB.setMessageReceiver(null);
				topbox.setPrefWidth(primaryStage.getWidth());
    	        bot.setPrefWidth(primaryStage.getWidth());
    	        log.setStyle("-fx-background-image: url('logo.png');"+"\n"+"-fx-background-size:" + primaryStage.getWidth()/2+";"+"\n"+"-fx-background-repeat: no-repeat;"+"\n"+"-fx-background-position: 0%;");
    	        signin.setPrefWidth(primaryStage.getWidth()/2-7);
    			signup.setPrefWidth(primaryStage.getWidth()/2-7);
    			usernamesignin.setPrefWidth(primaryStage.getWidth()/4);
    			usernamesignup.setPrefWidth(primaryStage.getWidth()/4);
    			pwdsignin.setPrefWidth(primaryStage.getWidth()/4);
    			pwdsignup.setPrefWidth(primaryStage.getWidth()/4);
    			topbox.setPrefHeight(primaryStage.getHeight()/6-37);
    			bot.setPrefHeight(5*primaryStage.getHeight()/6-37);
			});
			
			typingmessage.setOnMouseClicked((e)-> {
				if(typingmessage.getText().contentEquals("Type your message here")){
					typingmessage.clear();
				}
			});
			
			scene2.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
				if ((!evt.getPickResult().equals(typingmessage)) && (typingmessage.getText().trim().isEmpty())) {
					typingmessage.setText("Type your message here");
				}
			});	
			
			addcontact.setOnMouseClicked((e)-> {				 			    
				dialog.setTitle("Add a contact");
			    dialog.setScene(dialogScene);
			    dialog.showAndWait();
			});
			
			removecontact.setOnMouseClicked((e)->{
				int k = 0;
				String usernames[] = new String[chatDB.getUserContactList().length-1];
				for(int j = 0; j < chatDB.getUserContactList().length; j++) {
					if(chatDB.getUserContactList()[j].getUsername().equals(chatDB.getCurrentConnectedUser().getUsername())) {
						j++;
					}
					usernames[k] = (chatDB.getUserContactList()[j]).getUsername();
					k++;
				}
				ObservableList<String> contlist = FXCollections.observableArrayList (usernames);
				contacttoremove.setItems(contlist);
				contacttoremove.getSelectionModel().selectFirst();
				dialog2.setTitle("Remove a contact");
				dialog2.setScene(dialogScene2);
				dialog2.showAndWait();
			});
			
			addconfirmation.setOnMouseClicked((e)-> {
				boolean firstfound = false;
				boolean secondfound = false;
				int i = 0;
				int j = 0;
				int idtoadd = 0;
				while(i != chatDB.getUsersList().length){
					if((chatDB.getUsersList())[i].getUsername().equals(contacttoadd.getText())) {
						idtoadd = (chatDB.getUsersList())[i].getUserID();
						firstfound = true;
						break;
					}
					i++;
				}
				while(j != chatDB.getUserContactList().length){
					if((chatDB.getUserContactList())[j].getUsername().equals(contacttoadd.getText())) {
						secondfound = true;
						break;
					}
					j++;
				}
				if(firstfound && (!secondfound)){
					try {
						chatDB.addContact(chatDB.getCurrentConnectedUser(), idtoadd);
						doesnotexist.setVisible(false);
						chatDB.updateContactString(chatDB.getCurrentConnectedUser());
						updateContactUsernames(chatDB, list, removecontact);
						dialog.close();
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
				dialog.hide();
				dialog.close();
			});
		
			removeconfirmation.setOnMouseClicked((e) -> {
				int j = 0;
				int idtoremove = -1;
				while(j < chatDB.getUserContactList().length){
		        	if((chatDB.getUserContactList()[j]).getUsername() == contacttoremove.getValue().toString()) {
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
					dialog2.close();
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				updateContactUsernames(chatDB, list, removecontact);
			});
			
			cancelrem.setOnMouseClicked((e) -> {
				dialog2.hide();
				dialog2.close();
			});
			
			sendbutton.setOnAction((e) -> {
				try {
					timer.cancel();
					DateFormat myformat = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
					Date date = new Date();
					StringBuilder messageToString = new StringBuilder();
					String time = myformat.format(date);
					messageToString
						.append(chatDB.getUserByID(chatDB.getCurrentConnectedUser().getUserID()).getUsername())
						.append(" on ") 
						.append(time)
						.append(": ")
						.append(typingmessage.getText())
						.toString();
					chatDB.sendMessage(chatDB.getCurrentConnectedUser(), chatDB.getMessageReceiver(), messageToString.toString(), time);
					chatDB.updateMessageListByUser(chatDB.getCurrentConnectedUser());
					if(!displaymessages.isVisible()){
						displaymessages.setItems(chatDB.getMessageObservableListFromUser(friendid));
						displaymessages.setVisible(true);
					}
					displaymessages.scrollTo(displaymessages.getItems().size()-1);
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
									chatDB.getUserByID(0);
									if (chatDB.getIfUserIsConnected()) {
										System.out.println("CURRENTLY UPDATING MESSAGES...");
										int nbMessagesBeforeUpdate = chatDB.getNbMessages();
										chatDB.updateMessageListByUser(chatDB.getCurrentConnectedUser());
										if (chatDB.getNbMessages() > nbMessagesBeforeUpdate) {
											for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
												for (int j = 0; j < list.getItems().size(); j++) {
													if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
														if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
															list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
															Popup notif = new Popup();
															Text mynotif = new Text("You have a new message from "+ chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
															mynotif.getStyleClass().add("popup");
															notif.getContent().add(mynotif);
															notif.setOnShown(new EventHandler<WindowEvent>() {
														        @Override
														        public void handle(WindowEvent e) {
														            notif.setX(primaryStage.getX() + primaryStage.getWidth()/2 - notif.getWidth()/2);
														            notif.setY(primaryStage.getY() + primaryStage.getHeight()/2 - notif.getHeight()/2);
														        }
														    });        
															notif.show(primaryStage);
															PauseTransition delay = new PauseTransition(Duration.seconds(5));
															delay.setOnFinished( event -> notif.hide() );
															delay.play();
														}
														else {
															displaymessages.scrollTo(displaymessages.getItems().size()-1);
														}
													}
												}
											}
											if (chatDB.getMessageReceiver() != null) {
												if (chatDB.getMessageObservableListFromUser(friendid) != null) {
													displaymessages.setVisible(true);
								        			displaymessages.setItems(chatDB.getMessageObservableListFromUser(friendid));
								        		}
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
	        	timer.schedule(myNewNewTask, 4000, 4000);
			});
			
			list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					timer.cancel();
					//messdisp.clear();
			        int currentid = -1;
			        int j = 0;
			        
			        while(j < chatDB.getUserContactList().length) {
			        	if((chatDB.getUserContactList()[j]).getUsername().equals(parsetospace(newValue))) {
		        			friendid = (chatDB.getUserContactList()[j]).getUserID();
		        			break;
			        	}
			        	j++;
			        }
			        
			        j = 0;
			        currentid = chatDB.getCurrentConnectedUser().getUserID();
			        
			        if((currentid != -1) && (friendid != -1)) {
			        	try {
			        		chatDB.setMessageReceiver(chatDB.getUserByID(friendid));
			        		chatDB.setUserIsConnected(true);
			        		chatDB.updateMessages(chatDB.getCurrentConnectedUser());
			        		if (chatDB.getUsersWithMessage().contains(friendid)) {
			        			displaymessages.setVisible(true);
			        			displaymessages.setItems(chatDB.getMessageObservableListFromUser(friendid));
			        			displaymessages.scrollTo(displaymessages.getItems().size()-1);
			        		}
			        		else {
								displaymessages.setVisible(false);
			        		}
			        		if (list.getSelectionModel().getSelectedIndex() != -1) {
			        			list.getItems().set(list.getSelectionModel().getSelectedIndex(), list.getItems().get(list.getSelectionModel().getSelectedIndex()).replaceAll(" " + "\\(NEW MESSAGE\\)", ""));
			        		}
			        	} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
			        	timer = new Timer();
			        	TimerTask myNewTask = new TimerTask() {
						    @Override
						    public void run() {
						    	Platform.runLater(new Runnable() {
						    		public void run() {
								    	try {
											chatDB.getUserByID(0);
											if (chatDB.getIfUserIsConnected()) {
												System.out.println("CURRENTLY UPDATING MESSAGES...");
												int nbMessagesBeforeUpdate = chatDB.getNbMessages();
												chatDB.updateMessageListByUser(chatDB.getCurrentConnectedUser());
												if (chatDB.getNbMessages() > nbMessagesBeforeUpdate) {
													for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
														for (int j = 0; j < list.getItems().size(); j++) {
															if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
																if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
																	list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
																	Popup notif = new Popup();
																	Text mynotif = new Text("You have a new message from "+ chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
																	mynotif.getStyleClass().add("popup");
																	notif.getContent().add(mynotif);
																	notif.setOnShown(new EventHandler<WindowEvent>() {
																        @Override
																        public void handle(WindowEvent e) {
																            notif.setX(primaryStage.getX() + primaryStage.getWidth()/2 - notif.getWidth()/2);
																            notif.setY(primaryStage.getY() + primaryStage.getHeight()/2 - notif.getHeight()/2);
																        }
																    });        
																	notif.show(primaryStage);
																	PauseTransition delay = new PauseTransition(Duration.seconds(5));
																	delay.setOnFinished( event -> notif.hide() );
																	delay.play();
																}
																else {
																	displaymessages.scrollTo(displaymessages.getItems().size()-1);
																}
															}
														}
													}
													if (chatDB.getMessageReceiver() != null) {
														if (chatDB.getMessageObservableListFromUser(friendid) != null) {
															displaymessages.setVisible(true);
										        			displaymessages.setItems(chatDB.getMessageObservableListFromUser(friendid));
										        		}
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
			        	timer.schedule(myNewTask, 4000, 4000);
			        }
			    }
			});
			
		    typingmessage.setOnKeyPressed(new EventHandler<KeyEvent>()
		    {
		        @Override
		        public void handle(KeyEvent ke)
		        {
		            if (ke.getCode().equals(KeyCode.ENTER))
		            {
		                
		                sendbutton.requestFocus();
		            	sendbutton.fire();
		            }
		        }
		    });
		    
		    usernamesignin.setOnKeyPressed(new EventHandler<KeyEvent>()
		    {
		        @Override
		        public void handle(KeyEvent ke)
		        {
		            if (ke.getCode().equals(KeyCode.ENTER))
		            {
		            	signinbutton.requestFocus();
		            	signinbutton.fire();
		            }
		        }
		    });
		    
		    pwdsignin.setOnKeyPressed(new EventHandler<KeyEvent>()
		    {
		        @Override
		        public void handle(KeyEvent ke)
		        {
		            if (ke.getCode().equals(KeyCode.ENTER))
		            {
		            	signinbutton.requestFocus();
		            	signinbutton.fire();
		            }
		        }
		    });
		    
		    usernamesignup.setOnKeyPressed(new EventHandler<KeyEvent>()
		    {
		        @Override
		        public void handle(KeyEvent ke)
		        {
		            if (ke.getCode().equals(KeyCode.ENTER))
		            {
		            	signupbutton.requestFocus();
		            	signupbutton.fire();
		            }
		        }
		    });
		    
		    pwdsignup.setOnKeyPressed(new EventHandler<KeyEvent>()
		    {
		        @Override
		        public void handle(KeyEvent ke)
		        {
		            if (ke.getCode().equals(KeyCode.ENTER))
		            {
		            	signupbutton.requestFocus();
		            	signupbutton.fire();
		            }
		        }
		    });
		    
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
