package gui.controls.overview;


import data.Entry;
import gui.GUI;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller used to add a new Entry to the project.
 * //TODO: FILL THIS IN!
 * @author Al-John
 */
public class EntryPopup extends Pane {

    private Label name;
    private Label startDate;
    private Label endDate;

    private VBox container;
    private Button close;

    public EntryPopup(Entry entry) {
        name = new Label(entry.getName());
        startDate = new Label("Start date: " + entry.getStart().toString());
        endDate = new Label("End date: " + entry.getEnd().toString());
        container = new VBox(5);
        close = new Button("Close");

        this.setStyle("-fx-background-color:violet");

        container.setPadding(new Insets(10));
        container.getChildren().addAll(name, startDate, endDate, close);
        getChildren().add(container);

        close.setOnMouseClicked(MouseEvent -> die());
    }

    public void spawn() {
        GUI.zoomFade(this, true, 200).play();
    }

    public void die() {
        Timeline death = GUI.zoomFade(this, false);
        death.setOnFinished(ActionEvent -> ((Pane)getParent()).getChildren().remove(this));
        death.play();
    }

}