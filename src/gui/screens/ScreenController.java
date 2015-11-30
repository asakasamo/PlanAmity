package gui.screens;

import data.Project;
import gui.GUI;
import gui.controls.EntryCell;
import gui.controls.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;

/**
 * The ScreenController class manages the transitions between the application's main screens. Currently, the screens
 * comprise:
 *
 * - Startup Menu
 * - Overview View
 * - Time View
 * - List View
 * - Calendar View
 */
public class ScreenController extends Pane {

    private Screen activeScreen;
    private final MenuBar menuBar;

    public static Project activeProject;
    public static EntryCell focus;

    public static final int STARTUP_MENU = 0,
            OVERVIEW = 1,
            TIME_VIEW = 2,
            LIST_VIEW = 3,
            CALENDAR_VIEW = 4;

    public ScreenController() {
        activeScreen = null;
        activeProject = new Project();
        menuBar = new MenuBar(this);
    }

    public void goTo(int screen) {
        Screen next = null;
        switch(screen){
            case STARTUP_MENU:  next = new StartupMenu(); break;
            case OVERVIEW:      next = new Overview(); break;
            case TIME_VIEW:     next = new TimeView(); break;
            case LIST_VIEW:     next = new ListView(); break;
            case CALENDAR_VIEW: next = new CalendarView(); break;
            default: System.out.println("Invalid screen index."); next = new StartupMenu(); break;
        }

        if(activeScreen != null) {
            activeScreen.transitionOut();
        }

        initScreen(next);
        getChildren().add(next);
        next.transitionIn();
        activeScreen = next;

        getChildren().remove(menuBar);
        if(screen != STARTUP_MENU) {
            getChildren().add(menuBar);
        }
    }

    private void initScreen(Screen screen) {
        if(!(screen instanceof StartupMenu)) {
            screen.setTranslateY(menuBar.getHeight());
        }

        screen.scaleXProperty().bind(GUI.ZOOM);
        screen.scaleYProperty().bind(GUI.ZOOM);
        screen.minWidthProperty().bind(this.widthProperty());
        screen.minHeightProperty().bind(this.heightProperty());
    }

    /**
     * Sets the currently active project.
     * @param p the currently active project
     */
    public void setActiveProject(Project p) {
        activeProject = p;
    }

    /**
     * Sets the current focused EntryCell
     * @param ec the EntryCell to focus
     */
    public void setFocus(EntryCell ec) {
        if(focus != null) focus.getRoot().getStyleClass().remove("focused");
        focus = ec;
        if(focus != null) focus.getRoot().getStyleClass().add("focused");
    }
}
