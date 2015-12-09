/**
 * Copyright?
 */
package data;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
     * Adds an entry to the project.
     * @param entry the entry to add
     */
    public void addEntry(Entry entry) {
        addEntries(entry);
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
        String s =
                "Project { \n" +
                    "\tName:" + name + ", \n" +
                    "\tstart:" + start + ", \n" +
                    "\tend:" + end + ", \n" +
                    "\t" + entries.size() + " Entries: { \n";
                        for(Entry e : entries) s += "\t\t" + e + "\n";
                s +="\t}\n" +
                    "\t" + participants.size() + " Participants: {\n";
                        for (Participant p : participants) s += "\t\t[" + p + "]; \n";

//        s = s.substring(0, s.length() -2);
        s += "\t}\n}";

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



    /**
     * Generates a sample project. The project includes:
     * - Starts today, ends February 20
     * - 3-8 participants
     * - 3-10 Top-level entries
     *      - with 0-7 sub-entries each
     *          - with 0-6 sub-entries each, etc...
     * - all Entries are empty.
     *
     * @return the sample project
     */
    public static Project sampleProject() {
        Project project = new Project("Sample Project", new DateTime(), new DateTime(2, 20, 2015));
        Color[] colors = {
                Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.PURPLE, Color.SKYBLUE, Color.PINK, Color.ORANGE
        };

        Random r = new Random();

        int par = 3 + r.nextInt(5);            //number of participants
        List<List> ent = enTree(7);            //number of subentries (size of each sub-array)

        List<Participant> participants = new ArrayList<>();

        //generate participants
        for (int i = 0; i < par; i++)
            participants.add(new Participant("Participant" + i, "P" + i, colors[i], "Role?"));
        participants.forEach(project::addParticipants);

        int count = 0;
        for(List l : ent){
            project.addEntry(makeEntry(l, count++, "Entry" + count));
        }

        return project;
    }

    private static List<List> enTree(int subs){
        if(subs < 4) return new ArrayList<>();

        int rand = new Random().nextInt(subs) + (subs == 7 ? 3 : 0);
        List<List> list = new ArrayList<>();
        while(rand-- >= 0){
            list.add(enTree(subs -1));
        }

        return list;
    }

    private static Entry makeEntry (List<List> depth, int idx, String prefix){
        Entry entry = new Entry(prefix, new DateTime(), new DateTime());
        int count = 0;
        for(List l : depth) {
            entry.addSubEntry(makeEntry(l, count++, prefix + "-" + count));
        }
        return entry;
    }
}