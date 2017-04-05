package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.sql.*;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Main extends Application {
		
	@Override
	public void start(Stage primaryStage) {
		try {
			
//			//construct Model
//			Model model = new Model();
//					
//			//construct Controller
//			Controller c = new Controller( model );
//			
//			//construct View
//			View v = new View( model, c, primaryStage );
			
			Database chatDB = new Database("jdbc:mysql://31.220.20.83:3306/u433658471_hcipj?useUnicode=true&characterEncoding=UTF-8", "u433658471_hugo", "Hm211195");
			
			User testUser = chatDB.createUser("Sender", "ITextYou");
			User testUser2 = chatDB.createUser("Receiver", "textMe");
			Room testRoom = chatDB.createRoom("testRoom");
			
			chatDB.connectUser(testUser, testRoom);
			chatDB.connectUser(testUser2, testRoom);
			chatDB.sendMessage(testUser, testUser2, testRoom, "I see you!", "04052017000823");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
