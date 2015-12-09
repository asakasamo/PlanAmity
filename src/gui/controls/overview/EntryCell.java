package gui.controls.overview;

import data.Entry;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * The EntryCell interface represents any visual representation of a single Entry in the interface.
 * @author Al-John
 *
 */
public abstract class EntryCell extends Pane {
    Entry entry;
    EntryCell parent;
    EntryBar entryBar;
    List<EntryCell> subEntries;

    /**
     * Returns the Entry that this cell is representing.
     * @return the Entry that this cell is representing
     */
    public Entry getEntry() {
        return entry;
    }

    /**
     * Handles the (visual) expansion of the EntryCell.
     */
    public abstract void expand();

    /**
     * Handles the (visual) retraction of the EntryCell.
     */
    public abstract void retract();

    /**
     * Returns the parent of this EntryCell, or none if it is a top-level Entry (i.e. a BubbleEntry)
     * @return the parent of this EntryCell
     */
    public EntryCell getParentCell() {
        return parent;
    }

    /**
     * Handles the EntryCell's visual representation of being focused.
     */
    public void gainFocus() {
        getStyleClass().add("focused");
    }

    /**
     * Handles the EntryCell's visual representation of losing focus.
     */
    public void loseFocus() {
        getStyleClass().remove("focused");
    }

    /**
     * Adds a child to this EntryCell.
     * @param child the child
     * @param insertAfter the child after which to insert this cell (if not provided, adds to end of list)
     */

    public void addSubEntry(EntryCell child, EntryCell... insertAfter){
        if(insertAfter.length == 0) subEntries.add(child);
        else subEntries.add(subEntries.indexOf(insertAfter[0]) +1, child);
    }

    /**
     * Returns this EntryCell's children.
     * @return this EntryCell's children
     */
    public List<EntryCell> getSubEntries() {
        return subEntries;
    }

    /**
     * Returns ALL of the subEntries of this entryCell (including all subEntries of its subEntries).
     * @return all of the subEntries
     */
    public List<EntryCell> getAllSubEntries() {
        List all = new ArrayList<>();
        all.addAll(subEntries);
        for(EntryCell sub : subEntries)
            all.addAll(sub.getAllSubEntries());

        return all;
    }

    public static EntryCell generate(Entry e, EntryCell parent, EntryBar bar) {
        if(e.getParent() == null)
            return new BubbleEntry(e, bar);
        else
            return new ListEntry(e, parent, bar);
    }

    public int totalSubs() {
        int count = subEntries.size();
        for (EntryCell child : subEntries)
            count += child.totalSubs();

        return count;
    }

    public String toString() {
        String tabs = "";
        int indent = entry.numParents();
        while(indent-- >= 0) tabs += "\t";

        String s = "[Name: " + entry.getName() + "]";
//        s += entry.toString();
        for(EntryCell sub : subEntries)
            s += "\n" + tabs + sub;

        return s;
    }
}
