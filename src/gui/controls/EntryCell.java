package gui.controls;

import data.Entry;
import javafx.scene.Node;

/**
 * The EntryCell interface represents any visual representation of a single Entry in the interface.
 * @author Al-John
 *
 */
public interface EntryCell {
	Entry getEntry();
    Node getRoot();
    void expand();
    void retract();
}
