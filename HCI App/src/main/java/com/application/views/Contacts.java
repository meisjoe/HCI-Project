package com.application.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.application.Database;
import com.application.DrawerManager;
import com.application.Main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Contacts extends View {

	public static Database dbconnection = DrawerManager.dbconnection;
	
    public Contacts(String name) {
        super(name);
        
        getStylesheets().add(Contacts.class.getResource("primary.css").toExternalForm());

        ListView<String> list = new ListView<String>();
		
		Button addcontact = new Button();
		addcontact.setGraphic(MaterialDesignIcon.IMPORT_CONTACTS.graphic());

		Button removecontact = new Button();
		removecontact.setGraphic(MaterialDesignIcon.DELETE.graphic());

		
		HBox controls = new HBox(addcontact, removecontact);
        VBox content = new VBox(controls);
        controls.setAlignment(Pos.BASELINE_CENTER);
        //content.setAlignment(Pos.CENTER);
        
        setCenter(content);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("Messenger - Contacts");
    }
    
}
