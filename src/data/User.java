/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * The User class represents a single participant of a project.
 * @author Al-John
 *
 */
public class User {
	
	private String name;
	private Color color;
	private String role;
	private List<Entry> assignments;
	public User(String name, Color color, String role) {
		this.name = name;
		this.color = color;
		this.role = role;
		assignments = new ArrayList<Entry>();
	}
	
	/**
	 * Adds an entry to the User's assignments.
	 * @param e the entry to add
	 */
	public void addAssignment(Entry e) { assignments.add(e); };
	
	/**
	 * Removes an entry from the User's assignments.
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
	 * @return the User's name 
	 */
	public String getName() { return name; }

	/**
	 * Sets the user's name.
	 * @param name the name to set
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * @return the User's current role
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
		return name + " (" + role + ")";
	}
}
