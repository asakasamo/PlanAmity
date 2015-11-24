/**
 * Copyright?
 */
package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data structure representing a single project. A Project is the main wrapper for this program,
 * and keeps track a list of entries, as well as its participants.
 * 
 * @author Al-John
 */
public class Project {
	private String name;
	private DateTime start;
	private DateTime end;
	private int duration;
	private List<Entry> entries;
	private List<Participant> participants;

    public Project() {
        this("##BLANK PROJECT##", new DateTime(), new DateTime());
    }
	
	public Project(String name, DateTime start, DateTime end) {
		this.name = name;
		this.start = start;
		this.end = end;
		duration = DateTime.minutesBetween(start, end);
		
		entries = new ArrayList<>();
		participants = new ArrayList<>();
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
	 * @param participants The participants of the project
	 */
	public void addParticipants(Participant... participants){
        Collections.addAll(this.participants, participants);
	}
	
	/**
	 * Removes a participant from the project.
	 * @param p the participant to remove
	 */
	public void removeParticipant(Participant p) { participants.remove(p); }
	
	/**
	 * Adds one or more entries to the project.
	 * @param entries the entry (or entries) to add
	 */
	public void addEntries(Entry... entries){ 
		Collections.addAll(this.entries, entries);
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
		e1.setStart(e2.getEnd().getLaterDateTime(gap), true);

		entries.set(idx2, e1);
		entries.set(idx1, e2);
	}
	
	/**
	 * 
	 * @return the duration of the project, in minutes
	 */
	public int getDuration() { return duration; }

    @Override
    public String toString() {
        String s = "Project { \n\tName:" + name + ", \n\t" +
                "start:" + start + ", \n\t" +
                "end:" + end + ", \n\t" +
                entries.size() + " Entries, \n\t" +
                participants.size() + " Participants: {  ";

        for (Participant p : participants) {
            s += "[" + p + "]; ";
        }
        s = s.substring(0, s.length() -2);
        s += "  }\n}";

        return s;
    }

    /**
     * Returns this Project's participants.
     * @return this Project's participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
