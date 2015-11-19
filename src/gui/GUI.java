package gui;

import application.Main;
import gui.screens.ProjectView;
import gui.screens.StartupMenu;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    public static Stage primaryStage;
    protected static Group root; //TODO: Root is a ScreenController

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

        primaryStage.setMinWidth(GUI_width);
        primaryStage.setMinHeight(GUI_height);

//        primaryStage.setMaximized(true);
        primaryStage.setTitle("planAmity");
        primaryStage.setResizable(true);
    }

    /**
     * Initializes the Scene, including its root Node, and assigns it to the primary Stage.
     */
    private static void initScene() {
        root = new Group(); //TODO: init to a ScreenController

        Scene scene = new Scene(root, GUI_width, GUI_height, Color.TRANSPARENT);
        scene.getStylesheets().add(Main.instance().getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }

    /**
     * Starts the program - only to be called after init() has completed.
     */
    public static void start() {
        //Ideally, add the main menu here
        StartupMenu menu = new StartupMenu();
//        ProjectView menu = new ProjectView(null);

        root.getChildren().add(menu);

        GUI.primaryStage.show();

        //must be done after show
        menu.fixDivider();
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
	 */
	public static void makeDraggable(final Node node, boolean... lockX) {
		final Delta dragDelta = new Delta();
		
		//stores the beginning position of the mouse
		node.setOnMousePressed((MouseEvent mouseEvent) -> {
            dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
            dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
        });
		
		//actually move the node
        node.setOnMouseDragged((MouseEvent mouseEvent) ->{
            node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            if(lockX.length < 1)
                node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
        });

        //what happens when you drag a node over this one
        node.setOnDragOver((DragEvent event) -> {

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
     * Switches the current screen to a specified screen.
     * @param screen the screen to switch to
     */
    public static void switchToScreen(Pane screen){
//        makeDraggable(root);
    }

    /**
     * Exits the program.
     */
    public static void exit() {
        primaryStage.close();
    }
}
