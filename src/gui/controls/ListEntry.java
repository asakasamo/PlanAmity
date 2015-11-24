package gui.controls;

import data.Entry;
import gui.GUI;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Graphical representation of any sub-Entry.
 */
public class ListEntry extends Pane implements EntryCell {
    private VBox container;
    private VBox subContainer;
    private Label name;
    private Entry entry;
    private boolean expanded;

    public ListEntry(Entry e, int idx){
        this.entry = e;
        expanded = false;
        container = new VBox(5);
        subContainer = new VBox(5);
        name = new Label(e.getName());
        container.getChildren().add(name);
        this.setStyle("-fx-background-color:" + e.getColor());

        subContainer.setTranslateX(10);
        getChildren().add(container);

        name.setOnMouseClicked(MouseEvent -> {
            if(expanded) retract();
            else expand();

            GUI.screenController.setFocus(this);
            System.out.println(entry.getChildren());
        });
        container.getChildren().add(subContainer);
    }

    @Override
    public Node getRoot() {
        return this;
    }

    @Override
    public Entry getEntry() {
        return entry;
    }

    @Override
    public void expand() {
        retract();
        int i = 0;
        for(Entry e : entry.getChildren()){
            subContainer.getChildren().add(new ListEntry(e, i++));
        }
        expanded = true;
    }

    @Override
    public void retract() {
        subContainer.getChildren().removeAll(subContainer.getChildren());
        expanded = false;
    }
}