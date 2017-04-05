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
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();	
			
			//Connection to the database
			String connectionURL = "jdbc:mysql://31.220.20.83:3306/u433658471_hcipj?useUnicode=true&characterEncoding=UTF-8";
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection con = DriverManager.getConnection(connectionURL, "u433658471_hugo", "Hm211195");
			System.out.println(con);
			Statement stmt = con.createStatement();
			//ResultSet rs = stmt.executeQuery("SELECT Username FROM User");
			//Performing INSERT SQL command on the DB
			stmt.executeUpdate("INSERT INTO User (Username, Password) VALUES ('Hugo', 'ilovemydog');");
			//rs.next();
			//System.out.println(rs.getString("Username"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
