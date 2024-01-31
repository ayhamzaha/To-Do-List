package application;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.image.Image;


public class Main extends Application {

	Stage mainStage;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage){
		try {
		mainStage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/Main.FXML"));
		Scene scene = new Scene(root);
		
		Image icon = new Image("ic.png");
		
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.getIcons().add(icon);
		mainStage.setTitle("To-Do List");		
		mainStage.show();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
