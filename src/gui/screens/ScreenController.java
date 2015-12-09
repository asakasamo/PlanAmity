package gui.screens;

import data.Project;
import gui.GUI;
import gui.controls.ParticipantKey;
import gui.controls.TitleBar;
import gui.controls.ViewBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
 *
 * The ScreenController is designed to be used as the root node of the primary Scene.
 */
public class ScreenController extends Pane {

    private Screen activeScreen;
    private final TitleBar titleBar;
    private ParticipantKey participantKey;
    private ViewBar viewBar;

    private Project activeProject;

    public static final int STARTUP_MENU = 0,
            OVERVIEW = 1,
            TIME_VIEW = 2,
            LIST_VIEW = 3,
            CALENDAR_VIEW = 4;

    public ScreenController() {
        activeProject = Project.sampleProject();

        titleBar = new TitleBar(this);
        viewBar = new ViewBar(this);
        participantKey = new ParticipantKey(this);

        activeScreen = null;
        setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode() == KeyCode.ESCAPE)
                GUI.exit();
        });
    }

    public final TitleBar getTitleBar() {
        return titleBar;
    }

    public final ParticipantKey getParticipantKey() {
        return participantKey;
    }

    public final ViewBar getViewBar() {
        return viewBar;
    }

    public final Project getActiveProject() {
        return activeProject;
    }

    /**
     * Switches the current screen to a specified screen, calling each screen's respective transition methods.
     * @param screen The screen to go to. Potential values for this parameter are available as static final fields of
     *               this class (STARTUP_MENU, OVERVIEW, etc.).
     */
    public void goTo(int screen) {
        Screen next;
        switch(screen){
            case STARTUP_MENU:  next = new StartupMenu(); break;
            case OVERVIEW:      next = new Overview(this); break;
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

    }

    private void initScreen(Screen screen) {
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
        viewBar = new ViewBar(this);
        participantKey = new ParticipantKey(this);
    }

}
