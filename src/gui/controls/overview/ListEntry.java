package gui.controls.overview;

import data.DateTime;
import data.Entry;
import gui.GUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Graphical representation of any sub-Entry.
 */
public class ListEntry extends EntryCell {

    public static final double HEIGHT = 16.8;
    private final double INDENT = 10;

    private TextField nameInput;
    private Label nameLabel;
    private ImageView attachments;

    private int indent;

    public ListEntry(EntryBar entryBar, EntryCell parent, EntryCell... insertAfter){
        this.parent = parent;
        this.entryBar = entryBar;
        subEntries = new ArrayList<>();
        nameLabel = new Label();
        indent = parent instanceof ListEntry ? ((ListEntry) parent).indent + 1 : 0;

        if(insertAfter.length == 0) parent.addSubEntry(this);
        else parent.addSubEntry(this, insertAfter);

        this.getStyleClass().add("listEntry");

        GUI.setHeight(this, HEIGHT);
//        this.setStyle("-fx-background-color:blue");

        nameInput = new TextField();
        nameInput.setPromptText("Entry Name");
        setTranslateX(indent * INDENT);

        getChildren().add(nameInput);
        Platform.runLater(() -> nameInput.requestFocus());
        setButtonFunctions();
    }

    public ListEntry(Entry e, EntryCell parent, EntryBar bar) {
        this(bar, parent);
        nameInput.setText(e.getName());
        entry = e;

//        System.out.println("GENERATED: " + this);
        finish(true);
    }

    public void setButtonFunctions() {

        this.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton() == MouseButton.SECONDARY)
                entryBar.openPopup(entry);
        });

        setOnMouseMoved(MouseEvent -> setCursor(Cursor.HAND));

        setOnMousePressed((MouseEvent event) -> {
            setCursor(Cursor.CLOSED_HAND);
            entryBar.setFocus(this);
        });

        setOnMouseReleased((MouseEvent event) -> {
            setCursor(Cursor.HAND);
        });

        this.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.TAB) {
                event.consume();
            }
            if(event.getCode() == KeyCode.ENTER){
                if(!nameInput.getText().isEmpty())
                    finish(true);
            }
            if(event.getCode() == KeyCode.ESCAPE){
                finish(false);
            }
        });

    }

    private void finish(boolean keep) {
        if(keep) {
            nameLabel.setText(nameInput.getText());
            nameLabel.setAlignment(Pos.CENTER_LEFT);
//            initTextBounds(nameLabel);

            getChildren().set(getChildren().indexOf(nameInput), nameLabel);

            if(entry == null)
                entry = new Entry(nameInput.getText(), new DateTime(), new DateTime());

        } else {

        }

        entryBar.newEntryFinish(this, keep);
    }

    public int getIndent() {
        return indent;
    }

    @Override
    public void expand() {
        entryBar.getEntryArea().expand(this);
    }

    @Override
    public void retract() {
        entryBar.getEntryArea().retract(this);
    }

    @Override
    public String toString() {
        String s = "";
        //for(int i = indent; i>= 0; i--) s += "\t";
        return s + "ListEntry :: " + super.toString();
    }

}