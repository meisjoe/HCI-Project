package com.application.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import static com.application.Main.ADDContact_VIEW;
import static com.application.Main.Chat_VIEW;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import com.application.Database;
import com.application.DrawerManager;
import com.application.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class Contacts extends View {
	
	public static Database dbconnection = DrawerManager.dbconnection;

	private Timer timer = new Timer();
	private int friendid = -1;
	
	public void updateContactUsernames(ListView<String> list){
		try {
			dbconnection.connectToDB();
			String usernames[] = new String[dbconnection.getUserContactList().length];
			for(int j = 0; j < dbconnection.getUserContactList().length; j++) {
				usernames[j] = (dbconnection.getUserContactList()[j]).getUsername();
				if(dbconnection.getUserContactList()[j].getUsername().equals(dbconnection.getCurrentConnectedUser().getUsername())){
					usernames[j] = usernames[j] + " (MYSELF)";
				}
			}
			
			ObservableList<String> friendlist = FXCollections.observableArrayList (usernames);
			
			if (list.getItems().size() == friendlist.size())
				list.refresh();
			else
				list.setItems(friendlist);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public Contacts(String name) {
        super(name);
        
        getStylesheets().add(Contacts.class.getResource("primary.css").toExternalForm());
		
		Button addcontact = new Button();
		addcontact.setGraphic(MaterialDesignIcon.PERSON_ADD.graphic());
		addcontact.setPrefWidth(MobileApplication.getInstance().getScreenWidth() / 2);
		
		Button removecontact = new Button();
		removecontact.setGraphic(MaterialDesignIcon.DELETE.graphic());
		removecontact.setPrefWidth(MobileApplication.getInstance().getScreenWidth() / 2);
		
		HBox controls = new HBox(addcontact, removecontact);
        controls.setAlignment(Pos.BASELINE_CENTER);        

        ListView<String> list = new ListView<String>();
        updateContactUsernames(list);
        setCenter(list);
        
        addcontact.setOnMouseClicked((e)-> {				 			    
        	MobileApplication.getInstance().switchView(ADDContact_VIEW);
		});
        
        removecontact.setOnMouseClicked((e) -> {
        	int j = 0;
			int idtoremove = -1;
			while(j < dbconnection.getUserContactList().length){
	        	if((dbconnection.getUserContactList()[j]).getUsername() == list.getSelectionModel().getSelectedItem()) {
	        		idtoremove = (dbconnection.getUserContactList()[j]).getUserID();
	        		break;
	        	}
		        j++;
			}
			try {
				dbconnection.removeContact(dbconnection.getCurrentConnectedUser(), idtoremove);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			try {
				dbconnection.updateContactString(dbconnection.getCurrentConnectedUser());
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			
			updateContactUsernames(list);
        });
                
        list.setOnMouseClicked((e) -> {
        	if (e.getClickCount() > 1) {
        		
        		int currentid = -1;
        		currentid = dbconnection.getCurrentConnectedUser().getUserID();
        		int j = 0;
        		
        		while(j < dbconnection.getUserContactList().length) {
		        	if((dbconnection.getUserContactList()[j]).getUsername().equals(list.getSelectionModel().getSelectedItem())) {
	        			friendid = (dbconnection.getUserContactList()[j]).getUserID();
	        			dbconnection.getUserContactList()[j].getUsername();
	        			dbconnection.setFriendID(friendid);
	        			dbconnection.setMessageReceiver(dbconnection.getUserContactList()[j]);
	        			break;
		        	}
		        	j++;
		        }
        		
    			dbconnection.setMessageReceiver(dbconnection.getUserContactList()[j]);
        		
        		if((currentid != -1) && (friendid != -1)) {
		        	try {
		        		dbconnection.setMessageReceiver(dbconnection.getUserByID(friendid));
		        		dbconnection.setUserIsConnected(true);
		        		dbconnection.updateMessages(dbconnection.getCurrentConnectedUser());
		        		if (list.getSelectionModel().getSelectedIndex() != -1) {
		        			list.getItems().set(list.getSelectionModel().getSelectedIndex(), list.getItems().get(list.getSelectionModel().getSelectedIndex()).replaceAll(" " + "\\(NEW MESSAGE\\)", ""));
		        		}
		        		MobileApplication.getInstance().switchView(Chat_VIEW);
		        		
		        	} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
        		}
        	}
        });
        
        TimerTask updateContacts = new TimerTask() {
			@Override
			public void run() {
				updateContactUsernames(list);
				System.out.println("Updated Contact List");
			}
        };
        
        timer.schedule(updateContacts, 4000, 4000);
        
        setBottom(controls);
    }
    
    public int getfriendid() {
    	return this.friendid;
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("BLAB - Contacts");
    }
    
}
