package gui.controls;

import gui.GUI;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * @author Al-John
 */
public class TimelineBar extends Pane {

    private Label label;

    public TimelineBar(Pane parent) {
        label = new Label("(Timeline bar)");
        this.setPrefHeight(30);
        this.prefWidthProperty().bind(parent.widthProperty());
        this.setStyle("-fx-background-color:" + GUI.toRGBCode(Color.CADETBLUE));

        this.getChildren().add(label);
    }
}
