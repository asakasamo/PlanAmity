package gui.controls;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * The PlusButton class is the screenController for the Plus Button, which is the primary element in the
 * interface used to add new Entries to the project. The PlusButton is attached to a single ProjectView,
 * and sends triggers to it based on which half of the button is clicked.
 * 
 * @author Al-John
 *
 */
public class PlusButton extends StackPane {
	private double width;
	private double height;

	private Pane topHalf;
	private Pane botHalf;
	private Circle topHalfCircle;
	private Circle botHalfCircle;

    private VBox container;

	public PlusButton(){
		width = height = 100;

        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        this.setMinWidth(width);
        this.setMinHeight(height);

		topHalf = new Pane();
		botHalf = new Pane();
        container = new VBox();

		//hide the overflow (for the half-circle effect)
		topHalf.setClip(new Rectangle(width, height/2));
		botHalf.setClip(new Rectangle(width, height/2));

		//Color indicators for top and bottom halves
//		topHalf.setStyle("-fx-background-color:pink");
//		botHalf.setStyle("-fx-background-color:purple");

		//initialize top half circle
		topHalfCircle = new Circle(width/2, Color.GREEN);
		topHalfCircle.setTranslateX(width/2);
		topHalfCircle.setTranslateY(height/2);

		topHalf.getChildren().add(topHalfCircle);

		//initialize bot half circle
		botHalfCircle = new Circle(width/2, Color.RED);
		botHalfCircle.setTranslateX(width/2);

		botHalf.getChildren().add(botHalfCircle);

		//add both halves to the button
		container.getChildren().addAll(topHalf, botHalf);
        container.setMaxHeight(height);
        container.setMaxWidth(width);
        this.getChildren().add(container);

        this.setMinWidth(125);
        this.setMinHeight(125);

        hoverMe(topHalfCircle);
        hoverMe(botHalfCircle);
//        GUI.makeDraggable(this);
    }

	/**
	 * As of now, sets the hover events for the half-circles in the PlusButton.
	 * @param n the Circle
	 */
	private void hoverMe(final Circle n){
		Paint orig = n.getFill();

        n.setOnMouseEntered((MouseEvent event) -> n.setFill(Color.YELLOW));

        n.setOnMouseExited((MouseEvent event) -> n.setFill(orig));

//        n.setOnMouseClicked((MouseEvent event) -> GUI.screenController.goTo(ScreenController.STARTUP_MENU));
	}

    public Node getTopHalf() {
        return topHalfCircle;
    }

    public Node getBotHalf() {
        return botHalfCircle;
    }
}