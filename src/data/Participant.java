/**
 * 
 */
package data;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The Participant class represents a single participant of a project.
 * @author Al-John
 *
 */
public class Participant {
	
	private String name;
    private String initials;
	private Color color;
	private String role;
	private List<Entry> assignments;
	public Participant(String name, String initials, Color color, String role) {
		this.name = name;
        this.initials = initials;
		this.color = color;
		this.role = role;
		assignments = new ArrayList<>();
	}
	
	/**
	 * Adds an entry to the Participant's assignments.
	 * @param e the entry to add
	 */
	public void addAssignment(Entry e) { assignments.add(e); }
	
	/**
	 * Removes an entry from the Participant's assignments.
	 * @param e the entry to remove
	 */
	public void removeAssignment(Entry e) { assignments.remove(e); }
	
	/**
	 * @return this user's color
	 */
	public Color getColor() { return color; }
	
	/**
	 * Returns this user's assignments.
	 * @return this user's assignments
	 */
	public List<Entry> getAssignments() 	{ return assignments; }
	
	/**
	 * @return the Participant's name
	 */
	public String getName() { return name; }

    /**
     *
     * @return this Participant's initials
     */
    public String getInitials() { return initials; }

	/**
	 * Sets the user's name.
	 * @param name the name to set
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * @return the Participant's current role
	 */
	public String getRole() { return role; }

	/**
	 * Sets the user's role.
	 * @param role the role to set
	 */
	public void setRole(String role) { this.role = role; }

	/**
	 * Sets the user's color.
	 * @param color the color to set
	 */
	public void setColor(Color color) { this.color = color; }

	public String toString(){
		return name + " (" + color + ", " + role + ")";
	}
}
