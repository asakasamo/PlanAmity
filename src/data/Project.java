/**
 * Copyright?
 */
package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure representing a single project. A Project is the main wrapper for this program,
 * and keeps track a list of entries, as well as its participants.
 * 
 * @author Al-John
 */
public class Project {
	private String name;
	private String description;
	private DateTime start;
	private DateTime end;
	private int duration;
	private List<Entry> entries;
	private List<User> participants;
	
	public Project(String name, String description, DateTime start, DateTime end) {
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		duration = DateTime.minutesBetween(start, end);
		
		entries = new ArrayList<Entry>();
		participants = new ArrayList<User>();
	}

	/**
	 * @return The name of the project
	 */
	public String getName() { return name; }

	/**
	 * @param name The new project name
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * @return the description
	 */
	public String getDescription() { return description; }

	/**
	 * @param description The new project description
	 */
	public void setDescription(String description) { this.description = description; }

	/**
	 * @return The start date of the project
	 */
	public DateTime getStart() { return start; }

	/**
	 * @param start The new start date of the project
	 */
	public void setStart(DateTime start) { this.start = start; }

	/**
	 * @return The end date of the project
	 */
	public DateTime getEnd() { return end; }

	/**
	 * @param end The new end date of the project
	 */
	public void setEnd(DateTime end) { this.end = end; }
	
	/**
	 * Adds (a) participant(s) to the project.
	 * @param users The participants of the project
	 */
	public void addParticipants(User... users){
		for(User u : users){
			this.participants.add(u);
		}
	}
	
	/**
	 * Removes a participant from the project.
	 * @param p the participant to remove
	 */
	public void removeParticipant(User p) { participants.remove(p); }
	
	/**
	 * Adds one or more entries to the project.
	 * @param entries the entry (or entries) to add
	 */
	public void addEntries(Entry... entries){ 
		for(Entry e : entries)
		this.entries.add(e); 
	}
	
	/**
	 * Deletes an entry from the project.
	 * @param e the entry to remove
	 */
	public void deleteEntry(Entry e) { entries.remove(e); }
	
	/**
	 * Switches the positions of two entries, and modifies their start and end dates accordingly.
	 * PRECONDITION: The entries array is sorted by date (i.e. entries[idx1] occurs BEFORE entries[idx2])
	 * @param idx1 index of the first entry
	 * @param idx2 index of the second entry
	 */
	public void swapEntries(int idx1, int idx2){
		Entry e1 = entries.get(idx1);
		Entry e2 = entries.get(idx2);

		//time between start and end, in minutes 
		int gap = DateTime.minutesBetween(e1.getEnd(), e2.getStart());
		
		//set start of the 2nd event to the start of the 1st event
		e2.setStart(e1.getStart(), true);
		//set start of the 1st event to the end of the 2nd event, plus whatever time gap there was
		e1.setStart(DateTime.getLaterDateTime(gap, e2.getEnd()), true);;
		
		entries.set(idx2, e1);
		entries.set(idx1, e2);
	}
	
	/**
	 * 
	 * @return the duration of the project, in minutes
	 */
	public int getDuration() { return duration; }
	
}
