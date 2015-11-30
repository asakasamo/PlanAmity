package gui.controls;


import data.DateTime;
import data.Entry;
import gui.GUI;
import gui.screens.Overview;
import gui.screens.ScreenController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller used to add a new Entry to the project.
 * @author Al-John
 */
public class NewEntryPopup extends Pane {

    private TextField name;
    private DatePicker start;
    private DatePicker end;
    private Button done;
    private VBox container;
    private Overview overview;
    private boolean level;

    public NewEntryPopup(Overview ov) {
        name = new TextField();
        start = new DatePicker();
        end = new DatePicker();
        container = new VBox(5);
        done = new Button("Done");
        overview = ov;
        level = true;

        this.setTranslateX(200);
        this.setTranslateY(200);

        container.setPadding(new Insets(5));
        name.setPromptText("Entry Title");
        start.setPromptText("Start Date");
        end.setPromptText("End Date");

        this.setStyle("-fx-background-color:limegreen");
        container.getChildren().addAll(name, start, end, done);
        this.getChildren().add(container);

        this.parentProperty().addListener(observable -> spawn());
        this.done.setOnMouseClicked(MouseEvent -> die());
        GUI.makeDraggable(this);
    }

    public void spawn() {
        GUI.zoomFade(this, true, 200).play();
        name.setText("");
        start.setValue(null);
        end.setValue(null);
    }

    //TODO: actually remove this from parent
    public void die() {
        //create entry from fields
        Entry e = new Entry(name.getText(), new DateTime(start.getValue()), new DateTime(end.getValue()));

        EntryCell focus = ScreenController.focus;
        if(focus != null) {
            if(level) {
                e.setParent(focus.getEntry().getParent());
                if(focus.getEntry().getParent() != null) focus.getEntry().getParent().getChildren().add(e); //TODO: .ADDCHILDREN PLS
            } else {
                e.setParent(focus.getEntry());
                focus.getEntry().getChildren().add(e);
            }
        }

        overview.createNewEntry(e);
    }

    public void setLevel(boolean level) {
        this.level = level;
    }
}