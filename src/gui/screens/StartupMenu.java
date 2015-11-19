package gui.screens;

import data.DateTime;
import data.Project;
import data.User;
import gui.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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
 * TODO: Login indicator
 * New Project form
 *  - Title
 *  - Start Date
 *  - End Date (Ideally)
 *  - Participants
 *      - Name, Initials, Title
 *  - Crossover Time period
 */
public class StartupMenu extends SplitPane {

//    private double width;
//    private double height;

    private Button openProjectBtn;
    private Button recentProjectsBtn;
    private Button exitBtn;

    private TextField projectTitleTxt;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button startBtn;

    private List<ParticipantForm> participantList;

    public StartupMenu() {
        prefWidthProperty().bind(GUI.primaryStage.widthProperty());
        prefHeightProperty().bind(GUI.primaryStage.heightProperty());
//        this.scaleXProperty().bind(GUI.ZOOM);
//        this.scaleYProperty().bind(GUI.ZOOM);

        getStyleClass().add("startup-split-pane");

        this.setStyle("-fx-background-color:transparent");

        openProjectBtn = new Button("Open Project");
        recentProjectsBtn = new Button("Recent Projects");
        exitBtn = new Button("Exit");

        projectTitleTxt = new TextField();
        projectTitleTxt.setPromptText("Project Title");
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date (Ideally)");

        //create participants list
        participantList = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            participantList.add(new ParticipantForm());

        startBtn = new Button("+");

        //crates the left side of the menu
        VBox leftSide = new VBox();
        leftSide.setPadding(new Insets(100, 50, 50, 50));
        leftSide.getChildren().addAll(openProjectBtn,
                recentProjectsBtn,
                exitBtn);
        leftSide.setStyle("-fx-background-color:blue");
        leftSide.setAlignment(Pos.TOP_RIGHT);
        leftSide.setSpacing(7);

        //creates the right side of the menu
        VBox rightSide = new VBox();
        rightSide.setPadding(new Insets(100, 50, 50, 50));
        rightSide.getChildren().addAll(projectTitleTxt,
                startDatePicker,
                endDatePicker);
        rightSide.getChildren().addAll(participantList);
        rightSide.getChildren().add(startBtn);
        rightSide.setStyle("-fx-background-color:green");
        rightSide.setAlignment(Pos.TOP_LEFT);
        rightSide.setSpacing(7);

        //adds the left and right sides
        getItems().addAll(leftSide, rightSide);

        setButtonFunctions();
    }

    public void fixDivider() {
        lookupAll(".split-pane-divider").stream().forEach(
                div -> div.setMouseTransparent(true) ); //disable divider

        setDividerPositions(.333);
    }

    private class ParticipantForm extends HBox {
        final ColorPicker colorPicker;
        final TextField nameField;
        final TextField initialsField;
        final TextField roleField;

        public ParticipantForm() {
            colorPicker = new ColorPicker(); //TODO: Make a custom color picker (https://community.oracle.com/thread/2318310)
            nameField = new TextField();
            nameField.setPromptText("Name");
            initialsField = new TextField();
            initialsField.setPromptText("Initials");
            roleField = new TextField();
            roleField.setPromptText("Role");

            this.getChildren().addAll(colorPicker, nameField, initialsField, roleField);
            this.setSpacing(7);
        }
    }

    public Project createProjectFromFields() {
        //TODO: Validate form

        Project p = new Project(projectTitleTxt.getText(), new DateTime(startDatePicker.getValue()),
                new DateTime(endDatePicker.getValue()));

        for(ParticipantForm pf : participantList) {
            p.addParticipants(new User(pf.nameField.getText(), pf.colorPicker.getValue(), pf.roleField.getText()));
        }

        return p;
    }

    private void setButtonFunctions() {
        exitBtn.setOnMouseClicked((MouseEvent me) -> GUI.exit());
        startBtn.setOnMouseClicked((MouseEvent me) -> {
        });
    }
}
