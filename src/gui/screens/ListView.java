package gui.screens;

import javafx.scene.control.Label;

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