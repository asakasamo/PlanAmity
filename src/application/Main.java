package application;

import data.Entry;
import data.User;
import gui.GUI;
import gui.ProjectView;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//PlanAmity

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		Scene scene = new Scene(root,GUI.WINDOW_WIDTH,GUI.WINDOW_HEIGHT);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		//Ideally, add the main menu here.
		root.getChildren().add(new ProjectView());

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
