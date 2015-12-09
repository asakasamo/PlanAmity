package application;

import data.Project;
import gui.GUI;
import gui.screens.ScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * Main entry class for PlanAmity.
 * @author Al-John
 *
 */
public class Main extends Application {

    private static Application instance;

	@Override
	public void start(Stage primaryStage) {

//        List l = Project.helper(10);
//        System.out.println(l);

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
