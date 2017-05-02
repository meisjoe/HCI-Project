package com.application.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import static com.application.Main.Contacts_VIEW;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.application.Database;
import com.application.DrawerManager;
import com.application.Main;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class Chat extends View {

	public static Database dbconnection = DrawerManager.dbconnection;
	
	private static Timer timer;
	private int friendid = -1;
		
    public Chat(String name) {
        super(name);
        
        friendid = dbconnection.getFriendID();
        
        if (friendid == -1) {
        	MobileApplication.getInstance().switchView(Contacts_VIEW);
        }
        
        getStylesheets().add(Chat.class.getResource("secondary.css").toExternalForm());
        
        ListView<String> displaymessages = new ListView<>();
        
        Button sendmessage = new Button("Send");
        sendmessage.setPrefWidth(MobileApplication.getInstance().getScreenWidth() / .8);
        
        TextField message = new TextField();
        message.setText("Type your message...");
        message.setPrefWidth(MobileApplication.getInstance().getScreenWidth() / .2);
        
        HBox inputHBox = new HBox(message, sendmessage);
        inputHBox.setAlignment(Pos.BASELINE_CENTER);
        setBottom(inputHBox);
        
        this.setOnShown((e) -> {
        	for (int j = 0; j < dbconnection.getUserContactList().length; j++) {
        		if (friendid != dbconnection.getMessageReceiver().getUserID()) {
                	displaymessages.setItems(null);
                	friendid = dbconnection.getMessageReceiver().getUserID();
        		}
        	}
        	try {
				dbconnection.connectToDB();
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
            dbconnection.updateMessageListByUser(dbconnection.getCurrentConnectedUser());
        	
            if (dbconnection.getMessagesToDisplay().length > 0) {
    			displaymessages.setItems(dbconnection.getMessageObservableListFromUser(friendid));
        	}

			System.out.println("Updated Chat List");
        });
        
        message.setOnMouseClicked((e)-> {
			if(message.getText().contentEquals("Type your message...")){
				message.setText("");
			}
		});
        
        sendmessage.setOnMouseClicked((e) -> {
        	try {
				DateFormat myformat = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
				Date date = new Date();
				StringBuilder messageToString = new StringBuilder();
				String time = myformat.format(date);
				messageToString
					.append(dbconnection.getUserByID(dbconnection.getCurrentConnectedUser().getUserID()).getUsername())
					.append(" on ") 
					.append(time)
					.append(": ")
					.append(message.getText())
					.toString();
				dbconnection.sendMessage(dbconnection.getCurrentConnectedUser(), dbconnection.getMessageReceiver(), messageToString.toString(), time);
				dbconnection.updateMessageListByUser(dbconnection.getCurrentConnectedUser());
				if(!displaymessages.isVisible()){
					displaymessages.setItems(dbconnection.getMessageObservableListFromUser(friendid));
					displaymessages.setVisible(true);
				}
				displaymessages.setItems(dbconnection.getMessageObservableListFromUser(friendid));
				displaymessages.scrollTo(displaymessages.getItems().size()-1);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			message.setText("Type your message...");
			timer = new Timer();
        	TimerTask myNewNewTask = new TimerTask() {
			    @Override
			    public void run() {
			    	Platform.runLater(new Runnable() {
			    		public void run() {
					    	try {
					    		dbconnection.getUserByID(0);
								if (dbconnection.getIfUserIsConnected()) {
									System.out.println("CURRENTLY UPDATING MESSAGES...");
									int nbMessagesBeforeUpdate = dbconnection.getNbMessages();
									dbconnection.updateMessageListByUser(dbconnection.getCurrentConnectedUser());
									if (dbconnection.getNbMessages() > nbMessagesBeforeUpdate) {
//										for (int i = nbMessagesBeforeUpdate; i < chatDB.getNbMessages(); i++) {
//											for (int j = 0; j < list.getItems().size(); j++) {
//												if (list.getItems().get(j).equals(chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername())) {
//													if (chatDB.getMessageReceiver().getUserID() != chatDB.getMessagesToDisplay()[i].getSenderID()) {
//														list.getItems().set(j, list.getItems().get(j).concat(" (NEW MESSAGE)"));
//														Popup notif = new Popup();
//														Text mynotif = new Text("You have a new message from "+ chatDB.getUserByID(chatDB.getMessagesToDisplay()[i].getSenderID()).getUsername());
//														mynotif.getStyleClass().add("popup");
//														notif.getContent().add(mynotif);
//														notif.setOnShown(new EventHandler<WindowEvent>() {
//													        @Override
//													        public void handle(WindowEvent e) {
//													            notif.setX(primaryStage.getX() + primaryStage.getWidth()/2 - notif.getWidth()/2);
//													            notif.setY(primaryStage.getY() + primaryStage.getHeight()/2 - notif.getHeight()/2);
//													        }
//													    });        
//														notif.show(primaryStage);
//														PauseTransition delay = new PauseTransition(Duration.seconds(5));
//														delay.setOnFinished( event -> notif.hide() );
//														delay.play();
//													}
//													else {
//														displaymessages.scrollTo(displaymessages.getItems().size()-1);
//													}
//												}
//											}
//										}
										if (dbconnection.getMessageReceiver() != null) {
											if (dbconnection.getMessageObservableListFromUser(friendid) != null) {
												displaymessages.setVisible(true);
							        			displaymessages.setItems(dbconnection.getMessageObservableListFromUser(friendid));
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
        
        Timer clock = new Timer();
        TimerTask updateChat = new TimerTask() {
			@Override
			public void run() {
				try {
					dbconnection.connectToDB();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				friendid = dbconnection.getMessageReceiver().getUserID();
				dbconnection.updateMessageListByUser(dbconnection.getCurrentConnectedUser());
				
				if (dbconnection.getMessagesToDisplay().length > 0) {
					displaymessages.setItems(dbconnection.getMessageObservableListFromUser(friendid));
				}
				System.out.println("Updated Chat List");
			}
        };
        clock.schedule(updateChat, 4000, 4000);

        setCenter(displaymessages);
    	setShowTransitionFactory(BounceInRightTransition::new);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("BLAB - Chat [" + dbconnection.getMessageReceiver().getUsername() + "]");
    }
    
}
