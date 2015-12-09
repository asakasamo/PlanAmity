package gui.controls.overview;

import gui.GUI;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * @author Al-John
 */
public class TimelineBar extends Pane {

    public final double HEIGHT = 36;

    private Label label;

    public TimelineBar(Pane parent) {
        GUI.setHeight(this, HEIGHT);

        label = new Label("(Timeline bar)");
        this.prefWidthProperty().bind(parent.widthProperty());
        this.setStyle("-fx-background-color:cadetblue");

        this.getChildren().add(label);
    }
}
