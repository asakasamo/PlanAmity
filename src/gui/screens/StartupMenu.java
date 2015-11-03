package gui.screens;

import data.DateTime;
import data.Project;
import data.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Startup Menu. This is the first screen that the user sees when they open the program.
 *
 * LEFT:
 * OpenProject button
 * Recent Projects button
 * Exit button
 *
 * RIGHT:
 * Login indicator
 * New Project form
 *  - Title
 *  - Start Date
 *  - End Date (Ideally)
 *  - Participants
 *      - Name, Initials, Title
 *  - Crossover Time period
 */
public class StartupMenu extends HBox {

    private double width;
    private double height;

    private Button openProjectBtn;
    private Button recentProjectsBtn;
    private Button exitBtn;

    private TextField projectTitleTxt;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button startBtn;

    private List<ParticipantForm> participantList;

    public StartupMenu() {
        width = 700;
        height = 500;

        this.setMaxWidth(width);
        this.setMaxHeight(height);
        this.setStyle("-fx-background-color:orange");

        openProjectBtn = new Button("Open Project");
        recentProjectsBtn = new Button("Recent Projects");
        exitBtn = new Button("Exit");

        projectTitleTxt = new TextField("Project Title");
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        //create participants list
        participantList = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            participantList.add(new ParticipantForm());

        startBtn = new Button("Start Project!");

        //crates the left side of the menu
        VBox leftSide = new VBox();
        leftSide.setPadding(new Insets(5, 5, 5, 5));
        leftSide.setPrefWidth(width / 2);
        leftSide.getChildren().addAll(openProjectBtn,
                recentProjectsBtn,
                exitBtn);

        //creates the right side of the menu
        VBox rightSide = new VBox();
        rightSide.setPadding(new Insets(5, 5, 5, 5));
        rightSide.setPrefWidth(width / 2);
        rightSide.getChildren().addAll(projectTitleTxt,
                startDatePicker,
                endDatePicker);
        rightSide.getChildren().addAll(participantList);
        rightSide.getChildren().add(startBtn);

        //adds the left and right sides
        this.getChildren().addAll(leftSide, rightSide);
    }

    private class ParticipantForm extends HBox {
        final ColorPicker colorPicker;
        final TextField nameField;
        final TextField initialsField;
        final TextField roleField;

        public ParticipantForm() {
            colorPicker = new ColorPicker(); //TODO: Make a custom color picker (https://community.oracle.com/thread/2318310)
            nameField = new TextField("Name");
            initialsField = new TextField("Initials");
            roleField = new TextField("Title");

            clearOnFocus(nameField);
            clearOnFocus(initialsField);
            clearOnFocus(roleField);

            this.getChildren().addAll(colorPicker, nameField, initialsField, roleField);
        }
    }

    /**
     * Adds a listener that clears a specified TextField on its first focus only, then removes the listener.
     * @param tf the specified TextField
     */
    public void clearOnFocus(final TextField tf) {

        final ChangeListener<Boolean> onFocus = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean isFocused) {
                if (isFocused) {
                    tf.setText("");
                    tf.focusedProperty().removeListener(this);
                }
            }};

        tf.focusedProperty().addListener(onFocus);
    }

    public Project createProjectFromFields() {
        Project p = new Project(projectTitleTxt.getText(), new DateTime(startDatePicker.getValue()),
                        new DateTime(endDatePicker.getValue()));

        for(ParticipantForm pf : participantList) {
            p.addParticipants(new User(pf.nameField.getText(), pf.colorPicker.getValue(), pf.roleField.getText()));
        }

        return p;
    }
}
