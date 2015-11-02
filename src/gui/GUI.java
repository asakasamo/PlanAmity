package gui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Class that provides general functions that will be used throughout the GUI.
 * @author Al-John
 *
 */
public final class GUI {

	public final static int WINDOW_WIDTH = 1600;
	public final static int WINDOW_HEIGHT = 900;

	private static final class Delta {
		double x, y;
	}

	/**
	 * Makes a node draggable.
	 * @param node the node
	 */
	public static void makeDraggable(final Node node, boolean... lockX) {
		final Delta dragDelta = new Delta();
		
		//stores the beginning position of the mouse
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
				dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
			}
		});
		
		//actually move the node
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent mouseEvent) {
				node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
				if(lockX.length < 1) 
					node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
			}
		});
		
		//what happens when you drag a node over this one
		node.setOnDragOver(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				//...
			}
		});

		//activates the drag event to enable other nodes to detect drag over
		node.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override 
			public void handle(MouseEvent mouseEvent) {
				node.startFullDrag();
			}
		});
	}

	/**
	 * Converts a Color to a String of its RGB code (for use in CSS).
	 * For example, passing in Color.BLACK returns "#000000".
	 * @param color the color
	 * @return the RGB code
	 */
	public static String toRGBCode( Color color ) {
		return String.format( "#%02X%02X%02X",
				(int)( color.getRed() * 255 ),
				(int)( color.getGreen() * 255 ),
				(int)( color.getBlue() * 255 ) );
	}

}
