package gui.controls.overview;

import data.DateTime;
import data.Entry;
import gui.GUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * The ListEntry class is the graphical representation of any sub-Entry in a Project. (These are shown as a "list" in
 * the EntryArea, hence the name "ListEntry".)
 */
public class ListEntry extends EntryCell {

    public static final double HEIGHT = 17;
    private final double INDENT = 10;

    private TextField nameText;
    private ImageView attachments;
    private boolean popupOpen;
    private boolean moving;

    private int indent;

    /**
     * Creates a new ListEntry, allowing user input for the creation of a new Entry.
     * @param entryBar the EntryBar this ListEntry inhabits
     * @param parent the parent EntryCell
     */
    public ListEntry(EntryBar entryBar, EntryCell parent){
        moving = false;
        this.parent = parent;
        this.entryBar = entryBar;
        subEntries = new ArrayList<>();
        popupOpen = false;
        indent = parent instanceof ListEntry ? ((ListEntry) parent).indent + 1 : 0;

        this.getStyleClass().add("listEntry");

        GUI.setHeight(this, HEIGHT);
//        this.setStyle("-fx-background-color:blue");

        nameText = new TextField();
        nameText.setPromptText("Entry Name");
        nameText.setAlignment(Pos.CENTER_LEFT);

        setTranslateX(indent * INDENT);

//        Label l = new Label("Entry1.2");
//        l.setStyle("-fx-background-color:red");
//        getChildren().add(l);
        getChildren().add(nameText);

        focusMe();
        setButtonFunctions();
    }

    /**
     * Creates a ListEntry for a specified Entry, skipping the user input phase of its creation.
     * @param entry the Entry
     * @param parent the parent EntryCell
     * @param bar the containing EntryBar
     */
    public ListEntry(Entry entry, EntryCell parent, EntryBar bar) {
        this(bar, parent);
        this.entry = entry;

        nameText.setText(entry.getName());
        nameText.setDisable(true);
    }

    public void setButtonFunctions() {

        this.addEventFilter(MouseEvent.ANY, (MouseEvent event) -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                if(!popupOpen) {
                    entryBar.openPopup(entry);
                    popupOpen = true;
                }
                event.consume();
            }
        });

        this.setOnMouseClicked((MouseEvent event) -> {

        });

        setOnMouseMoved(MouseEvent -> setCursor(Cursor.HAND));

        setOnMousePressed((MouseEvent event) -> {
            setCursor(Cursor.CLOSED_HAND);
            if(event.isAltDown())
                expandAll();
            else
                entryBar.setFocus(this);

//            this.setStyle("-fx-background-color:green");
        });

        setOnMouseReleased((MouseEvent event) -> {
            setCursor(Cursor.HAND);
            if(!moving)
                toggle();
            else{
                moving = false;
                entryBar.setMoving(null);
            }
        });

        this.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.TAB) {
                event.consume();
            }
            if(event.getCode() == KeyCode.ENTER){
                if(!nameText.getText().isEmpty())
                    finish();
            }
            if(event.getCode() == KeyCode.ESCAPE){
                die();
            }
        });

    }

    public void focusMe() {
        Platform.runLater(() -> nameText.requestFocus());
    }

    private void finish() {
        nameText.setDisable(true);
        entry = new Entry(nameText.getText(), new DateTime(), new DateTime());
        entryBar.newEntryFinish();
    }

    public int getIndent() {
        return indent;
    }

    @Override
    public void die() {
        entryBar.delete(this);
        entryBar.newEntryCancel();
    }

    @Override
    public void expand() {
        entryBar.getEntryArea().expand(this);
        expanded = true;
    }

    public void expandAll() {
        expand();
        subEntries.forEach(ListEntry::expand);
    }

    @Override
    public void retract() {
        entryBar.getEntryArea().retract(this);
        expanded = false;
    }

    @Override
    public String toString() {
        String s = "";
        //for(int i = indent; i>= 0; i--) s += "\t";
        return s + "ListEntry :: " + super.toString();
    }

    public String getEntryName() {
        return nameText.getText();
    }
}