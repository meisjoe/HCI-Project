package com.application.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import static com.application.Main.Contacts_VIEW;

import java.sql.SQLException;

import com.application.Database;
import com.application.DrawerManager;
import com.application.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AddContact extends View {

	public static Database dbconnection = DrawerManager.dbconnection;
	
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
			list.setItems(friendlist);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
    public AddContact(String name) {
        super(name);
        
        getStylesheets().add(AddContact.class.getResource("secondary.css").toExternalForm());
        
        ListView<String> list = new ListView<String>();
        updateContactUsernames(list);        
        
        Text addindic = new Text ("Type the username of the contact you want to add: ");
        addindic.maxWidth(MobileApplication.getInstance().getScreenWidth() /.8);
        addindic.setTextAlignment(TextAlignment.CENTER);
        TextField contacttoadd = new TextField();
        contacttoadd.setAlignment(Pos.CENTER);
        contacttoadd.setMaxWidth(MobileApplication.getInstance().getScreenWidth() /.8);
        
        Button addbutton = new Button("Add");
        Button cancelbutton = new Button("Cancel");
        
        Text doesnotexist = new Text ("This user does not exist or he is already in your Contact List");
		doesnotexist.setVisible(false);
		doesnotexist.setTextAlignment(TextAlignment.CENTER);
		doesnotexist.maxWidth(MobileApplication.getInstance().getScreenWidth() /.8);
        
        addbutton.setOnMouseClicked((e)-> {
			boolean firstfound = false;
			boolean secondfound = false;
			int i = 0;
			int j = 0;
			int idtoadd = 0;
			while(i != dbconnection.getUsersList().length){
				if((dbconnection.getUsersList())[i].getUsername().equals(contacttoadd.getText())) {
					idtoadd = (dbconnection.getUsersList())[i].getUserID();
					firstfound = true;
					break;
				}
				i++;
			}
			while(j != dbconnection.getUserContactList().length){
				if((dbconnection.getUserContactList())[j].getUsername().equals(contacttoadd.getText())) {
					secondfound = true;
					break;
				}
				j++;
			}
			if(firstfound && (!secondfound)){
				try {
					System.out.println("ID: " + idtoadd);
					dbconnection.addContact(dbconnection.getCurrentConnectedUser(), idtoadd);
					doesnotexist.setVisible(false);
					dbconnection.updateContactString(dbconnection.getCurrentConnectedUser());
					updateContactUsernames(list);
					
					MobileApplication.getInstance().switchView(Contacts_VIEW);
				} catch (ClassNotFoundException | SQLException e2) {
					e2.printStackTrace();
				}
			}
			else {
				doesnotexist.setVisible(true);
			}
		});
        
        
        cancelbutton.setOnMouseClicked((e)->{
			contacttoadd.clear();
			doesnotexist.setVisible(false);
			MobileApplication.getInstance().switchView(Contacts_VIEW);
        });
        
             
        HBox hbox = new HBox(addbutton, cancelbutton);
        VBox vbox = new VBox(addindic, contacttoadd, hbox, doesnotexist);
        hbox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        hbox.spacingProperty().add(USE_PREF_SIZE);
        vbox.spacingProperty().add(USE_PREF_SIZE);
        
        setCenter(vbox);
        
        setShowTransitionFactory(BounceInRightTransition::new);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("BLAB - Add Contact");
    }
    
}
