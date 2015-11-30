package gui.controls;

import data.Entry;
import gui.GUI;
import gui.screens.ScreenController;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Al-John
 */
public class EntryBar extends Pane {

    private static final double CELL_WIDTH = 120;
    private final double Y_POS = 0;
    private int moving;

    private List<EntryCell> mainEntries;

    public EntryBar() {
        moving = -1;
        mainEntries = new ArrayList<>();

        //timeline bar height
        setTranslateY(50);
        setTranslateX(15);
    }

    public void addNewEntry(Entry e) {
        if(e.getParent() == null) {
            int idx = mainEntries.indexOf(ScreenController.focus);
            if(idx < 0) idx = 0;
            final BubbleEntry bubble = new BubbleEntry(e, getProperX(idx), Y_POS, this);
            mainEntries.add(idx, bubble);
            getChildren().add(bubble);

            for (int i = idx + 1; i < mainEntries.size(); i++) {
                GUI.moveTo(mainEntries.get(i).getRoot(), getProperX(i), Y_POS).play();
            }

            GUI.screenController.setFocus(bubble);
        }
        else {
            ScreenController.focus.expand();
        }
    }

    public static double getProperX(int idx) {
        return idx * CELL_WIDTH;
    }

    /**
     * Returns the cell index that the x position is within
     * @param x the x position
     * @return the cell index that the x position is within
     */
    public int overIdx(double x) {
        int idx = (int)x / (int)CELL_WIDTH;
        if(idx >= mainEntries.size())
            return mainEntries.size() -1;
        if(idx < 0)
            return 0;

        return idx;
    }

    /**
     * Swaps a specified EntryCell with the one in the index specified.
     * @param from the from index
     * @param to the to index
     */
    public void swap(int from, int to) {

        if(from != to){
            EntryCell boss = mainEntries.get(from);
            EntryCell target = mainEntries.get(to);

            Entry.swap(boss.getEntry(), target.getEntry());

            mainEntries.set(to, boss);
            mainEntries.set(from, target);

            fixPositions();

            System.out.println(from + " " + mainEntries.get(from).getEntry().getName());
            System.out.println(to + " " + mainEntries.get(to).getEntry().getName());
        }
    }

    public void setMoving(int idx) {
        moving = idx;
    }

    private void fixPositions() {
        for(int i = 0; i < mainEntries.size(); i++) {
            if(i != moving) GUI.moveTo(mainEntries.get(i).getRoot(), getProperX(i), Y_POS, 200).play();
        }
    }
}
