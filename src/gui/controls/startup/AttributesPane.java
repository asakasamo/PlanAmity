package gui.controls.startup;

import gui.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author Al-John
 */
public class AttributesPane extends Pane {
    VBox topContainer;
    HBox nameRow;
    HBox startRow;
    HBox endRow;

    Label name;
    Label start;
    Label end;

    public TextField nameInput;
    public DatePicker startInput;
    public DatePicker endInput;

    public AttributesPane() {

        name = new Label("Project Name:");
        start = new Label("Start Date:");
        end = new Label("End Date (Ideally):");
        alignRight(name, start, end);

        nameInput = new TextField();
        startInput = new DatePicker();
        endInput = new DatePicker();

        nameRow = new row(5);
        startRow = new row(5);
        endRow = new row(5);
        topContainer = new VBox(5);

//        this.setStyle("-fx-background-color:orange");

        nameRow.getChildren().addAll(name, nameInput);
        startRow.getChildren().addAll(start, startInput);
        endRow.getChildren().addAll(end, endInput);
        topContainer.getChildren().addAll(nameRow, startRow, endRow);

        getChildren().add(topContainer);
    }

    private class row extends HBox {
        row(double s) {
            super(s);
            GUI.setHeight(this, 40);
            this.setAlignment(Pos.CENTER_LEFT);
        }
    }

    private void alignRight(Label... labels){
        for(Label l : labels) {
            l.setAlignment(Pos.CENTER_RIGHT);
            GUI.setWidth(l, 200);
        }
    }
}