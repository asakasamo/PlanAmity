package gui.controls.overview;


import data.DateTime;
import data.Entry;
import gui.GUI;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.event.DocumentEvent;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Controller used to add a new Entry to the project.
 * //TODO: FILL THIS IN!
 * @author Al-John
 */
public class EntryPopup extends Pane {

    private Label name;
    private DatePicker startDate;
    private DatePicker endDate;

    private VBox container;
    private Button close;
    private Entry entry;

    public EntryPopup(Entry entry) {
        this.entry = entry;
        name = new Label(entry.getName());
        startDate = new DatePicker(entry.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        endDate = new DatePicker(entry.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        container = new VBox(5);
        close = new Button("Close");

        this.setStyle("-fx-background-color:violet");

        container.setPadding(new Insets(10));
        container.getChildren().addAll(name, startDate, endDate, close);
        getChildren().add(container);

        close.setOnMouseClicked(MouseEvent -> die());

        startDate.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            entry.setStart(new DateTime(newValue), false);
        });

        endDate.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            entry.setEnd(new DateTime(newValue), false);
        });
    }

    public void spawn() {
        GUI.zoomFade(this, true).play();
    }

    public void die() {
        Timeline death = GUI.zoomFade(this, false);
        death.setOnFinished(ActionEvent -> ((Pane)getParent()).getChildren().remove(this));

        death.play();
    }

}