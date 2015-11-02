package gui.controls;

import data.Entry;
import gui.screens.ProjectView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * The EntryBubble class is a GUI wrapper that represents a top level Entry in the interface. It
 * modifies itself visually based on the attributes of the Entry assigned to it.
 * 
 * TODO: Abstract The entries into Bubbles & SubEntries
 * 
 * @author Al-John
 *
 */
public class BubbleEntry extends StackPane implements EntryCell {
	public static final int WIDTH = 200;
	public static final int HEIGHT = 100;

	private Entry entry;
	
	private Circle progressIndicator;
	private Rectangle lastModifiedIndicator;
	private Text nameIndicator;
	private Rectangle attachmentIndicator;
	private ProjectView projectView;
	
	private double width;
	private double height;
	
	/**
	 * Creates an EntryBubble and assigns an entry to it.
	 * @param entry the entry assigned to this bubble
	 * @param pv the ProjectView associated with this EntryBubble
     * @param x the x coordinate
     * @param y the y coordinate
	 */
	public BubbleEntry(Entry entry, ProjectView pv, double x, double y) {
		width = 200;
		height = 100;
		
		this.setTranslateX(x);
		this.setTranslateY(y);
		this.setMinWidth(width);
		this.setMinHeight(height);
		this.setPadding(new Insets(5,5,5,5));
		this.entry = entry;
		projectView = pv;
		
		//progress indicator
		progressIndicator = new Circle(20);
		Color prog;
		switch(entry.getPercentComplete()) {
			case 0: prog = Color.RED; break;
			case 100: prog = Color.GREEN; break;
			default: prog = Color.YELLOW;
		}
		progressIndicator.setFill(prog);
		setAlignment(progressIndicator, Pos.TOP_LEFT);
		
		//last modified indicator
		lastModifiedIndicator = new Rectangle(50,50);
		Color lastmod;
		if(entry.getLastModifiedBy() == null) lastmod = Color.GRAY;
		else lastmod = entry.getLastModifiedBy().getColor();
		lastModifiedIndicator.setFill(lastmod);
		setAlignment(lastModifiedIndicator, Pos.TOP_RIGHT);
		
		//name indicator
		nameIndicator = new Text(entry.getName());
		nameIndicator.getStyleClass().add("bubbleText");
		
		//attachmentIndicator
		attachmentIndicator = new Rectangle(20,20);
		if(!entry.hasAttachments()) attachmentIndicator.setOpacity(0);
		setAlignment(attachmentIndicator, Pos.BOTTOM_RIGHT);
		
		//set background to owner's color
		Color assigned;
		if(entry.getAssignedTo() == null) assigned = Color.STEELBLUE;
		else assigned = entry.getAssignedTo().getColor();
		Rectangle background = new Rectangle(width,height);
		background.getStyleClass().add("bubble-bg");
		background.setFill(assigned);
		//this.setStyle("-fx-background-color:" + GUI.toRGBCode(assigned));
		
		//style
		this.getStyleClass().add("bubble");
		
		this.getChildren().addAll( background,
				progressIndicator, 
				lastModifiedIndicator, 
				nameIndicator, 
				attachmentIndicator);
		
		initInputResponses();
	}
	
	/**
	 * Initializes the bubble's responses to user input.
	 */
	public void initInputResponses() {
		BubbleEntry eb = this;
		
		//Click the bubble
        this.setOnMouseClicked((MouseEvent event) -> projectView.entryClicked(event, eb));
		
		//Drag into the bubble
        this.setOnMouseDragEntered((MouseEvent event) -> projectView.entryClicked(null, eb));
	}
}
