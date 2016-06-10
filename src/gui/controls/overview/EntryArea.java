package gui.controls.overview;

import gui.GUI;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for the Entry Area, which is the area in the Overview where expanded Entries are shown.
 *
 * The Entry Area comprises the Side Timeline, and all currently expanded entries.
 *
 * @author Al-John
 */
public class EntryArea extends Pane {

    private final double TL_WIDTH = 42;

    private EntryBar entryBar;

    private Pane sideTimeline;
    private Pane entryArea;
    private IntegerProperty colWidth;
    private Map<EntryCell, EntryColumn> owners;
    private Map<EntryCell, Boolean> isExpanded;
    private List<BubbleEntry> activeBubbles;

    public EntryArea(EntryBar entryBar) {
        this.entryBar = entryBar;
        sideTimeline = new Pane();
        entryArea = new Pane();
        colWidth = new SimpleIntegerProperty();
        owners = new HashMap<>();
        isExpanded = new HashMap<>();
        activeBubbles = new ArrayList<>();

        GUI.setWidth(sideTimeline, TL_WIDTH);
        GUI.bindWidth(entryArea, entryBar.widthProperty().subtract(TL_WIDTH));
        this.getStyleClass().add("entryArea");

        sideTimeline.setPrefHeight(200);
        entryArea.setPrefHeight(200);

        //        sideTimeline.getSubEntries().addAll(new Label("Side\nTL"));
//        sideTimeline.setStyle("-fx-background-color:red");
//        entryArea.setStyle("-fx-background-color:blue");
//        entryBar.setStyle("-fx-background-color:green");

        entryArea.setTranslateX(TL_WIDTH + 10);
        getChildren().add(sideTimeline);
        getChildren().add(entryArea);
    }

    //TODO: This is BAD
    public void expand(EntryCell entry) {
        if(entry instanceof BubbleEntry)
            expand((BubbleEntry)entry);
        else if(entry instanceof ListEntry)
            expand((ListEntry)entry);
    }

    //TODO: This is BAD
    public void retract(EntryCell entry) {
        if(entry instanceof BubbleEntry)
            retract((BubbleEntry)entry);
        else if(entry instanceof ListEntry)
            retract((ListEntry)entry);
    }

    public void expand(BubbleEntry bubble){
        List<BubbleEntry> one = new ArrayList<>();
        one.add(bubble);
        expand(one);
        isExpanded.put(bubble, true);
    }

    public void expand(List<BubbleEntry> entries) {

        entries.sort((BubbleEntry e1, BubbleEntry e2) -> entryBar.indexOf(e1) - entryBar.indexOf(e2));

        activeBubbles = entries;
        entryArea.getChildren().clear();                //clear the current area
        int cols = entries.size();                      //set number of columns
        colWidth.bind(widthProperty().divide(cols));    //set width of columns

        for(int i = 0; i < entries.size(); i++) {                   //for each bubble...
            BubbleEntry bubble = entries.get(i);

            Pane col = new EntryColumn(bubble);                     //...create a column pane
            GUI.bindWidth(col, colWidth);                           //set proper width
            GUI.bindHeight(col, heightProperty());                  //set proper height
            col.translateXProperty().bind(colWidth.multiply(i));    //set proper x position

            entryArea.getChildren().add(col);                       //add column to entry area

            isExpanded.put(bubble, false);                           //set expanded status to true
        }
    }

    public void retract(BubbleEntry bubble){
        activeBubbles.remove(bubble);
        expand(activeBubbles);
        isExpanded.put(bubble, false);
    }

    public void expand(ListEntry listEntry) {
        owners.get(listEntry).expand(listEntry);
        isExpanded.put(listEntry, true);
    }

    public void expandAll(EntryCell cell) {
        if(cell instanceof ListEntry)
            owners.get(cell).expandAll((ListEntry)cell);
        else if(cell instanceof BubbleEntry)
            owners.get(cell).expandAll();
    }

    public void retract(ListEntry listEntry) {
        owners.get(listEntry).retract(listEntry);
        isExpanded.put(listEntry, false);
    }

    public void toggle(EntryCell entry) {
        if(isExpanded.get(entry) == Boolean.TRUE) {
            retract(entry);
            isExpanded.put(entry, false);
        }
        else {
            expand(entry);
            isExpanded.put(entry, true);
        }
    }

    /**
     * Deletes a specified entry from existence.
     * @param entry the entry to delete
     */
    public void delete(ListEntry entry) {
        owners.get(entry).delete(entry);
        owners.remove(entry);
    }

    /**
     * Updates the EntryArea (essentially to show a new ListEntry).
     * @param fresh the new ListEntry
     */
    public void update(ListEntry fresh) {
        owners.get(fresh.getParentCell()).addEntry(fresh);
    }

    private class EntryColumn extends Pane {
        List<ListEntry> visible;
        private BubbleEntry owner;
        final double DASH = 6;

