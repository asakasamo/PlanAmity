package gui;

import application.Main;
import gui.screens.ScreenController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Class that provides general functions that will be used throughout the GUI.
 * @author Al-John
 *
 */
public final class GUI {
    //Resolution height (A numerator)
    private static double resHeight;

    //(diagonal) monitor size (B numerator)
    private static double monitorSize;

    //denominator for A in the scaling formula
    private static final double A_DENOM = 720;

    //denominator for B in the scaling formula
    private static final double B_DENOM = 16;

    //default app width
    private static double GUI_width = 900;

    //default app height
    private static double GUI_height = 600;

    /**
     * This is the global scaling factor that is applied to all text and elements in the program (to ensure consistency
     * of the viewing experience across devices).
     *
     * Global scaling formula for all text & elements:
     *
     * <i>Size * a/b * UserSetZoom</i>,
     *
     * where a = Resolution Height / 720,
     * and b = Monitor Size (inches) / 16
     *
     */
    protected static double globalScale;

    public static ScreenController screenController; //this is the root node!

    public static Stage primaryStage;

    private GUI() {}

    /**
     * This is the user defined "zoom" setting to the program.
     */
    public static DoubleProperty ZOOM = new SimpleDoubleProperty(1);
    private static final double maxZoom = 2;
    private static final double minZoom = 10.0/12.0;

    /**
     * Initializes the main GUI elements - the primary Stage, Scene, and root node - with the Stage specified in Main.
     * @param primaryStage the primary stage
     */
    public static void init(Stage primaryStage) {
        GUI.primaryStage = primaryStage;

        initGlobals();
        initStage();
        initScene();
    }

    /**
     * Initializes the global variables referenced throughout the program.
     */
    private static void initGlobals() {

        resHeight = Screen.getPrimary().getBounds().getHeight();
        monitorSize = getMonitorSize();
        globalScale = (resHeight/A_DENOM) / (monitorSize/B_DENOM);

        //set starting zoom lower (to compensate for tablets)
        if(monitorSize < 9) { ZOOM.set(10.0/12.0); }
        else if(monitorSize < 13) { ZOOM.set(11.0/12.0); }
    }

    /**
     * //TODO:Load from settings file
     * @return the diagonal monitor size
     */
    private static double getMonitorSize() {
        return 16;
    }

    /**
     * Initializes the Stage's attributes.
     */
    private static void initStage(){
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setMaximized(false);

        primaryStage.setMinWidth(GUI_width);
        primaryStage.setMinHeight(GUI_height);

        primaryStage.setTitle("planAmity");
        primaryStage.setResizable(true);
    }

