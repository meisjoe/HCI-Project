package com.application.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import java.sql.SQLException;

import com.application.Database;
import com.application.DrawerManager;
import static com.application.Main.Contacts_VIEW;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Login extends View {

	public static Database dbconnection = DrawerManager.dbconnection;
	
    public Login(String name) {
        super(name);
        
        getStylesheets().add(Login.class.getResource("primary.css").toExternalForm());

        Text userText = new Text("Username: ");
        TextField usernamesignin = new TextField();
        Text pwdText = new Text("Password: ");
        PasswordField pwdsignin = new PasswordField();
        Button signinbutton = new Button("Sign In");
        Button registerbutton = new Button("Register");
        
        //Result when an bad info is given
        Text incorrectpwd = new Text ("The combination username/password is incorrect");
		incorrectpwd.setVisible(false);
		Text errregister = new Text();
		
		this.setOnShown((e) -> {
			usernamesignin.clear();
			pwdsignin.clear();
		});
		
        signinbutton.setOnAction((e)-> {

			try {
				dbconnection.connectToDB();
				dbconnection.updateUsersList();
				System.out.println("Connected");
			} catch (ClassNotFoundException e1) {
				System.out.println("Class Error");
				e1.printStackTrace();
			} catch (SQLException e1) {
				System.out.println("SQL Error");
				e1.printStackTrace();
			}
			
			boolean found = false;
			int i = 0;
			while(i != dbconnection.getUsersList().length){
				//System.out.println(usernamesignin.getText().equals((dbconnection.getUsersList())[i].getUsername()));
				if((usernamesignin.getText().equals((dbconnection.getUsersList())[i].getUsername())) && (pwdsignin.getText().equals((dbconnection.getUsersList())[i].getPassword()))) {
					dbconnection.setCurrentConnectedUser((dbconnection.getUsersList())[i]);
					found = true;
					break;
				}
				i++;
			}
			
			if(found){
				try {
					dbconnection.connectUser(dbconnection.getCurrentConnectedUser());
					dbconnection.updateContactString(dbconnection.getCurrentConnectedUser());
					dbconnection.updateMessages(dbconnection.getCurrentConnectedUser());
					dbconnection.createMessageListByUser(dbconnection.getCurrentConnectedUser());
					
		        	MobileApplication.getInstance().switchView(Contacts_VIEW);
		        	
		        	errregister.setText("");
					incorrectpwd.setVisible(false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			else{
				incorrectpwd.setVisible(true);
			}
			errregister.setText("");
		});
        
        registerbutton.setOnAction((e)-> {
			try {
				dbconnection.connectToDB();
				dbconnection.updateUsersList();
				if(usernamesignin.getText().equals("")){
					errregister.setText("Please enter a valid username");
				}
				else if(pwdsignin.getText().length()<6){
					errregister.setText("The password must contain at least 6 characters");
				}
				else {
					int i = 0;
					boolean exists = false;
					while (i != dbconnection.getUsersList().length) {
						if((dbconnection.getUsersList())[i].getUsername().equals(usernamesignin.getText())){
							exists = true;
							break;
						}
						i++;
					}
					if(!exists) {
						try {
							dbconnection.createUser(usernamesignin.getText(), pwdsignin.getText());
							errregister.setText("The user has been created. You can now sign in.");
							usernamesignin.clear();
							pwdsignin.clear();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					else{
						errregister.setText("This username already exists");
					}
				}
			} catch (ClassNotFoundException | SQLException e2) {
				e2.printStackTrace();
			}
			
			incorrectpwd.setVisible(false);
		});

        HBox userHBox = new HBox(20.0, userText, usernamesignin);
        HBox pwdHBox = new HBox(20.0, pwdText, pwdsignin);
        HBox buttonHBox = new HBox(20.0, signinbutton, registerbutton);
        VBox input = new VBox(15.0, userHBox, pwdHBox, buttonHBox, incorrectpwd, errregister);
        userHBox.setAlignment(Pos.CENTER);
        pwdHBox.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);
        input.setAlignment(Pos.CENTER);
        
        setCenter(input);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        //appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
        appBar.setTitleText("BLAB - Login");
    }
    
}
