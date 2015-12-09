package gui.controls.overview;

import data.DateTime;
import data.Entry;
import gui.GUI;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * The EntryBubble class is a GUI wrapper that represents a top level Entry in the interface. It
 * modifies itself visually based on the attributes of the Entry assigned to it.
 * 
 * @author Al-John
 *
 */
public class BubbleEntry extends EntryCell {

	public static final int HEIGHT = 40;
    private final int PADS = 15;

    private Rectangle background;
    private TextField nameInput;
    private Label nameLabel;

    private DoubleProperty width;

	/**
	 * Creates an EntryBubble and assigns an entry to it.
     * @param entryBar the entryBar in which this BubbleEntry is contained
     * @param width the width of this EntryBubble
	 */
	public BubbleEntry(EntryBar entryBar, final DoubleProperty width) {
        subEntries = new ArrayList<>();
        nameLabel = new Label();

        this.entryBar = entryBar;
        GUI.setHeight(this, HEIGHT);
        GUI.bindWidth(this, width);

        this.getStyleClass().add("bubble");

        background = new Rectangle(width.get(), HEIGHT, Color.LIGHTGRAY);
        background.widthProperty().bind(this.widthProperty());
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.getStyleClass().add("bg");

        nameInput = new TextField();
        nameInput.setPromptText("Enter Entry name");
        nameInput.setAlignment(Pos.CENTER);
        initTextBounds(nameInput);

        getChildren().addAll(background, nameInput);

        setButtonFunctions();
        entry = new Entry("##BLANK##", new DateTime(), new DateTime());

        Platform.runLater(() -> nameInput.requestFocus());
	}

    public BubbleEntry(Entry e, EntryBar bar) {
        this(bar, bar.bubbleWidth);
        this.nameInput.setText(e.getName());
        entry = e;
 //
        System.out.println("GENERATED: " + this);
        finish(true);
    }

    public Entry getEntry() {
        return entry;
    }

    public Node getRoot() {
        return this;
    }

    private static final class DragContext {
        double x;
        int idx;
    }
	
	/**
	 * Initializes the bubble's responses to user input.
	 */
	public void setButtonFunctions() {
		//Click the bubble
        setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton() == MouseButton.SECONDARY)
                entryBar.openPopup(entry);
        });

        setOnMouseMoved(MouseEvent -> setCursor(Cursor.HAND));

        final DragContext drag = new DragContext();

        //stores the beginning position of the mouse
        setOnMousePressed((MouseEvent mouseEvent) -> {
            drag.x = this.getTranslateX() - mouseEvent.getSceneX();
            drag.idx = entryBar.overIdx(mouseEvent.getSceneX());

            if (mouseEvent.isControlDown()) entryBar.addFocus(this);
            else entryBar.setFocus(this);

            setCursor(Cursor.CLOSED_HAND);
        });

        //actually moves the node
        this.setOnMouseDragged((MouseEvent mouseEvent) ->{
            this.setTranslateX(mouseEvent.getSceneX() + drag.x);

            int idx = entryBar.overIdx(mouseEvent.getSceneX());
            entryBar.setFocus(this);
            entryBar.setMoving(this);

            if(idx != drag.idx) {
                entryBar.swap(idx);
                drag.idx = idx;
            }

        });

        this.setOnMouseReleased((MouseEvent event) -> {
            entryBar.setMoving(null);
            entryBar.fixPositions().play();
            setCursor(Cursor.HAND);
            this.expand();
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

    private void initTextBounds(final Region text) {
        text.translateXProperty().bind(this.widthProperty().subtract(text.widthProperty()).divide(2));
        text.translateYProperty().bind(this.heightProperty().subtract(text.heightProperty()).divide(2));
        GUI.bindWidth(text, this.widthProperty().subtract(PADS));
    }

    private void finish(boolean keep) {

        if(keep) {
            nameLabel.setText(nameInput.getText());
            nameLabel.setAlignment(Pos.CENTER);
            initTextBounds(nameLabel);

            getChildren().set(getChildren().indexOf(nameInput), nameLabel);

            if(entry == null)
                entry = new Entry(nameInput.getText(), new DateTime(), new DateTime());
        }

        entryBar.newEntryFinish(this, keep);
    }

    @Override
    public void expand() {
        entryBar.getEntryArea().expand(this);
    }

    @Override
    public void retract() { entryBar.getEntryArea().retract(this); }

    public String toString() {
        return "BubbleEntry :: " + super.toString();
    }

}
