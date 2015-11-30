package gui.controls;

import data.Participant;
import gui.GUI;
import gui.screens.ScreenController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Al-John
 */
public class ParticipantKey extends Pane {

    private VBox container;

    public ParticipantKey(Pane parent) {

        container = new VBox();

        setMinWidth(20);
        setMinHeight(20);

        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(5));
        if(ScreenController.activeProject != null) {
            for (Participant p : ScreenController.activeProject.getParticipants()) {
                container.getChildren().add(new pLabel(p));
            }
        }

        this.getChildren().add(container);
        this.setTranslateX(50);
        this.translateYProperty().bind(parent.heightProperty().subtract(50).subtract(this.heightProperty()));
        setStyle("-fx-background-color:black");
    }

    private class pLabel extends HBox {
        private Rectangle color;
        private Label name;

        public pLabel(Participant p) {
            super(5);
            setAlignment(Pos.CENTER_LEFT);

            color = new Rectangle(10, 10, p.getColor());
            name = new Label(p.getName() + " (" + p.getRole() + ")");

            name.setTextFill(Color.WHITE);

            getChildren().addAll(color, name);
        }
    }


}
