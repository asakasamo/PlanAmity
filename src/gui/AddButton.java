package gui;

import data.Entry;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class AddButton extends VBox {
	private double width;
	private double height;

	private Pane topHalf;
	private Pane botHalf;
	private Circle topHalfCircle;
	private Circle botHalfCircle;

	ProjectView projectView;

	public AddButton(ProjectView pv){
		width = height = 150;
		projectView = pv;

		this.setMaxWidth(width);
		this.setMaxHeight(height);

		topHalf = new Pane();
		botHalf = new Pane();

		//sets clip to hide overflow
		topHalf.setClip(new Rectangle(width, height/2));
		botHalf.setClip(new Rectangle(width, height/2));

		//Color indicators for top and bottom halves
				topHalf.setStyle("-fx-background-color:pink");
		//		botHalf.setStyle("-fx-background-color:purple");
		//		this.setStyle("-fx-background-color:yellow");

		//initialize top half circle
		topHalfCircle = new Circle(width/2, Color.BLUE);
		topHalfCircle.setTranslateX(width/2);
		topHalfCircle.setTranslateY(height/2);
		topHalfCircle = (Circle)hoverMe(topHalfCircle);

		topHalf.getChildren().add(topHalfCircle);

		//initialize bot half circle
		botHalfCircle = new Circle(width/2, Color.RED);
		botHalfCircle.setTranslateX(width/2);
		botHalfCircle = (Circle)hoverMe(botHalfCircle);

		botHalf.getChildren().add(botHalfCircle);

		//add both halves to the button
		this.getChildren().add(topHalf);
		this.getChildren().add(botHalf);
	}

	private Circle hoverMe(Circle n){
		Paint orig = n.getFill();

		n.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				n.setFill(Color.YELLOW);
			}
		});

		n.setOnMouseExited((new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				n.setFill(orig);
			}
		}));

		n.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event){
				projectView.triggerAddNewEntry(true);
			}
		});

		return n;
	}
}