        //populate the column
        public EntryColumn(BubbleEntry bubble) {
            visible = new ArrayList<>();
            owner = bubble;

            owners.put(bubble, this);

            for(ListEntry sub : bubble.getSubEntries()){
                visible.add(sub);

                if(isExpanded.get(sub) == Boolean.TRUE) {
                    expand(sub);                                   //expand properly
                }

                owners.put(sub, this);
            }

            repopulate();
//            generateBranches();
        }

        //TODO: Add a changeListener to visible (instead of rebuilding whole thing)
        public void repopulate() {
            getChildren().clear();
            for(int i = 0; i < visible.size(); i++){
                Node node = visible.get(i);
                node.setTranslateY(i * ListEntry.HEIGHT);
//                getChildren().remove(node);
                getChildren().add(node);

                owners.put(visible.get(i), this);
            }
            generateBranches();
//            generateFunBranches();
        }

        public void generateBranches() {
            if(visible.size() == 0)
                return;

            List<Integer> indents = new ArrayList<>();

            //draw horizontal branches
            for(int i = 0; i < visible.size(); i++) {
                double startx = (visible.get(i).getIndent() * 10) - 10;
                double starty = (ListEntry.HEIGHT * i) + (ListEntry.HEIGHT / 2);
                double endx = startx + DASH;
                double endy = starty;

                Line branch = new Line(startx + .5, starty, endx + .5, endy);
                if(i > 0)
                    branch.getStrokeDashArray().addAll(2d, 2d, 2d);

                getChildren().add(branch);
                indents.add(visible.get(i).getIndent());
            }

            //draw vertical branches
            drawVBranches(0, indents);

            //draw branches to bubbles
            double offset = -10;
            double sx = offset + entryBar.getProperX(owner) - (colWidth.get() * activeBubbles.indexOf(owner)) + .5;
            double sy = BubbleEntry.HEIGHT - EntryBar.HEIGHT + .5;       //bottom of bubble, above the bottom of the area
            double ex = sx;
            double ey = sy + 10;
            Line branchDown = new Line(sx, sy, ex, ey);
            Line branchToSubs = new Line(ex, ey, -10, ey);
            Line branchToFirst = new Line(-10 + .5, ey, -10 + .5, ListEntry.HEIGHT/2);

            setStrokeLineCapButt(branchDown, branchToSubs, branchToFirst);

            /******************************
             * BRANCH POSITION BINDINGS
             * ****************************
             * Starting from the bubble,
             *
             * First branch:
             * - The startX is equal to:
             *      (the owner bubble's translateX)
             *      - (the width of the column)
             *      * (the index of the owner bubble [in the current list of active bubbles])
             *      + (the offset)
             * - The endX is bound to the StartX
             *
             * Second branch:
             * - The startX is bound to the endX of the first branch
             *
             * The rest of it just falls in place.
             *
             ****************/
            branchDown.startXProperty().bind(owner.translateXProperty().
                    subtract(colWidth.intValue() * activeBubbles.indexOf(owner)).add(offset).add(.5));

            branchDown.endXProperty().bind(branchDown.startXProperty());

            branchToSubs.startXProperty().bind(branchDown.endXProperty().add(.5));

            getChildren().addAll(branchDown, branchToSubs, branchToFirst);
        }

        //TODO: INEFFICIENT!!!!!
        private int drawVBranches(int start, List<Integer> indents){
            int end = start + 1;
            for(; end < indents.size(); end++) {
                if(indents.get(start) < indents.get(end)) {
//                    System.out.println(indents.get(start) + " to " + indents.get(end));

                    //if immediately below, draw "hanging" line
                    if(end == start + 1){
                        double sx = (indents.get(end) * 10) - 10 + .5;
                        double sy = (start * ListEntry.HEIGHT) + ListEntry.HEIGHT + .5;
                        double ex = sx;
                        double ey = sy + ListEntry.HEIGHT / 2d;
                        Line hang = new Line(sx, sy, ex, ey);

                        //draw dash
                        Line dash = new Line(sx, ey - .5, sx + DASH - 2, ey - .5);
                        getChildren().addAll(hang, dash);
                    }

                    //move step to next index of same
                    end = drawVBranches(end, indents);
                }
                else if(indents.get(start).equals(indents.get(end))) {
                    //draw line from first to last
                    double startX = (indents.get(start) * 10) - 10 + .5;
                    double startY = (start * ListEntry.HEIGHT) + (ListEntry.HEIGHT / 2d) + .5;
                    double endX = startX;
                    double endY = (end * ListEntry.HEIGHT) + (ListEntry.HEIGHT / 2d);
                    Line branch = new Line(startX, startY, endX, endY);
                    branch.getStrokeDashArray().addAll(1d, 4d);
                    getChildren().add(branch);

                    start = end;
                }
                else if(indents.get(start) > indents.get(end)){
                    return start;
                }
            }
            return end;
        }

