package gui.controls.overview;

import data.Entry;
import data.ObsList;
import data.Project;
import gui.GUI;
import gui.screens.Overview;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Al-John
 */
public class EntryBar extends Pane {

    public static final double HEIGHT = 59;
    private final double BUBBLE_GAP = 15;
    private final double PAD_L = 24;
    private final double PAD_R = BUBBLE_GAP;

    private boolean startup;

    DoubleProperty bubbleWidth;
    private final double Y_POS = 0;
    private BubbleEntry moving;

    private ObsList<BubbleEntry> bubbles;
    private List<EntryCell> focus;
    private EntryCell prevFocus;
    private Overview overview;

    private List<Line> branches;

    private EntryArea entryArea;

    public EntryBar(Overview overview) {
        this.overview = overview;

        prevFocus = null;
        focus = new ArrayList<>();
        bubbles = new ObsList<>();
        entryArea = new EntryArea(this);
        startup = true;
        branches = new ArrayList<>();

        GUI.bindWidth(this, overview.widthProperty().subtract(PAD_R));
        GUI.setHeight(this, HEIGHT);

        bubbleWidth = new SimpleDoubleProperty(prefWidthProperty().get());

        this.setTranslateX(PAD_L);
//        this.setStyle("-fx-background-color:orange");

        initListeners();
    }

