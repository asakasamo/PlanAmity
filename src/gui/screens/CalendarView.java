package gui.screens;

import javafx.scene.control.Label;

/**
 * @author Al-John
 */
public class CalendarView extends Screen  {
    public CalendarView() {
        Label label = new Label("Calendar View");
        label.setTranslateX(50);
        label.setTranslateY(100);
        getChildren().add(label);

        setStyle("-fx-background-color:blue;-fx-fill:white");
    }

    public void populate() {

    }
}
