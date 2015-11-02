package gui;

import java.util.ArrayList;
import java.util.List;

import data.Entry;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ProjectView extends Pane {

	private EntryBubble focus; 								//TODO: Abstract the entries into Bubbles & SubEntries
	private List<EntryBubble> mainEntries; 					//main entries
	private final int CELL_WIDTH = EntryBubble.WIDTH + 10;	//width of the cells of the main entries

	public ProjectView() {

		//solid green background
		this.setStyle("-fx-background-color:green");
		mainEntries = new ArrayList<EntryBubble>();
		focus = null;

		//set view dimensions
		//		this.setPrefWidth(GUI.WINDOW_WIDTH);
		//		this.setPrefHeight(GUI.WINDOW_HEIGHT);

		PlusButton button = new PlusButton(this);
		GUI.makeDraggable(button);
		this.getChildren().add(button);

		initProperties();
	}

	static int i = 0;
	/**
	 * Handles the creation of a new Entry. All creations are done relative to the current focused
	 * Entry. If the focus is null, then the 
	 * 
	 * @param level true if the new Entry should is at the same level as the current focused
	 * 		entry; false if the new entry is a child.
	 */
	public void triggerAddNewEntry(boolean level) {
		//TODO: Write this
		Entry e = new Entry("Sample" + i++);

		//location on the main timeline
		double XONGRID = (1 + mainEntries.size()) * CELL_WIDTH; //TODO: actually figure it out
		double YONGRID = 200;
		EntryBubble bubble = new EntryBubble(e, this, XONGRID, YONGRID);

		//add to everything that needs to keep track of it
		this.mainEntries.add(bubble);
		this.getChildren().add(bubble);

		//set it as the focus
		this.focus = bubble;
	}

	/**
	 * Handles the deletion of an Entry
	 */
	public void triggerDeleteEntry(){

	}

	/**
	 * Initializes stuff.
	 */
	public void initProperties() {

	}

	/**
	 * Handles the clicking of an EntryCell.
	 * @param me the mouse event that triggered
	 * @param ec the EntryCell that was clicked
	 */
	public void entryClicked(MouseEvent me, EntryCell ec) {
		//TODO: Organize this fucking code
		int idx = mainEntries.indexOf(ec);

		//animation: shift all of the entry bubbles after the deleted one to the left
		for(int i = idx; i < mainEntries.size(); i++){
			final Timeline timeline = new Timeline();
			EntryBubble eb = mainEntries.get(i);
			final KeyValue kv = new KeyValue(eb.translateXProperty(), eb.getTranslateX() - CELL_WIDTH, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
			timeline.getKeyFrames().add(kf);
			timeline.play();
		}

		//final removal from the program
		this.mainEntries.remove(ec);
		this.getChildren().remove(ec);
	}
}