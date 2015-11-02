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
		
		Entry e = new Entry("E");
		Entry f = new Entry("F", e);
		Entry g = new Entry("G", e);
		Entry h = new Entry("H", e);
		Entry i = new Entry("I", e);
		Entry j = new Entry("J", f);
		Entry k = new Entry("K", h);
		Entry l = new Entry("L", f);
		
		User a = new User("Green", Color.GREEN, "");
		User b = new User("Blue", Color.BLUE, "");
		User c = new User("Orange", Color.ORANGE, "");
		
		e.assignTo(a);
		f.assignTo(b);
		g.assignTo(c);
		h.assignTo(a);
		i.assignTo(a);
		j.assignTo(c);
		k.assignTo(c);
		l.assignTo(b);
		
//		GUI gui = new GUI();
//		root.getChildren().add(gui.createNode(e, 0));
		
		root.getChildren().add(new ProjectView());

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
