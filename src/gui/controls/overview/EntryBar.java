package gui.controls.overview;

import data.Entry;
import data.ObsList;
import gui.GUI;
import gui.screens.Overview;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

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
    private final double PAD_L = 24;
    private final double BUBBLE_GAP = 15;
    private final double PAD_R = BUBBLE_GAP;

    private boolean startup;

    DoubleProperty bubbleWidth;
    private final double Y_POS = 0;
    private EntryCell moving;

    private ObsList<BubbleEntry> bubbles;
    private List<EntryCell> focus;
    private EntryCell prevFocus;
    private Overview overview;

    private EntryArea entryArea;

    public EntryBar(Overview overview) {
        prevFocus = null;
        focus = new ArrayList<>();
        bubbles = new ObsList<>();
        this.overview = overview;
        entryArea = new EntryArea(this);
        startup = false;

        GUI.bindWidth(this, overview.widthProperty().subtract(PAD_R));
        GUI.setHeight(this, HEIGHT);

        this.setTranslateX(PAD_L);
//        this.setStyle("-fx-background-color:orange");

        bubbleWidth = new SimpleDoubleProperty(widthProperty().get());

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

    public void enableStartup () { startup = true; }
    public void finishStartup () { startup = false;}

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
     * @param level true if the new Entry is at the same level as the current focused entry; false if the new entry is a
     *              child.
     */
    public void newEntryStart(boolean level) {
        EntryCell focus = this.focus.isEmpty() ? null : this.focus.get(this.focus.size() -1);

        //is first entry OR a new BubbleEntry
        if(focus == null || (level && focus.getParentCell() == null)) {
            //spawn entry bubble
            BubbleEntry fresh = new BubbleEntry(this, bubbleWidth);
            fresh.setOpacity(0);
            getChildren().add(fresh);
            bubbles.add(fresh);

            Timeline t = fixPositions();
            t.setOnFinished(ActionEvent -> GUI.fade(fresh, true).play());
            t.play();

//            overview.exclusiveFocus(fresh);
            setFocus(fresh);
        }
        else { //adding a ListEntry
            EntryCell parent = level ? focus.getParentCell() : focus;
            ListEntry fresh;

            //if level, INSERT child into parent subEntry list
            if(level) fresh = new ListEntry(this, parent, focus);
            //else, ADD child to subEntry list
            else fresh = new ListEntry(this, parent);

            entryArea.invalidate(parent);
            parent.expand();

//            overview.exclusiveFocus(fresh);
            setFocus(fresh);
        }
    }

    public void newEntryFinish(final BubbleEntry fresh, boolean keep) {
        if(keep) {
            setFocus(fresh);
        } else{
            Timeline disappear = GUI.fade(fresh, false);
            disappear.setOnFinished(ActionEvent -> {
                bubbles.remove(fresh);
                getChildren().remove(fresh);
                fixPositions().play();
            });
            disappear.play();
//            setFocus(focus);
        }

        overview.relaxFocus();
    }

    public void newEntryFinish(final ListEntry fresh, boolean keep){
        if(keep) {
            setFocus(fresh);
        } else{
            fresh.getParentCell().getSubEntries().remove(fresh);
            entryArea.delete(fresh);

            entryArea.invalidate(prevFocus);
            setFocus(prevFocus);
        }

        overview.relaxFocus();
    }

    public void setFocus(EntryCell cell){
        if(startup) {
            entryArea.invalidate(cell);
            entryArea.expand(cell);
            return;
        }

        for(EntryCell e : focus) {                                          //for all currently focused elements,
            e.loseFocus();                                                  //remove their focus (visually)
            if(e instanceof BubbleEntry && cell instanceof BubbleEntry)     //if going from bubble to bubble,
                e.retract();                                                //retract the previous bubble

            if(prevFocus != e)
                prevFocus = e;
        }

        focus.clear();          //remove all focused elements
        focus.add(cell);        //add the new focus

        cell.gainFocus();       //gain focus (visually)
        toggle(cell);           //toggle the expanded status of the new focus
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

        //convert to a list of bubbles
        List<BubbleEntry> bubbles = focus.stream().map(cell -> (BubbleEntry) cell).collect(Collectors.toList());
        entryArea.expand(bubbles);
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
            int from = bubbles.indexOf((BubbleEntry)moving);

            bubbles.set(to, (BubbleEntry)moving);
            bubbles.set(from, target);

            moving.retract();
            fixPositions().play();

//            System.out.println(from + " " + bubbles.get(from).getEntry().getName());
//            System.out.println(to + " " + bubbles.get(to).getEntry().getName());
        }
    }

    public void setMoving(EntryCell cell) {
        moving = cell;
    }

    public Timeline fixPositions() {
        Timeline timeline = new Timeline();
        for(int i = 0; i < bubbles.size(); i++) {
            if(bubbles.get(i) != moving) timeline.getKeyFrames().addAll(
                    GUI.moveTo(bubbles.get(i), getProperX(i), Y_POS, 200).getKeyFrames());
        }

        System.out.println("Job done!");

        return timeline;
    }

    /**
     * Returns the "proper" x position of the given index, in the context of the EntryBar. Used for positioning
     * EntryBubbles.
     *
     * @param idx the index (i.e. of the Bubble in question)
     * @return the proper position
     */
    public double getProperX(int idx) {
        return idx * cellWidth();
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

    public double getProperX(BubbleEntry bubble){
        return getProperX(bubbles.indexOf(bubble));
    }

    public void loadEntry(EntryCell cell) {
//        System.out.println(cell);

        if(cell instanceof BubbleEntry) {
            getChildren().add(cell);
            newEntryFinish((BubbleEntry) cell, true);
            bubbles.add((BubbleEntry)cell);
        }
        else if (cell instanceof ListEntry) {
//            entryArea.invalidate(cell.getParentCell());
//            cell.getParentCell().expand();
//            newEntryFinish((ListEntry) cell, true);
        }

    }

    public int indexOf(BubbleEntry bubble) {
        return bubbles.indexOf(bubble);
    }
}
