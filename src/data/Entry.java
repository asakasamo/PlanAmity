package data;

import java.util.ArrayList;
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
	private ArrayList<Entry> children;

	private User assignedTo;
	private User lastModifiedBy;
	private ArrayList<Object> attachments;
	
	public Entry(String name){
		this.name = name;
		description = "";
		
		parent = null;
		children = new ArrayList<Entry>();
		
		start = new DateTime();
		end = new DateTime();
		duration = 0;
		percentComplete = 0;

		assignedTo = null;
		lastModifiedBy = null;
		attachments = new ArrayList<Object>();
	}
	
	public Entry(String name, Entry parent){
		this(name);
		this.parent = parent;
		parent.children.add(this);
	}
	
	/**
	 * @return This entry's parent entry, or null if it is a top level entry.
	 */
	public Entry getParent(){ return parent; }
	
	/**
	 * @return This entry's child entries.
	 */
	public ArrayList<Entry> getChildren() { return children; }
	
	/**
	 * @return This entry's name.
	 */
	public String getName() { return name; }
	
	/**
	 * Assigns the entry to a specified user.
	 * @param u the user to assign the entry to
	 */
	public void assignTo(User u) { 
		assignedTo = u; 
		u.addAssignment(this);
	}
	
	/**
	 * @return the color associated with this User.
	 */
	public Color getColor() {
		if(assignedTo == null)
			return Color.GRAY;
		else
			return assignedTo.getColor();
	}
	
	/**
	 * Get the user this entry is assigned to.
	 * @return
	 */
	public User getAssignedTo() { return assignedTo; }
	
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
		end = DateTime.getLaterDateTime(dur, start);
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
		if(keepDur)	end = DateTime.getLaterDateTime(duration, start);
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
	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the User that last modified this Entry
	 */
	public void setLastModifiedBy(User lastModifiedBy) {
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
	 * @param assignedTo the User this Entry is assigned to
	 */
	public void setAssignedTo(User assignedTo) {
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
	public ArrayList<Object> getAttachments() {
		return attachments;
	}
}
