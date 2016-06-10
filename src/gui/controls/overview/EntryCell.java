package gui.controls.overview;

import data.Entry;
import javafx.application.Platform;
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
    List<ListEntry> subEntries;
    boolean expanded;

    /**
     * Returns the Entry that this cell is representing.
     * @return the Entry that this cell is representing
     */
    public Entry getEntry() {
        return entry;
    }

    /**
     * Sets the Entry assigned to this EntryCell.
     * @param entry the entry
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
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
     * Toggles the expanded status of the EntryCell.
     */
    public void toggle() {
        if(!expanded)
            expand();
        else
            retract();
    }

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

    public void addSubEntry(ListEntry child, ListEntry... insertAfter){
        if(insertAfter.length == 0) subEntries.add(child);
        else subEntries.add(subEntries.indexOf(insertAfter[0]) +1, child);
    }

    /**
     * Returns this EntryCell's children.
     * @return this EntryCell's children
     */
    public List<ListEntry> getSubEntries() {
        return subEntries;
    }

    /**
     * Returns ALL of the subEntries of this entryCell (including all subEntries of its subEntries).
     * @return all of the subEntries
     */
    public List<ListEntry> getAllSubEntries() {
        List<ListEntry> all = new ArrayList<>();
        for(ListEntry sub : subEntries) {
            all.add(sub);
            all.addAll(sub.getAllSubEntries());
        }

        return all;
    }

    /**
     * Generates an EntryCell for a specified Entry. This method also creates EntryCells for all of the Entry's
     * subEntries, and those subEntries can be accessed by a call to <code>getSubEntries()</code>.
     *
     * (Note: The parent-child relationship is set in the constructor of the respective EntryCells; there is NO NEED to
     * set them elsewhere.)
     *
     * @param entry the Entry
     * @param parent the parent EntryCell
     * @param bar the EntryBar that this EntryCell will inhabit
     * @return the generated EntryCell
     */
    public static EntryCell generate(Entry entry, EntryCell parent, EntryBar bar) {
        EntryCell cell;

        if(entry.getParent() == null)
            cell = new BubbleEntry(entry, bar);
        else
            cell = new ListEntry(entry, parent, bar);

        cell.depthLoad();
        return cell;
    }

    /**
     * Loads the full depth of EntryCells for a specified EntryCell. (I.e., loads all of its sub-entries, and all of
     * their sub-entries, and so on.)
     */
    private void depthLoad(){
        for(Entry sub : entry.getSubEntries()) {
            EntryCell subCell = EntryCell.generate(sub, this, entryBar);
            addSubEntry((ListEntry) subCell);
        }
    }

    /**
     * Returns the total number of subEntries.
     * @return the total number of subEntries
     */
    public int totalSubs() {
        int count = subEntries.size();
        for (EntryCell child : subEntries)
            count += child.totalSubs();

        return count;
    }

    /**
     * Handles the cancellation of this EntryCell's creation (i.e. its "death")
     */
    public abstract void die();

    public void setExpanded(boolean status) {
        expanded = status;
    }

    @Override
    public String toString() {
        if(entry == null)
            return  "#dud#";

        String tabs = "";
        int indent = entry.numParents();
        while(indent-- >= 0) tabs += "\t";

        String s = "[Name: " + entry.getName() + "]";
//        s += entry.toString();
        for(EntryCell sub : subEntries)
            s += "\n" + tabs + sub;

        return s;


//        return "[Name: " + entry.getName() + "]";
    }

    public abstract String getEntryName();
}
