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
     * Adds an entry to the project after a specified entry.
     * @param toAdd the entry to add
     * @param after the entry after which to add it
     */
    public void addEntryAfter(Entry toAdd, Entry after){
        entries.add(entries.indexOf(after) +1, toAdd);
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
     * @param numEntries the number of entries
     * @return the sample project
     */
    public static Project randomProject(int numEntries) {
        Project project = new Project("Sample Project", new DateTime(1,1,2016), new DateTime(1,31,2016));
        Color[] colors = {
                Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.PURPLE, Color.SKYBLUE, Color.PINK, Color.ORANGE
        };

        Random r = new Random();

        int par = 3 + r.nextInt(5);            //number of participants
        List<List> ent = enTree(numEntries);   //number of subentries (size of each sub-array)

        List<Participant> participants = new ArrayList<>();

        //generate participants
        for (int i = 0; i < par; i++)
            participants.add(new Participant("Participant" + i, "P" + i, colors[i], "Role?"));
        participants.forEach(project::addParticipants);

        int count = 0;
        for(List l : ent){
            project.addEntry(makeEntry(l, count++, "Entry" + count));
        }

//        System.out.println(project + "\n\n ================================================ \n\n");
        return project;
    }

    /**
     * Random Project generator helper
     * @param subs number of subEntries
     * @return a List used to represent the Project hierarchy
     */
    private static List<List> enTree(int subs){
        if(subs == 0) return new ArrayList<>();

        int rand = subs; //new Random().nextInt(subs) + (subs == 7 ? 3 : 0);
        List<List> list = new ArrayList<>();
        while(rand-- >= 0){
            list.add(enTree(subs -1));
        }

        return list;
    }

    private static Entry makeEntry (List<List> depth, int idx, String prefix){
        Entry entry = new Entry(prefix, new DateTime(), new DateTime());
        int count = 0;
        for(List<List> l : depth) {
            entry.addSubEntry(makeEntry(l, count++, prefix + "." + count));
        }
        return entry;
    }

    /**
     * Adds a new Entry to the end of the project.
     * @param name the name of the Entry
     * @return the new Entry that was added
     */
    public Entry addNewEntry(String name) {
        if(entries.isEmpty()) {
            DateTime date = DateTime.getDateBetween(start, end);
            Entry fresh = new Entry(name, date, date);
            entries.add(fresh);
            return fresh;
        }
        else
            return addNewEntryAfter(entries.get(entries.size() -1), name);
    }

    /**
     * Adds a new Entry with a specified name after a specified Entry.
     * @param after the Entry after which to add the new Entry
     * @param name the name of the new entry
     */
    public Entry addNewEntryAfter(Entry after, String name){
        Entry fresh = new Entry(name);
        this.addEntryAfter(fresh, after);

        List<Entry> pointEntryGroup = this.getGroupOfSinglePointEntries(fresh);
        int groupSize = pointEntryGroup.size();
        int groupStartIdx = entries.indexOf(pointEntryGroup.get(0)) -1;
        int groupEndIdx = groupStartIdx + groupSize + 1;

        DateTime upper = groupStartIdx >= 0 ? entries.get(groupStartIdx).getEnd()           :   start;
        DateTime lower = groupEndIdx < entries.size() ? entries.get(groupEndIdx).getStart() :   end;

//        System.out.println(pointEntryGroup);
//        System.out.println("\n======================\n");

        List<DateTime> startDates = DateTime.getDatesDistributedEvenlyBetween(upper, lower, groupSize);

        for(int i = 0; i < pointEntryGroup.size(); i++)
            pointEntryGroup.get(i).setStart(startDates.get(i), true);

        return fresh;
    }

    /**
     * Returns the Entry at the specified index. The Project is always sorted by date.
     * @param idx the index
     * @return the Entry
     */
    public Entry getEntry(int idx){
        if(idx < 0 || idx >= entries.size())
            throw new IndexOutOfBoundsException("Index out of bounds.");

        return entries.get(idx);
    }

    /**
     * Returns a List representing a specified target Entry's corresponding "group" of single-point Entries (i.e. all of
     * the single-point Entries adjacent to it).
     *
     * @param target the target Entry
     * @return all adjacent single-point Entries
     */
    public List<Entry> getGroupOfSinglePointEntries(Entry target) {
        List<Entry> adjacentPoints = new ArrayList<>();
        int start = entries.indexOf(target) -1;
        int endIdx = entries.indexOf(target) +1;

        while(start >= 0 && entries.get(start).isSinglePoint()) {
            adjacentPoints.add(0, entries.get(start));
            start--;
        }

        if(target.isSinglePoint())
            adjacentPoints.add(target);

        while(endIdx < entries.size() && entries.get(endIdx).isSinglePoint()) {
            adjacentPoints.add(entries.get(endIdx));
            endIdx++;
        }

        return adjacentPoints;
    }

    /**
     * Swaps two entries, as such:
     * - The gap between the two entries is preserved.
     *
     *   123456789ABCDEFGHI       123456789ABCDEFGHI
     *   --[-----]---[--]--  >>>  --[--]---[-----]--
     *     X     Y   x  y           x  y   X     Y
     *
     * - The start of the 2nd entry becomes the start of the first entry
     * - The start of the 1st entry becomes the end of the 2nd entry + the gap
     *
     * Easiest code I have ever written
     * TODO: not static
     *
     * @param e1 the first Entry
     * @param e2 the second Entry
     */
    public static void swapEntries(Entry e1, Entry e2) {
        DateTime e1Start = e1.getStart();
        DateTime e2End = e2.getEnd();

        e2.setStart(e1Start, true);
        e1.setEnd(e2End, true);
    }
}