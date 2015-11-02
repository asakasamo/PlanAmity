package application;

import gui.GUI;
import gui.screens.ProjectView;
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
		ProjectView pv = new ProjectView(null);
		root.getChildren().add(pv);
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		
		//GUI.makeDraggable(root);
		
		primaryStage.show();
		
		final Timeline timeline = new Timeline();
		final KeyValue kv = new KeyValue(pv.translateXProperty(), -1000, Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(2000), kv);
		timeline.getKeyFrames().add(kf);
		timeline.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				//scene.getChildren().remove(root);
				root.getChildren().set(0, new ProjectView(null));
			}
		});
		timeline.play();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