        public void setStrokeLineCapButt(Line... lines){
            for(Line l : lines) l.setStrokeLineCap(StrokeLineCap.BUTT);
        }

        public void expand(ListEntry entry){
//            System.out.println(entry);

            int idx = visible.indexOf(entry);                               //index of the current entry
            for(int i = entry.getSubEntries().size() -1; i >= 0; i--){          //for each subEntry,
                ListEntry sub = entry.getSubEntries().get(i);
                if(!visible.contains(sub)) {                                //if not already visible,
                    visible.add(idx + 1, sub);                          //insert it after the previous subEntry
                }

                if(isExpanded.get(sub) == Boolean.TRUE)
                    expand(sub);

                owners.put(sub, this);                                      //map it to the current column
            }

            isExpanded.put(entry, true);                                    //set expanded status to true
            repopulate();                                                   //fix the visual
        }

        public void expandAll() {
            visible = owner.getAllSubEntries();
            for(ListEntry sub : visible)
                isExpanded.put(sub, true);

            repopulate();
        }

        public void expandAll(ListEntry entry){
            visible.addAll(visible.indexOf(entry) + 1, entry.getAllSubEntries());
            repopulate();
        }

        public void retract(ListEntry entry){
            for(ListEntry child : entry.getSubEntries()) {
                int idx = visible.indexOf(child);
                visible.remove(child);

                if(idx > 0) {
                    List<ListEntry> all = entry.getAllSubEntries();
                    while (idx < visible.size() && all.contains(visible.get(idx)))
                        visible.remove(idx);
                }
            }

            isExpanded.put(entry, false);
            repopulate();
        }

        public void delete(ListEntry entry) {
            visible.remove(entry);
            getChildren().remove(entry);
            repopulate();
        }

        public void generateFunBranches() {
            List<Integer> indents = new ArrayList<>();

            //draw horizontal branches
            for(int i = 0; i < visible.size(); i++) {
                double startx = (visible.get(i).getIndent() * 10) - 10;
                double starty = (ListEntry.HEIGHT * i) + (ListEntry.HEIGHT / 2);
                double endx = startx + 5;
                double endy = starty;

                Line branch = new Line(startx, starty, endx, endy);

                getChildren().add(branch);
                indents.add(visible.get(i).getIndent());
            }

            //draw vertical branches
            for(int i = 0; indents.contains(i); i++){    //for each indent value
                Delta fl = firstLast(i, indents);       //get first and last occurrence of indent

                //draw line from first to last
                double startX = (i * 10) - 10;
                double startY = (fl.x * ListEntry.HEIGHT) + (ListEntry.HEIGHT / 2);
                double endX = startX;
                double endY = (fl.y * ListEntry.HEIGHT) + (ListEntry.HEIGHT / 2);
                Line branch = new Line(startX, startY, endX, endY);
                getChildren().add(branch);
            }
        }

        /**
         * Removes all instances of a specified integer from a specified list of integers.
         * @param remove the integer to remove
         * @param list the list
         */
        private void removeAll(int remove, List<Integer> list){
            list.stream().filter(i -> i.equals(remove)).forEach(list::remove);
        }

        /**
         * Returns the first and last occurrence of the given integer in a specified List. within the bounds.
         * @param indent the integer
         * @param indents the list
         * @param bot the lower bound
         * @param top the upper bound
         * @return the first and last instance of the integer.
         */
        private Delta firstLast (int indent, List<Integer> indents, int bot, int top){
            int first = 0, last = 0;
            boolean firstSet = false;

            for(int i = bot; i <= top; i++) {
                if(!firstSet && indents.get(i).equals(indent)) {
                    firstSet = true;
                    first = i;
                }
                if(indents.get(i).equals(indent))
                    last = i;
            }

            return new Delta(first, last);
        }

        /**
         * Returns the first and last occurrence of the given integer in a specified List.
         * @param indent the integer
         * @param indents the list
         * @return the first and last instance of the integer.
         */
        private Delta firstLast (int indent, List<Integer> indents){
            int first = 0, last = 0;
            boolean firstSet = false;

            for(int i = 0; i < indents.size(); i++) {
                if(!firstSet && indents.get(i).equals(indent)) {
                    firstSet = true;
                    first = i;
                }
                if(indents.get(i).equals(indent))
                    last = i;
            }

            return new Delta(first, last);
        }

        public void addEntry(ListEntry fresh) {
            EntryCell parent = fresh.getParentCell();
            if(parent instanceof BubbleEntry) {
                fresh.getParentCell().expand();
            }
            if(parent instanceof ListEntry) {
                retract((ListEntry)fresh.getParentCell());
                expand((ListEntry)fresh.getParentCell());
            }
        }

        private class Delta {
            int x, y;
            Delta (int x, int y) { this.x = x; this.y = y; }

            @Override
            public String toString() {
                return "(" + x + "," + y + ")";
            }
        }
    }
}