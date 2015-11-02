package gui;

import java.util.List;

import data.Entry;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public final class GUI {

	public final static int WINDOW_WIDTH = 1600;
	public final static int WINDOW_HEIGHT = 900;

	public final Node createNode(Entry e, int depth) {
		final VBox bubble = new VBox(5);
		//final AnchorPane bubble = new AnchorPane();
		bubble.setPadding(new Insets(0, 0, 0, depth * 10));

		final Label text = new Label(e.getName());

		if(e.getParent() == null){ //is a top level entry
			text.setMinSize(100, 50);
		} else { //is a sub-entry
			text.setMinSize(80, 40);
		}

		text.setStyle("-fx-background-color:" + toRGBCode(e.getColor()) + ";-fx-font-size:30;");
		text.setId(text.getText());

		text.setTooltip(new Tooltip("wow..."));

		bubble.getChildren().add(text);
		//		bubble.getChildren().add(text);

		//draw the children
		List<Entry> sub = e.getChildren();
		for(Entry f : sub){
			bubble.getChildren().add(createNode(f, depth +1));
		}

		bubble.setId(e.getAssignedTo().toString() + "#" + depth);

		//		return (bubble);
		return bubble;
	}

	private static final class Delta {
		double x, y;
	}

	/**
	 * Makes a node draggable.
	 * @param node the node
	 */
	public static void makeDraggable(final Node node, boolean... lockX) {
		final Delta dragDelta = new Delta();
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
				dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
			}
		});
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent mouseEvent) {

				node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
				if(lockX.length < 1) 
					node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
			}
		});
		
		node.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				
				// TODO Auto-generated method stub
//				node.setLayoutX(event.getSceneX() + dragDelta.x);
//				if(lockX.length < 1) 
//					node.setLayoutY(event.getSceneY() + dragDelta.y);
			}
			
		});

		node.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent mouseEvent) {
				node.startFullDrag();
//				Dragboard db = node.startDragAndDrop(TransferMode.ANY);
//				ClipboardContent content = new ClipboardContent();
//				content.put(new DataFormat("text/Object"), node);
//				db.setContent(content);
//				mouseEvent.consume();
			}
		});
	}

	public static String toRGBCode( Color color ) {
		return String.format( "#%02X%02X%02X",
				(int)( color.getRed() * 255 ),
				(int)( color.getGreen() * 255 ),
				(int)( color.getBlue() * 255 ) );
	}

	/**
	 * Returns the center x coordinate of a given node.
	 * @param n the node
	 * @return the center y coordinate
	 */
	public static double getCenterX(Node n) {
		return 0;
	}

	/**
	 * Returns the center y coordinate of a given node.
	 * @param n the node
	 * @return the center y coordinate
	 */
	public static double getCenterY(Node n) {
		return 0;
	}

}
