package application;

import gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry class for PlanAmity.
 * @author Al-John
 *
 */
public class Main extends Application {

    private static Application instance;

	@Override
	public void start(Stage primaryStage) {

        instance = this;
        GUI.init(primaryStage);
        GUI.start();

    }

    public static Application instance() {
        return instance;
    }

	public static void main(String[] args) {
        launch(args);
	}
}
