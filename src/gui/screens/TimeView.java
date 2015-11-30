package gui.screens;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * @author Al-John
 */
public class TimeView extends Screen  {
    public TimeView(){

        Label label = new Label("Time View");
        label.setTranslateX(50);
        label.setTranslateY(100);
        getChildren().add(label);

        setStyle("-fx-background-color:green;");
    }

    public void populate() {

    }
}