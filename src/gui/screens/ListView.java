package gui.screens;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * @author Al-John
 */
public class ListView extends Screen  {
    public ListView(){

        Label label = new Label("List View");
        label.setTranslateX(50);
        label.setTranslateY(100);
        getChildren().add(label);

        setStyle("-fx-background-color:orange;");
    }

    public void populate() {

    }
}