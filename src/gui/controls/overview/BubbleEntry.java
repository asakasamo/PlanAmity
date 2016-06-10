package gui.controls.overview;

import data.DateTime;
import data.Entry;
import gui.GUI;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * The EntryBubble class is the visual representation of a top level Entry in a Project.
 * 
 * @author Al-John
 */
public class BubbleEntry extends EntryCell {

	public static final int HEIGHT = 40;
    private final int PADS = 15;

    private Rectangle background;
    private TextField nameText;

    private DoubleProperty width;
    private boolean moving;
    private boolean isNewFocus;

	/**
	 * Creates a new EntryBubble, allowing user input for the creation of a new Entry.
     * @param entryBar the entryBar in which this BubbleEntry is contained
     * @param width the width property that this BubbleEntry will be bound to
	 */
	public BubbleEntry(EntryBar entryBar, final DoubleProperty width) {
        subEntries = new ArrayList<>();
        moving = false;
        isNewFocus = false;

        this.entryBar = entryBar;
        GUI.setHeight(this, HEIGHT);
        GUI.bindWidth(this, width);

        this.getStyleClass().add("bubble");

        background = new Rectangle(width.get(), HEIGHT);
        background.widthProperty().bind(this.widthProperty());
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.getStyleClass().add("bg");
        background.setStroke(Color.BLACK);

        nameText = new TextField();
        nameText.setPromptText("Enter Entry name");
//        nameText.setAlignment(Pos.CENTER);
        initTextBounds(nameText);

        getChildren().addAll(background, nameText);

        setButtonFunctions();
//        entry = new Entry("##BLANK##", new DateTime(), new DateTime());

        Platform.runLater(() -> nameText.requestFocus());
	}

    /**
     * Creates a BubbleEntry for a specified Entry, skipping the user input phase of its creation.
     * @param entry the Entry
     * @param bar the containing EntryBar
     */
    public BubbleEntry(Entry entry, EntryBar bar) {
        this(bar, bar.bubbleWidth);
        this.entry = entry;

        nameText.setText(entry.getName());
        nameText.setDisable(true);
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
            isNewFocus = true;

            if(mouseEvent.isAltDown() && mouseEvent.isControlDown() && mouseEvent.isShiftDown())
                entryBar.ctrlAltShiftClick();
            else if(mouseEvent.isAltDown())
                entryBar.altClick(this);
            else if (mouseEvent.isControlDown())
                entryBar.ctrlClick(this);
            else if (mouseEvent.isShiftDown()) {
                entryBar.shiftClick(this);
            }
            else {
                isNewFocus = entryBar.setFocus(this);
                if(isNewFocus)
                    expand();
            }

            setCursor(Cursor.CLOSED_HAND);
        });

        //actually moves the node
        this.setOnMouseDragged((MouseEvent mouseEvent) ->{
            moving = true;
            this.setTranslateX(mouseEvent.getSceneX() + drag.x);

            int idx = entryBar.overIdx(mouseEvent.getSceneX());
            entryBar.setMoving(this);

            if(idx != drag.idx) {
                entryBar.swap(idx);
                drag.idx = idx;
            }
        });

        this.setOnMouseReleased((MouseEvent event) -> {
            if(moving){
                moving = false;
                entryBar.stopMoving();
                entryBar.fixPositions().play();
//                expand();
            }
            else if(!isNewFocus){
                toggle();
            }

            setCursor(Cursor.HAND);
        });

        this.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                if(!nameText.getText().isEmpty())
                    finish();
            }
            if(event.getCode() == KeyCode.ESCAPE){
                die();
            }
        });
	}

    private void initTextBounds(final Region text) {
        text.translateXProperty().bind(this.widthProperty().subtract(text.widthProperty()).divide(2));
        text.translateYProperty().bind(this.heightProperty().subtract(text.heightProperty()).divide(2));
        GUI.bindWidth(text, this.widthProperty().subtract(PADS));
    }

    /**
     * Finalizes the creation of an EntryBubble.
     */
    private void finish() {
        nameText.setDisable(true);
        entryBar.newEntryFinish();
    }

    @Override
    public void die() {
        Timeline disappear = GUI.fade(this, false);
        disappear.setOnFinished(ActionEvent -> entryBar.delete(this));
        disappear.play();
        entryBar.newEntryCancel();
    }

    @Override
    public void expand() {
        expanded = true;
        entryBar.getEntryArea().expand(this);
    }

    @Override
    public void retract() {
        expanded = false;
        entryBar.getEntryArea().retract(this);
    }

    public String toString() {
        return "BubbleEntry :: " + super.toString();
    }

    public String getEntryName() {
        return nameText.getText();
    }
}
