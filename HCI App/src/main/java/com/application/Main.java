package com.application;

import com.application.views.Login;
import com.application.views.Chat;
import com.application.views.Contacts;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends MobileApplication {

    public static final String Login_VIEW = HOME_VIEW;
    public static final String Chat_VIEW = "Chat";
    public static final String Contacts_VIEW = "Contacts";
    public static final String MENU_LAYER = "Side Menu";
    
    @Override
    public void init() {
        addViewFactory(Login_VIEW, () -> new Login(Login_VIEW));
        addViewFactory(Contacts_VIEW, () -> new Contacts(Contacts_VIEW));
        addViewFactory(Chat_VIEW, () -> new Chat(Chat_VIEW));
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(new DrawerManager().getDrawer()));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/addcontactsymb.png")));
    }
}