    /**
     * Initializes the Scene, including its root Node, and assigns it to the primary Stage.
     */
    private static void initScene() {
        screenController = new ScreenController();

        Scene scene = new Scene(screenController, GUI_width, GUI_height, Color.TRANSPARENT);

        scene.getStylesheets().add(Main.instance().getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }

    /**
     * Starts the program - only to be called after init() has completed.
     */
    public static void start() {
        GUI.primaryStage.show();
        screenController.goTo(ScreenController.STARTUP_MENU);
//        screenController.goTo(ScreenController.OVERVIEW);
    }

    /**
     * Returns a specified value adjusted to the app's global scale.
     * @param d the specified value
     */
    public static double toScale(double d) {
        return d * GUI.globalScale * GUI.ZOOM.get();
    }

    /**
     * Zooms the program in or out, based on the given parameter, by 1/12. Zoom values are limited to [10/12, 2].
     * @param in if true, zooms in; if false, zooms out.
     */
    public static void zoom(boolean in) {

        //zooms in, if not already max zoom
        if(in && ZOOM.get() < maxZoom) {
            ZOOM.set(ZOOM.get() + 1.0/12.0);
        }

        //zooms out, if not already min zoom
        if(!in && ZOOM.get() > minZoom){
            ZOOM.set(ZOOM.get() - 1.0/12.0);
        }
    }

    private static final class Delta {
		double x, y;
	}

	/**
	 * Makes a node draggable.
	 * @param node the node
     * @param lockX if specified, will lock the dragging to only the X position
	 */
	public static void makeDraggable(final Node node, boolean... lockX) {
		final Delta dragDelta = new Delta();
		
		//stores the beginning position of the mouse
		node.setOnMousePressed((MouseEvent mouseEvent) -> {
            dragDelta.x = node.getTranslateX() - mouseEvent.getSceneX();
            dragDelta.y = node.getTranslateY() - mouseEvent.getSceneY();
        });
		
		//actually move the node
        node.setOnMouseDragged((MouseEvent mouseEvent) ->{
            node.setTranslateX(mouseEvent.getSceneX() + dragDelta.x);
            if(lockX.length < 1)
                node.setTranslateY(mouseEvent.getSceneY() + dragDelta.y);
        });

		//activates the drag event to enable other nodes to detect drag over
		node.setOnDragDetected((MouseEvent mouseEvent) -> node.startFullDrag());
	}

	/**
	 * Converts a Color to a String of its RGB code (e.g. for use in CSS).
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

    /**
     * Creates a fade effect for the specified Node.
     * @param n the specified node
     * @param in true if this is a fade in; false if this is a fade out.
     * @param duration the duration of the fade in milliseconds (default duration is 500 if not provided). Only the
     *                 first argument is used; the rest are ignored.
     * @return the final fade effect
     */
    public static Timeline fade(final Node n, boolean in, int... duration) {
        n.setOpacity(in ? 0 : 1);
        int dur = duration.length == 0 ? 500 : duration[0];

        final Timeline timeline = new Timeline();
        final KeyValue kv = new KeyValue(n.opacityProperty(), in ? 1 : 0, Interpolator.EASE_BOTH);

        final KeyFrame kf = new KeyFrame(Duration.millis(dur), kv);
        timeline.getKeyFrames().add(kf);

        return timeline;
    }

    /**
     * Creates a zoom-and-fade effect for a specified node.
     * @param n the Node
     * @param in true if fading in; false if fading out.
     * @param duration the duration of the animation (defaults to 500 ms). An additional parameter can be provided to
     *                 indicate the final value of the zoom (defaults to 95%).
     * @return the generated animation
     */
    public static Timeline zoomFade(final Node n, boolean in, int... duration) {

        int dur = duration.length == 0 ? 500 : duration[0];
        double endScale = duration.length == 2 ? duration[1] : .95;

        //set the starting attributes
        n.setOpacity(in ? 0 : 1);
        n.setScaleX(in ? endScale : 1);
        n.setScaleY(in ? endScale : 1);

        final Timeline timeline = new Timeline();
        final KeyValue kv = new KeyValue(n.opacityProperty(), in ? 1 : 0, Interpolator.EASE_BOTH);
        final KeyValue kv2 = new KeyValue(n.scaleXProperty(), in ? 1 : endScale, Interpolator.EASE_BOTH);
        final KeyValue kv3 = new KeyValue(n.scaleYProperty(), in ? 1 : endScale, Interpolator.EASE_BOTH);

        final KeyFrame kf = new KeyFrame(Duration.millis(dur), kv, kv2, kv3);
        timeline.getKeyFrames().add(kf);

        return timeline;
    }

    /**
     * Creates an animation that changes the transform of a specified node to specified coordinates.
     * @param n the node
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the duration of the animation (defaults to 200)
     * @return the generated animation
     */
    public static Timeline moveTo(final Node n, double x, double y, int... duration) {
        int dur = duration.length == 0 ? 200 : duration[0];

        final Timeline timeline = new Timeline();
        final KeyValue kv = new KeyValue(n.translateXProperty(), x, Interpolator.EASE_BOTH);
        final KeyValue kv2 = new KeyValue(n.translateYProperty(), y, Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(dur), kv, kv2);
        timeline.getKeyFrames().add(kf);
        return timeline;
    }

    public static void minimize() {
        primaryStage.setIconified(!primaryStage.isIconified());
    }

    public static void maximize() {
        primaryStage.setMaximized(!primaryStage.isMaximized());
    }

    /**
     * Exits the program, with a fancy fade-out animation.
     */
    public static void exit() {
        Timeline close = zoomFade(screenController, false, 200);
        close.setOnFinished((ActionEvent done) -> primaryStage.close());
        close.play();
    }
}