    private void initListeners() {
        //Bar width listener
        this.widthProperty().addListener(ChangeListener -> {
            fixBubbleWidth();
            fixPositions().play();
        });

        //Bubble count listener; readjust bubble size accordingly
        bubbles.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                fixBubbleWidth();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fixBubbleWidth();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                //do nothing
            }
        });
    }

    /**
     * Begins the startup process.
     */
    public void beginStartup() { startup = true; }

    /**
     * Finalizes startup.
     */
    public void endStartup() {
        if(bubbles.size() > 0)
            setFocus(bubbles.get(0));

        generateBranches();

        startup = false;
    }

    private void fixBubbleWidth() {
        bubbleWidth.set(widthProperty().get() / bubbles.size() - BUBBLE_GAP);
    }

    /**
     * Handles the creation of a new Entry. All creations are done relative to the current focused Entry. If the focus
     * is null, then a new top level Entry is created.
     *
     * The process is as follows:
     * - The appropriate Entry representation is created and added to the view, allowing user entry of the Entry name.
     * - When the user hits enter, the Entry is then added to the Project.
     *      - The start date depends on the following Entries.
     *          - If the following Entry is a single point (duration 0),
     *              - find all single point entries adjacent to the focus
     *              - readjust their start time so that the single-point entries are equidistant
     *          - Else
     *              - make the start date of the new entry exactly in between the focus and the next entry
     *
     * @param level true if the new Entry and the current focused entry are level; false if the new entry is a child.
     */
    public void newEntryStart(boolean level) {
        EntryCell focus = getCurrentFocus();
        prevFocus = focus;

        //is a new BubbleEntry or the very first Entry (i.e. a BubbleEntry)
        if( creatingNewBubble(level) ) {
            BubbleEntry fresh = new BubbleEntry(this, bubbleWidth);
            int newIdx = focus == null ? 0 : bubbles.indexOf((BubbleEntry)focus) + 1;
            bubbles.add(newIdx, fresh);

            //branches are wrong now
            removeAllBranches();

            //Add node
            fresh.setOpacity(0);
            getChildren().add(fresh);

            //spawn animation for bubble
            Timeline t = fixPositions();
            t.setOnFinished(ActionEvent -> {
                GUI.fade(fresh, true).play();
                generateBranches();
            });
            t.play();

//            overview.exclusiveFocus(fresh);
            setFocus(fresh);
        }
        else { //adding a ListEntry
            EntryCell parent = level ? focus.getParentCell() : focus;
            ListEntry fresh = new ListEntry(this, parent);

            //if level, insert the subEntry into the parent AFTER the focus
            if(level) parent.addSubEntry(fresh, (ListEntry)focus);
            //else, add the subEntry to the end of the parent
            else parent.addSubEntry(fresh);

            //update EntryArea
            entryArea.update(fresh);

            setFocus(fresh);
        }
    }

    /**
     * Returns true if the current focus is null (there are no Entries), or the level is the same as a BubbleEntry.
     * @param level true if the new Entry and the current focused entry are level; false if the new entry is a child
     * @return true if a new Bubble is being created (determined by the current state of things)
     */
    private boolean creatingNewBubble(boolean level) {
        return focus.isEmpty() || (level && getCurrentFocus() instanceof BubbleEntry);
    }

    /**
     * Returns the current selected EntryCell, or none if there are no EntryCells available. If there are multiple
     * selected, returns the last one that was selected.
     * @return the current selected EntryCell, or null if there are none available.
     */
    public EntryCell getCurrentFocus() {
        return this.focus.isEmpty() ? null : this.focus.get(this.focus.size() -1);
    }

    /**
     * The final stages of creating a new Entry.
     */
    public void newEntryFinish() {

        if(prevFocus == null) {
            getCurrentFocus().setEntry(overview.getScreenController().getActiveProject().addNewEntry(
                    getCurrentFocus().getEntryName()));
        }
        else {
            getCurrentFocus().setEntry(overview.getScreenController().getActiveProject().addNewEntryAfter(
                    prevFocus.getEntry(), getCurrentFocus().getEntryName()));
        }

        getCurrentFocus().expand();
        generateBranches();
    }

    /**
     * Cancels the creation of a new Entry.
     */
    public void newEntryCancel() {
        setFocus(prevFocus);
    }

    /**
     * Deletes a BubbleEntry from the Project.
     * @param bubble the bubble to delete
     */
    public void delete(BubbleEntry bubble) {
        bubbles.remove(bubble);
        getChildren().remove(bubble);

        Timeline fix = fixPositions();
        fix.setOnFinished(ActionEvent -> generateBranches());
        fix.play();
    }

    /**
     * Deletes a ListEntry from the Project.
     * @param entry
     */
    public void delete(ListEntry entry) {
        entryArea.delete(entry);
    }

    /**
     * Handles setting a single EntryCell as the current focus of the application.
     * @param toFocus the EntryCell to focus
     * @return true if the EntryCell is already focused; false otherwise.
     */
    public boolean setFocus(EntryCell toFocus){
//        System.out.println(toFocus);

        boolean alreadyFocused = alreadyFocused(toFocus);

        //if it's multiple bubbles selected, unfocus all of them
        if(focus.size() > 1)
            focus.forEach(EntryCell::retract);

        //all current focus lose focus
        focus.forEach(EntryCell::loseFocus);
        focus.clear();

        focus.add(toFocus);
        toFocus.gainFocus();

        return !alreadyFocused;
    }

    /**
     * Returns true if the specified EntryCell is currently focused; false otherwise.
     * @param cell the cell to check
     * @return true if the cell is focused; false otherwise.
     */
    public boolean alreadyFocused(EntryCell cell){
        return focus.size() == 1 && focus.get(0) == cell;
    }

    public boolean setFocus(ListEntry toFocus){
        boolean alreadyFocused = alreadyFocused(toFocus);

        focus.forEach(EntryCell::loseFocus);
        focus.clear();

        focus.add(toFocus);
        toFocus.gainFocus();

        return !alreadyFocused;
    }

    public boolean setFocus(BubbleEntry toFocus){
        boolean alreadyFocused = alreadyFocused(toFocus);

        if(focus.size() > 1)
            focus.forEach(EntryCell::retract);

        //all current focus lose focus
        focus.forEach(EntryCell::loseFocus);

        focus.clear();
        focus.add(toFocus);

        toFocus.gainFocus();

        return !alreadyFocused;
    }

    public void addFocus(BubbleEntry bubble){
        for(EntryCell cell : focus) {
            if (cell instanceof ListEntry) {
                cell.loseFocus();
                focus.remove(cell);
            }
            if (cell == bubble)
                return;
        }

        focus.add(bubble);
        bubble.gainFocus();

        displayFocusedBubbles();
    }

    public void removeFocus(BubbleEntry bubble) {
        bubble.loseFocus();
        focus.remove(bubble);
        displayFocusedBubbles();
    }

    private void displayFocusedBubbles() {
        List<BubbleEntry> bubbles = focus.stream().map(cell -> (BubbleEntry) cell).collect(Collectors.toList());
        entryArea.expand(bubbles);
    }

    public void focusAllBetween(int start, int end) {
        int i = start < end ? start : end;
        int j = i == start ? end : start;

        for(; i <= j; i++){
            addFocus(bubbles.get(i));
        }
    }

    public void ctrlClick(BubbleEntry bubble) {
        if(focus.contains(bubble))
            removeFocus(bubble);
        else
            addFocus(bubble);
    }

    public void shiftClick(BubbleEntry bubble){
        focusAllBetween(bubbles.indexOf((BubbleEntry)focus.get(focus.size() -1)), bubbles.indexOf(bubble));
    }

    public void altClick(EntryCell entry){
        setFocus(entry);
        entryArea.expandAll(entry);
    }

    public void ctrlAltShiftClick() {
        focusAllBetween(0, bubbles.size() -1);
        for(EntryCell cell : focus)
            entryArea.expandAll(cell);
    }

    public void generateBranches() {
        removeAllBranches();

        for(int i = 0; i < bubbles.size() -1; i++){
            double sx = (i+1) * bubbleWidth.get() + (i*BUBBLE_GAP);
            double sy = BubbleEntry.HEIGHT / 2d + .5;
            double ex = sx + BUBBLE_GAP;
            double ey = sy;

            Line branch = new Line(sx, sy, ex, ey);

            branch.getStrokeDashArray().addAll(2d, 3d, 4d, 3d);
            getChildren().add(branch);
            branches.add(branch);
        }
    }

    private void removeBranches(BubbleEntry bubble) {
        int idx = bubbles.indexOf(bubble);
        if(idx -1 < branches.size() && idx -1 >= 0)
            getChildren().remove(branches.get(idx -1));
        if(idx < branches.size() && idx >= 0)
            getChildren().remove(branches.get(idx));
    }

    private void removeAllBranches() {
        getChildren().removeAll(branches);
        branches.clear();
    }

    public void toggle(EntryCell entry) {
        entryArea.toggle(entry);
    }

    private double cellWidth() {
        return bubbleWidth.get() + BUBBLE_GAP;
    }

    /**
     * Returns the cell index that the x position is within
     * @param x the x position
     * @return the cell index that the x position is within
     */
    public int overIdx(double x) {
        int idx = (int)x / (int)cellWidth();
        if(idx >= bubbles.size())
            return bubbles.size() -1;
        if(idx < 0)
            return 0;

        return idx;
    }

    /**
     * Swaps a specified EntryCell with the one in the index specified.
     * @param to the to index
     */
    public void swap(int to) {

        if(moving != bubbles.get(to)){
            BubbleEntry target = bubbles.get(to);
            int from = bubbles.indexOf(moving);

            bubbles.set(to, moving);
            bubbles.set(from, target);

            Project.swapEntries(target.getEntry(), moving.getEntry());

            Timeline fix = fixPositions();

            fix.setOnFinished(ActionEvent -> {
                generateBranches();
                removeBranches(moving);
            });

            fix.play();
        }
    }

    public void setMoving(BubbleEntry bubble) {
        removeBranches(bubble);
        moving = bubble;
    }

    public void stopMoving() {
        moving = null;
        generateBranches();
    }

    /**
     * Generates an animation that "fixes" the positions of all of the bubbles.
     * @return an animation that fixes the positions of all of the bubbles
     */
    public Timeline fixPositions() {
        Timeline timeline = new Timeline();

        for(int i = 0; i < bubbles.size(); i++) {
            if(bubbles.get(i) != moving) {
                timeline.getKeyFrames().addAll(GUI.moveTo(bubbles.get(i), getProperX(i), Y_POS).getKeyFrames());
            }
        }

        return timeline;
    }

    /**
     * Returns the "proper" x position of a specified index (i.e. the index of the BubbleEntry). Used for positioning
     * BubbleEntries.
     *
     * @param idx the index of the Bubble
     * @return the proper position
     */
    public double getProperX(int idx) {
        return (int)(idx * cellWidth());
    }

    public EntryArea getEntryArea() {
        return entryArea;
    }

    public void openPopup(Entry entry) {
        EntryPopup popup = new EntryPopup(entry);
        popup.setTranslateX(300);
        popup.setTranslateY(300);
        overview.getChildren().addAll(popup);
        popup.spawn();
    }

    /**
     * Returns the proper X position of a given BubbleEntry.
     * @param bubble the BubbleEntry
     * @return the proper X position
     */
    public double getProperX(BubbleEntry bubble){
        return getProperX(bubbles.indexOf(bubble));
    }

    /**
     * Loads a BubbleEntry into the EntryBar.
     * @param bubble the BubbleEntry
     */
    public void loadBubbleEntry(BubbleEntry bubble) {
        getChildren().add(bubble);
        bubbles.add(bubble);
    }

    public int indexOf(BubbleEntry bubble) {
        return bubbles.indexOf(bubble);
    }
}
