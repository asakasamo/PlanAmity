package gui.controls;

import data.Entry;
import gui.GUI;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
public class BubbleEntry extends Pane implements EntryCell {
	public static final int WIDTH = 100;
	public static final int HEIGHT = 50;

	private Entry entry;

//	private Circle progressIndicator;
//	private Rectangle lastModifiedIndicator;
//	private Rectangle attachmentIndicator;

    private StackPane container;
    private Text nameIndicator;
    private VBox topContainer;
    private VBox subContainer;
    private EntryBar entryBar;

    private boolean expanded;
	
	/**
	 * Creates an EntryBubble and assigns an entry to it.
	 * @param entry the entry assigned to this bubble
     * @param x the x coordinate
     * @param y the y coordinate
	 */
	public BubbleEntry(Entry entry, double x, double y, EntryBar entryBar) {
        expanded = false;
        container = new StackPane();
        topContainer = new VBox();
        subContainer = new VBox();
        this.entryBar = entryBar;

        //TODO: fix the bounds of this crazy thing
		this.setTranslateX(x);
		this.setTranslateY(y);

		container.setPadding(new Insets(5,5,5,5));
		this.entry = entry;
		
		//name indicator
		nameIndicator = new Text(entry.getName());
		nameIndicator.getStyleClass().add("bubbleText");
		
		//set background to owner's color
		Color assigned;
		if(entry.getAssignedTo() == null) assigned = Color.STEELBLUE;
		else assigned = entry.getAssignedTo().getColor();
		Rectangle background = new Rectangle(WIDTH - 5, HEIGHT - 5);
		background.getStyleClass().add("bubble-bg");
		background.setFill(assigned);
//		container.setStyle("-fx-background-color:" + GUI.toRGBCode(assigned));
		
		//style
		container.getStyleClass().add("bubble");
		
		container.getChildren().addAll( background,
//				progressIndicator,
//				lastModifiedIndicator,
				nameIndicator
//				attachmentIndicator
        );

        topContainer.getChildren().add(container);

        subContainer.setTranslateX(10);
        topContainer.getChildren().add(subContainer);

        this.getChildren().add(topContainer);

		initInputResponses();
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
	public void initInputResponses() {
		
		//Click the bubble
        container.setOnMouseClicked((MouseEvent event) -> {
            System.out.println(entry);

            if(expanded) retract();
            else expand();

            GUI.screenController.setFocus(this);
        });

        final DragContext drag = new DragContext();

        //stores the beginning position of the mouse
        setOnMousePressed((MouseEvent mouseEvent) -> {
            drag.x = this.getTranslateX() - mouseEvent.getSceneX();
            drag.idx = entryBar.overIdx(mouseEvent.getSceneX());
        });

        //actually moves the node
        this.setOnMouseDragged((MouseEvent mouseEvent) ->{

            this.setTranslateX(mouseEvent.getSceneX() + drag.x);

            int idx = entryBar.overIdx(mouseEvent.getSceneX());
            entryBar.setMoving(idx);

            if(idx != drag.idx) {
                entryBar.swap(idx, drag.idx);
                drag.idx = idx;
            }

        });

        this.setOnMouseReleased((MouseEvent event) -> {
            GUI.moveTo(this,
                    EntryBar.getProperX(entryBar.overIdx(event.getSceneX())),
                    this.getTranslateY()).play();

            System.out.println(event.getX());
        });
	}

    public void expand() {
        int i = 0;
        for (Entry e : entry.getChildren()) {
            subContainer.getChildren().add(new ListEntry(e, i++));
        }
        expanded = true;
    }

    @Override
    public void retract() {
        subContainer.getChildren().removeAll(subContainer.getChildren());
        expanded = false;
    }
}
