package gui.screens;

import data.DateTime;
import data.Project;
import data.Participant;
import gui.GUI;
import gui.controls.PlusButton;
import gui.controls.startup.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
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
 * TODO: - Crossover Time period (checkbox)
 */
public class StartupMenu extends Screen {

//    private double width;
//    private double height;

    private SplitPane mainPane;
    private Button openProjectBtn;
    private Button recentProjectsBtn;
    private Button exitBtn;
    private Button addBtn;

    private PlusButton plusButton;

    private VBox leftSide, rightSide;

    private TextField projectTitleTxt;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private List<ParticipantForm> participantList;

    //TODO: Figure out why it doesn't take up the whole screen... (imaginary margins)
    public StartupMenu() {

        getStyleClass().add("startup-split-pane");
        getStyleClass().add("startupMenu");

        mainPane = new SplitPane();
        openProjectBtn = new Button("Open Project");
        recentProjectsBtn = new Button("Recent Projects");
        exitBtn = new Button("Exit");
        plusButton = new PlusButton(this);

        mainPane.minWidthProperty().bind(GUI.screenController.widthProperty());
        mainPane.minHeightProperty().bind(GUI.screenController.heightProperty());

        projectTitleTxt = new TextField();
        projectTitleTxt.setPromptText("Project Title");
        startDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date (Ideally)"); //TODO: disable dates before start date

        //create participants list
        participantList = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            participantList.add(new ParticipantForm());

        addBtn = new Button("+");

        //crates the left side of the menu
        leftSide = new VBox();
        leftSide.setPadding(new Insets(25));
        leftSide.getChildren().addAll(openProjectBtn,
                recentProjectsBtn,
                exitBtn);
        leftSide.setStyle("-fx-background-color:" + GUI.toRGBCode(Color.TEAL));
        leftSide.setAlignment(Pos.TOP_LEFT);
        leftSide.setSpacing(7);

        //creates the right side of the menu
        rightSide = new VBox();
        rightSide.setStyle("-fx-background-color:" + GUI.toRGBCode(Color.SLATEGRAY));
        rightSide.setPadding(new Insets(15));
//        rightSide.getSubEntries().add(new AttributesPane());
        rightSide.getChildren().addAll(projectTitleTxt,
                startDatePicker,
                endDatePicker);
        rightSide.getChildren().addAll(participantList);
        rightSide.setAlignment(Pos.TOP_LEFT);
        rightSide.setSpacing(7);
        rightSide.getChildren().add(addBtn);

        //adds the left and right sides
        mainPane.getItems().addAll(leftSide, rightSide);

//        this.getSubEntries().add(new BubbleEntry(new Entry("hi", null), 100,200));

        setButtonFunctions();

        getChildren().add(mainPane);
        getChildren().add(plusButton);

        plusButton.translateXProperty().bind(leftSide.widthProperty().subtract(plusButton.widthProperty()));
        plusButton.setTranslateY(300);
        plusButton.setStyle("-fx-background-color:slategray");
        plusButton.combine();
        GUI.setWidth(plusButton, 80);
        GUI.setHeight(plusButton, 80);

//        getSubEntries().add(overlay);
    }

    /**
     * Disables the split pane's divider and positions it properly.
     */
    private void fixDivider() {

        EventHandler<MouseEvent> hack = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lookupAll(".split-pane-divider").stream().forEach(div ->
                        div.setMouseTransparent(true)); //disables divider

                removeEventFilter(MouseEvent.ANY, this); //make sure this only happens once
            }
        };

        this.addEventFilter(MouseEvent.ANY, hack);
        mainPane.setDividerPositions(.25);
    }

    /**
     * Generates a new Project from the fields provided in the StartupMenu. Returns null if any of the fields are
     * invalid, or if there are not enough valid Participants. (If any Participant field is not completely filled out,
     * the creation will skip them and continue to create the Project with the rest of the fields).
     * TODO: DON'T don't just skip the partially filled ones; instead, invalidate the form completely if there are any.
     *
     * @return a new Project from the fields provided in the StartupMenu
     */
    public Project createProjectFromFields() {

        if(isValidForm()) {

            //TODO: Validate form
            Project project = new Project(projectTitleTxt.getText(), new DateTime(startDatePicker.getValue()),
                    new DateTime(endDatePicker.getValue()));

            boolean hasParticipants = false;
            for (ParticipantForm pf : participantList) {
                if(pf.isFilled()) {
                    hasParticipants = true;
                    System.out.println(pf.nameField.getText());
                    project.addParticipants(new Participant(pf.nameField.getText(), pf.initialsField.getText(),
                            pf.colorPicker.getValue(), pf.roleField.getText()));
                }
            }

            if(!hasParticipants) {
                System.out.println("Not enough participants. (Incomplete rows are ignored.)"); //TODO: proper validation
                return null;
            }

            return project;
        } else {
            System.out.println("Invalid form. Missing/malformatted fields.");
            return null;
        }
    }

    /**
     * Validates the Project Title, Start Date, and End Date fields
     * @return true if these fields are not empty or invalid
     */
    private boolean isValidForm() {
        return !projectTitleTxt.getText().isEmpty() &&
                startDatePicker.getValue() != null &&
                endDatePicker.getValue() != null;
    }

    /**
     * Sets the functions for each button in the StartupMenu.
     */
    private void setButtonFunctions() {

        /*
            openProjectBtn;
            recentProjectsBtn;
            exitBtn;
            addBtn;
         */
        exitBtn.setOnMouseClicked((MouseEvent me) -> GUI.exit());

        addBtn.setOnMouseClicked((MouseEvent me) -> {
            final ParticipantForm pf = new ParticipantForm();
            participantList.add(pf);
            rightSide.getChildren().add(rightSide.getChildren().size() -1, pf);
        });

        openProjectBtn.setOnMouseClicked((MouseEvent me) -> openProjectAction());

        recentProjectsBtn.setOnMouseClicked((MouseEvent me) -> {});

        plusButton.getCircle().setOnMouseClicked((MouseEvent me) -> newProjectAction());

        this.setOnScroll((ScrollEvent scroll) -> {
            this.setLayoutY(this.getLayoutY() + scroll.getDeltaY());
        });
    }

    /**
     * Actions performed to load a file from a project.
     */
    private void openProjectAction() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");
        File file = fileChooser.showOpenDialog(GUI.primaryStage);
        if(file != null) {
            System.out.println("You picked \"" + file.getAbsolutePath() + "\". Good choice!");
            //TODO: Do something with the file
        } else {
            System.out.println("...never mind, then.");
        }
    }

    /**
     * Actions performed when a new project is started.
     */
    private void newProjectAction() {
        Project project = createProjectFromFields();
        if(project == null) {
            System.out.println("Can't start. Fix something.");
        }else {
            GUI.screenController.setActiveProject(project);
            GUI.screenController.goTo(ScreenController.OVERVIEW);
        }
    }

    @Override
    public void transitionIn() {
        fixDivider();
        GUI.fade(this, true).play();
    }

    public void populate() {

    }
}
