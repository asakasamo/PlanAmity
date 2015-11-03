package application;

import gui.GUI;
import gui.screens.ProjectView;
import gui.screens.StartupMenu;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Main entry class for PlanAmity.
 * @author Al-John
 *
 */
public class Main extends Application {
	private StackPane root;
	
	@Override
	public void start(Stage primaryStage) {
		root = new StackPane();
		Scene scene = new Scene(root,GUI.WINDOW_WIDTH,GUI.WINDOW_HEIGHT);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		//Ideally, add the main menu here.
        StartupMenu menu = new StartupMenu();
		ProjectView pv = new ProjectView(null);
		root.getChildren().add(menu);
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		
		//GUI.makeDraggable(root);
		
		primaryStage.show();
		
//		final Timeline timeline = new Timeline();
//		final KeyValue kv = new KeyValue(pv.translateXProperty(), -2000, Interpolator.EASE_BOTH);
//		final KeyFrame kf = new KeyFrame(Duration.millis(2000), kv);
//		timeline.getKeyFrames().add(kf);
//
//		timeline.setOnFinished((ActionEvent event) -> root.getChildren().set(0, new ProjectView(null)));
//
//		timeline.play();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
