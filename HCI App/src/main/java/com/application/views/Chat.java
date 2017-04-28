package com.application.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.application.Database;
import com.application.DrawerManager;
import com.application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Chat extends View {

	public static Database dbconnection = DrawerManager.dbconnection;
	
    public Chat(String name) {
        super(name);
        
        getStylesheets().add(Chat.class.getResource("secondary.css").toExternalForm());
        
        Label label = new Label("This is Secondary!");

        VBox controls = new VBox(label);
        controls.setAlignment(Pos.CENTER);
        
        setCenter(controls);
        
        setShowTransitionFactory(BounceInRightTransition::new);
        
        getLayers().add(new FloatingActionButton(MaterialDesignIcon.INFO.text, 
            e -> System.out.println("Info")).getLayer());
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("Messenger - Chat");
    }
    
}
