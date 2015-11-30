package gui.screens;

import gui.GUI;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;

/**
 * A View is the container for the elements of a specific context of the program. Subclasses of the View interface
 * must specify actions that are taken when the View transitions into or out of the main focus, as well as handle
 * Project updates.
 */
public abstract class Screen extends Pane {
    /**
     * Designates the actions performed when transitioning into this screen.
     */
    public void transitionIn() {
        populate();
        GUI.fade(this, true).play();
    }

    /**
     * Designates the actions performed when transitioning out of this screen.
     */
    public void transitionOut() {
        Timeline out = GUI.fade(this, false);
        out.setOnFinished((ActionEvent remove) -> GUI.screenController.getChildren().remove(this));
        out.play();
    }

    /**
     * Populates the screen based on the current active project.
     */
    public abstract void populate();
}