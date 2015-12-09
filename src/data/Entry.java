package data;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * The Entry class is a data structure representing a single entry in a project. Every project
 * is essentially a collection of Entries.
 * @author Al-John
 *
 */
public class Entry {
	private String name;
	private String description;

	private DateTime start;
	private DateTime end;
	private int duration;
	private int percentComplete;

	private Entry parent;
	private List<Entry> subEntries;

	private Participant assignedTo;
	private Participant lastModifiedBy;
	private List<Object> attachments;

	/**
	 * Creates an empty Entry with a specified name. The other attributes are set to null, and
	 * the start and end time are set to the moment of creation.
	 * @param name the Entry's name
	 */
	public Entry(String name, DateTime start, DateTime end){
		this.name = name;
		description = "";

		parent = null;
		subEntries = new ArrayList<>();

		this.start = start;
		this.end = end;
		duration = 0;
		percentComplete = 0;

		assignedTo = null;
		lastModifiedBy = null;
		attachments = new ArrayList<>();
	}

	/**
	 * Creates an empty entry, with its name and parent Entry specified.
	 * @param name the Entry's name
	 * @param parent the Entry's parent Entry
	 */
	public Entry(String name, DateTime start, DateTime end, Entry parent){
		this(name, start, end);
		this.parent = parent;
        if(parent != null) parent.subEntries.add(this);
	}

	/**
	 * @return This entry's parent entry, or null if it is a top level entry.
	 */
	public Entry getParent(){ return parent; }

	/**
	 * @return This entry's child entries.
	 */
	public List<Entry> getSubEntries() { return subEntries; }

	/**
	 * @return This entry's name.
	 */
	public String getName() { return name; }

	/**
	 * Assigns the entry to a specified user.
	 * @param u the user to assign the entry to
	 */
	public void assignTo(Participant u) {
		assignedTo = u; 
		u.addAssignment(this);
	}

	/**
	 * @return the color associated with this Participant.
	 */
	public Color getColor() {
		if(assignedTo == null)
			return Color.INDIANRED;
		else
			return assignedTo.getColor();
	}

	/**
	 * Returns the user this entry is assigned to.
	 * @return the user this entry is assigned to
	 */
	public Participant getAssignedTo() { return assignedTo; }

	/**
	 * 
	 * @return the duration of this entry, in hours.
	 */
	public long getDuration() { return duration; }

	/**
	 * Sets the duration of this entry, then modifies its end date accordingly.
	 * @param dur the duration of this entry, in hours
	 */
	public void setDuration(int dur){
		this.duration = dur;
		end = start.getLaterDateTime(dur);
	}

	/**
	 * Sets the start DateTime of this entry. 
	 * 	If keepDur is true, then modifies the end DateTime accordingly.
	 * 	If keepDur is false, then modifies the duration accordingly.
	 * @param start the new start DateTime
	 * @param keepDur true if the duration is to be preserved; false otherwise
	 */
	public void setStart(DateTime start, boolean keepDur){
		this.start = start;
		if(keepDur)	end = start.getLaterDateTime(duration);
		else this.duration = DateTime.minutesBetween(start, end);
	}

	/**
	 * Sets the end DateTime of this entry, then modifies its duration accordingly.
	 * @param end the new end DateTime
	 */
	public void setEnd(DateTime end){
		this.end = end;
		this.duration = DateTime.minutesBetween(start, end);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Changes the Entry's description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public Participant getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the Participant that last modified this Entry
	 */
	public void setLastModifiedBy(Participant lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the start DateTime of the project
	 */
	public DateTime getStart() {
		return start;
	}

	/**
	 * @return the end DateTime of the project
	 */
	public DateTime getEnd() {
		return end;
	}

	/**
	 * @param name the new name of the project
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parent the new parent Entry, or null if it is a top level Entry
	 */
	public void setParent(Entry parent) {
		this.parent = parent;
	}

	/**
	 * @param assignedTo the Participant this Entry is assigned to
	 */
	public void setAssignedTo(Participant assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * @return the percentage of the entry that is complete
	 */
	public int getPercentComplete() {
		return percentComplete;
	}

	/**
	 * @param percentComplete the new value of percentComplete
	 */
	public void setPercentComplete(int percentComplete) {
		this.percentComplete = percentComplete;
	}

	/**
	 * @return the attachments
	 */
	public List<Object> getAttachments() {
		return attachments;
	}

	/**
	 * Returns true if the Entry has any attachments. TODO: MAKE THIS NOT RANDOM
	 * @return true if the Entry has any attachments, false otherwise
	 */
	public boolean hasAttachments() {
		return Math.random() > .5;
	}

    public String toString() {
        String tabs = "";
        for(int i = numParents(); i >= 0; i--) tabs += "\t";

        String s = tabs + "[Name: " + name + "; start:" + start + "; end:" + end + "; " + subEntries.size() + " SubEntries: {";
        for(Entry e : subEntries) {
            s += "\n" + tabs + e;
        }

        return s + "}";
    }

    public int numParents() {
        if(parent == null)
            return 0;

        return 1 + parent.numParents();
    }

    public static void swap(Entry e1, Entry e2) {
        DateTime tmp = e2.getStart();
        e2.setStart(e1.getStart(), true);
        e1.setStart(tmp, true);
    }

    public void addSubEntry(Entry sub) {
        sub.setParent(this);
        subEntries.add(sub);
    }
}
