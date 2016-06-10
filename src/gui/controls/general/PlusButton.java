package gui.controls.general;

import gui.GUI;
import gui.screens.Screen;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
    public final double RADIUS = 31;

	private Pane topHalf;
	private Pane botHalf;
	private Circle topHalfCircle;
	private Circle botHalfCircle;
    private VBox halvesContainer;

    private Circle circle;
    private Label plus;

	public PlusButton(Screen screen){
        GUI.setWidth(this, RADIUS*2);
        GUI.setHeight(this, RADIUS*2);

		topHalf = new Pane();
		botHalf = new Pane();
        halvesContainer = new VBox();
        plus = new Label("+");

        circle = new Circle(RADIUS, Color.LIGHTGRAY);
        circle.setStroke(Color.BLACK);

        plus.setStyle("-fx-font-size:35px");

		//hide the overflow (for the half-circle effect)
		topHalf.setClip(new Rectangle(RADIUS*2 +2, RADIUS +2));
		botHalf.setClip(new Rectangle(RADIUS*2 +2, RADIUS +2));

		//Color indicators for top and bottom halves
//		topHalf.setStyle("-fx-background-color:pink");
//		botHalf.setStyle("-fx-background-color:purple");

		//initialize top half circle
		topHalfCircle = new Circle(RADIUS, Color.TRANSPARENT);
		topHalfCircle.setTranslateX(RADIUS +1);
		topHalfCircle.setTranslateY(RADIUS +1);

		topHalf.getChildren().add(topHalfCircle);

		//initialize bot half circle
		botHalfCircle = new Circle(RADIUS, Color.TRANSPARENT);
		botHalfCircle.setTranslateX(RADIUS +1);

		botHalf.getChildren().add(botHalfCircle);

		//add both halves to the button
		halvesContainer.getChildren().addAll(topHalf, botHalf);
        halvesContainer.setMaxHeight(RADIUS*2);
        halvesContainer.setMaxWidth(RADIUS*2);

        Line ns = new Line(14,0,14,28);
        Line ew = new Line(0,14,28,14);
        ns.setFill(Color.BLACK);
        ns.setStrokeWidth(2);
        ew.setFill(Color.BLACK);
        ew.setStrokeWidth(2);

        topHalfCircle.setCursor(Cursor.HAND);
        botHalfCircle.setCursor(Cursor.HAND);
        ns.setCursor(Cursor.HAND);
        ew.setCursor(Cursor.HAND);
        ns.setDisable(true);
        ew.setDisable(true);
        circle.setCursor(Cursor.HAND);

        this.getChildren().addAll(circle, halvesContainer, ns, ew);

        setButtonFunctions();
    }

	/**
	 * As of now, sets the hover events for the half-circles in the PlusButton.
	 */
	private void setButtonFunctions(){

		topHalfCircle.setOnMouseMoved(MouseEvent -> {
            turnOff(botHalfCircle);
            turnOn(topHalfCircle);
        });

        botHalfCircle.setOnMouseMoved(MouseEvent -> {
            turnOff(topHalfCircle);
            turnOn(botHalfCircle);
        });

        this.setOnMouseExited(MouseEvent -> {
            turnOff(topHalfCircle);
            turnOff(botHalfCircle);
        });
	}

    public void combine() {
        getChildren().remove(halvesContainer);
        circle.setOnMouseMoved(MouseEvent -> turnOn(circle));
        circle.setOnMouseExited(MouseEvent -> circle.setFill(Color.LIGHTGRAY));
    }

    private void turnOff(Circle c) {
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.TRANSPARENT);
    }

    private void turnOn(Circle c) {
        c.setFill(Color.YELLOW);
        c.setStroke(Color.BLACK);
    }

    public Node getTopHalf() {
        return topHalfCircle;
    }
    public Node getBotHalf() {
        return botHalfCircle;
    }
    public Node getCircle() { return circle; }
}