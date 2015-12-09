package gui.screens;

import data.Entry;
import data.Project;
import gui.GUI;
import gui.controls.*;
import gui.controls.overview.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebHistory;

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

    private EventHandler<MouseEvent> focusFilter;

    public Overview(ScreenController screenController) {

        this.getStyleClass().add("overview");
        this.prefWidthProperty().bind(screenController.widthProperty());
        this.prefHeightProperty().bind(screenController.heightProperty());

        focusFilter = MouseEvent::consume;

        this.setStyle("-fx-background-color:" + GUI.toRGBCode(Color.LIGHTBLUE));
        this.screenController = screenController;

        populate();
        setButtonFunctions();
    }

    @Override
    public void populate() {
        titleBar = screenController.getTitleBar();
        viewBar = screenController.getViewBar();
        timelineBar = new TimelineBar(this);
        entryBar = new EntryBar(this);
        entryArea = entryBar.getEntryArea();
        participantKey = screenController.getParticipantKey();
        plusButton = new PlusButton(this);

        //vertical offset (to ensure things are right on top of each other)
        double yOffset = 0;

        //add element
        addElement(titleBar, yOffset);
        addElement(viewBar, yOffset += titleBar.HEIGHT);
        addElement(timelineBar, yOffset += viewBar.HEIGHT);
        addElement(entryBar, yOffset += timelineBar.HEIGHT);
        addElement(entryArea, yOffset + EntryBar.HEIGHT);

        plusButton.translateXProperty().bind(this.widthProperty().subtract(plusButton.RADIUS*2).subtract(15));
        plusButton.translateYProperty().bind(this.heightProperty().subtract(plusButton.RADIUS*2).subtract(15));

        loadActiveProject();
//        System.out.println(screenController.getActiveProject());

        getChildren().add(participantKey);
        getChildren().add(plusButton);
    }

    private void loadActiveProject() {
        entryBar.enableStartup();
        Project project = screenController.getActiveProject();
        List<Entry> entries = project.getEntries();

//        System.out.println(project);

        for(Entry e : entries){
            EntryCell cell = depthLoad(EntryCell.generate(e, null, entryBar));
            entryBar.loadEntry(cell);
//            System.out.println(cell);
        }
        entryBar.finishStartup();
    }

    private EntryCell depthLoad(final EntryCell parent){
//        System.out.println(entry.getEntry());
//        System.out.println("PARENT:" + entry.getParentCell());
//        System.out.println("ENTRY: " + entry);
        for(Entry sub : parent.getEntry().getSubEntries()) {
            depthLoad(EntryCell.generate(sub, parent, entryBar));
        }

        return parent;
    }

    private void addElement(Pane pane, double yOffset) {
        pane.setTranslateY(yOffset);
        getChildren().add(pane);
    }

    public void setButtonFunctions() {
        plusButton.getTopHalf().setOnMouseClicked(MouseEvent -> entryBar.newEntryStart(true));
        plusButton.getBotHalf().setOnMouseClicked(MouseEvent -> entryBar.newEntryStart(false));
    }

    public void exclusiveFocus(Node n){
        this.addEventFilter(MouseEvent.ANY, focusFilter);
    }

    public void relaxFocus() {
        this.removeEventFilter(MouseEvent.ANY, focusFilter);
    }
}