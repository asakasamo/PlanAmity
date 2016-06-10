package gui.screens;

import data.Entry;
import data.Project;
import gui.GUI;
import gui.controls.general.ParticipantKey;
import gui.controls.general.PlusButton;
import gui.controls.general.TitleBar;
import gui.controls.general.ViewBar;
import gui.controls.overview.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Controller class for the Overview screen. Manages all user interaction with the interface, modifying the Project as
 * necessary.
 *
 * The Overview comprises, from top to bottom:
 * - Title Bar (20px tall)
 * - View Bar (27px tall)
 *      - View Frame (118 px wide)
 * - Timeline Bar (36px tall)
 * - Entry Bar (40px tall, 1px border)
 * - Entry Area
 *      - Side Timeline
 * - Participant Key
 * - Plus Button
 *
 * @author Al-John
 *
 */
public class Overview extends Screen {

    private TitleBar titleBar;
    private ViewBar viewBar;
    private TimelineBar timelineBar;
    private EntryBar entryBar;
    private EntryArea entryArea;
    private ParticipantKey participantKey;
    private PlusButton plusButton;
    private ScreenController screenController;

    public Overview(ScreenController screenController) {

        this.getStyleClass().add("overview");

//        this.setStyle("-fx-background-color:" + GUI.toRGBCode(Color.SKYBLUE));
        this.screenController = screenController;

        populate();
        setButtonFunctions();
    }

    @Override
    public void populate() {
        timelineBar = new TimelineBar(this);
        entryBar = new EntryBar(this);
        entryArea = entryBar.getEntryArea();
        participantKey = screenController.getParticipantKey();
        plusButton = new PlusButton(this);

        //vertical offset (to ensure things are right on top of each other)
        double yOffset = 0;

        //add element
        addElement(timelineBar, yOffset);
        addElement(entryBar, yOffset += timelineBar.HEIGHT);
        addElement(entryArea, yOffset + EntryBar.HEIGHT);

        anchorToBottomRight(plusButton, 30, 30);
        anchorToBottomLeft(participantKey, 30, 30);

        loadActiveProject();

        getChildren().add(participantKey);
        getChildren().add(plusButton);
    }

    private void anchorToBottomRight(Pane pane, double xGap, double yGap){
        pane.translateXProperty().bind(this.widthProperty().subtract(pane.heightProperty()).subtract(xGap));
        pane.translateYProperty().bind(this.heightProperty().subtract(pane.heightProperty()).subtract(yGap));
    }

    private void anchorToBottomLeft(Pane pane, double xGap, double yGap) {
        pane.setTranslateX(xGap);
        pane.translateYProperty().bind(this.heightProperty().subtract(pane.heightProperty()).subtract(yGap));
    }

    /**
     * Loads the currently active project into the Overview. The active project is accessed from the ScreenController
     * instance passed in from the constructor.
     */
    private void loadActiveProject() {
        entryBar.beginStartup();

        Project project = screenController.getActiveProject();
        List<Entry> entries = project.getEntries();

        //load in the top level entries
        for(Entry e : entries){
            EntryCell cell = EntryCell.generate(e, null, entryBar); //These are all BubbleEntries, guaranteed.
            entryBar.loadBubbleEntry((BubbleEntry)cell);
        }

        entryBar.endStartup();
    }

    private void addElement(Pane pane, double yOffset) {
        pane.setTranslateY(yOffset);
        getChildren().add(pane);
    }

    private void setButtonFunctions() {
        plusButton.getTopHalf().setOnMouseClicked(MouseEvent -> entryBar.newEntryStart(true));
        plusButton.getBotHalf().setOnMouseClicked(MouseEvent -> entryBar.newEntryStart(false));
    }

    public void exclusiveFocus(Node n){

    }

    public void relaxFocus() {

    }

    public ScreenController getScreenController() {
        return screenController;
    }
}