package com.application;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import static com.application.Main.MENU_LAYER;
import static com.application.Main.Login_VIEW;
import static com.application.Main.Chat_VIEW;
import static com.application.Main.Contacts_VIEW;

import javafx.scene.Node;

public class DrawerManager {

    private final NavigationDrawer drawer;
    public static Database dbconnection = new Database("jdbc:mysql://31.220.20.83:3306/u433658471_hcipj?useUnicode=true&characterEncoding=UTF-8&connectTimeout=10000&socketTimeout=10000", "u433658471_hugo", "Hm211195");
    public static Item contactsMenuItem;
    public static Item chatMenuItem;
    public static Item logOutMenuItem;
    
    /*
     *  Top left menubar to switch between contacts and chat page.
     */
    public DrawerManager() {
        this.drawer = new NavigationDrawer();
        
        try{
        	dbconnection.connectToDB();
        } catch(Exception e) {
    		e.printStackTrace();
    	}
        
        contactsMenuItem = new ViewItem("Contacts", MaterialDesignIcon.CONTACTS.graphic(), Contacts_VIEW, ViewStackPolicy.SKIP);
        chatMenuItem = new ViewItem("Chat", MaterialDesignIcon.INBOX.graphic(), Chat_VIEW);
        logOutMenuItem = new ViewItem("Logout", MaterialDesignIcon.EXIT_TO_APP.graphic(), Login_VIEW);
                
        drawer.getItems().addAll(contactsMenuItem, chatMenuItem, logOutMenuItem);
        
        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.CLOSE.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
        
        drawer.addEventHandler(NavigationDrawer.ITEM_SELECTED, 
                e -> MobileApplication.getInstance().hideLayer(MENU_LAYER));
        
        MobileApplication.getInstance().viewProperty().addListener((obs, oldView, newView) -> updateItem(newView.getName()));
        updateItem(Login_VIEW);
    }
    
    private void updateItem(String nameView) {
        for (Node item : drawer.getItems()) {
            if (item instanceof ViewItem && ((ViewItem) item).getViewName().equals(nameView)) {
                drawer.setSelectedItem(item);
                break;
            }
        }
    }
    
    public NavigationDrawer getDrawer() {
        return drawer;
    }
}
