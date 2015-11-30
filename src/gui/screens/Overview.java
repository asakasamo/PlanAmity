package gui.screens;

import data.Entry;
import gui.GUI;
import gui.controls.*;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Overview screen. Manages all user interaction with the interface, modifying the Project as
 * necessary.
 *
 * @author Al-John
 *
 */
public class Overview extends Screen {

    private PlusButton plusButton;
    private ParticipantKey participantKey;
    private TimelineBar timelineBar;
    private EntryBar entryBar;
    static NewEntryPopup newEntryPopup;

    private List<BubbleEntry> mainEntries; 					//main entries

    public Overview() {
        newEntryPopup = new NewEntryPopup(this);

        participantKey = new ParticipantKey(this);
        timelineBar = new TimelineBar(this);
        plusButton = new PlusButton();
        entryBar = new EntryBar();

        this.setStyle("-fx-background-color:" + GUI.toRGBCode(Color.CORNSILK));
        mainEntries = new ArrayList<>();

//        plusButton.translateXProperty().bind(GUI.screenController.widthProperty().subtract(
//                plusButton.widthProperty().subtract(50)));
//
//        plusButton.translateYProperty().bind(GUI.screenController.heightProperty().subtract(
//                plusButton.widthProperty().subtract(50)));

        bindToCenter(plusButton);

        this.getChildren().addAll(plusButton, participantKey, timelineBar, entryBar);

        setButtonFunctions();
    }

    private void bindToCenter(Pane p) {
        p.translateXProperty().bind(GUI.screenController.widthProperty().divide(2).subtract(p.getWidth()));
        p.translateYProperty().bind(GUI.screenController.heightProperty().divide(2).subtract(p.getHeight()));
        System.out.println(p.getLayoutX());
    }

    public void setButtonFunctions() {
        this.setOnScroll((ScrollEvent scroll) -> GUI.zoom(scroll.getDeltaY() > 0));

        plusButton.setOnMouseClicked((MouseEvent me) -> System.out.println(ScreenController.activeProject));
        plusButton.getTopHalf().setOnMouseClicked((MouseEvent me) -> triggerAddNewEntry(true));
        plusButton.getBotHalf().setOnMouseClicked((MouseEvent me) -> triggerAddNewEntry(false));
    }

    /**
     * Handles the creation of a new Entry. All creations are done relative to the current focused
     * Entry. If the focus is null, then a new top level Entry is created //TODO: aaand finish this description
     *
     * @param level true if the new Entry should is at the same level as the current focused
     * 		entry; false if the new entry is a child.
     */
    public void triggerAddNewEntry(boolean level) {
        newEntryPopup.setLevel(level);
        this.getChildren().add(newEntryPopup);
    }

    public void createNewEntry(Entry e) {

        Timeline die = GUI.zoomFade(newEntryPopup, false, 200);
        die.setOnFinished(ActionEvent -> this.getChildren().remove(newEntryPopup));
        die.play();

        entryBar.addNewEntry(e);
        ScreenController.activeProject.addEntries(e);
    }

    public void populate() {
        List<Entry> l = ScreenController.activeProject.getEntries();
        int count = l.size();
        for(int i = 0; i < count; i++) {
            entryBar.addNewEntry(l.get(i));
        }
    }
}