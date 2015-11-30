package gui.controls;

import gui.GUI;
import gui.screens.ScreenController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * @author Al-John
 */
public class MenuBar extends Pane {

    private HBox container;

    private Label projectNameLbl;
    private Label dateSpanLbl;

    private Button overviewBtn;
    private Button calendarBtn;
    private Button listBtn;
    private Button timeBtn;

    private Button homeBtn;
    private Button minimizeBtn;
    private Button maximizeBtn;
    private Button closeBtn;

    public MenuBar(Pane parent) {
        this.setPrefHeight(30);
        this.prefWidthProperty().bind(parent.widthProperty());
        this.setStyle("-fx-background-color:gray");

        projectNameLbl = new Label("Project name");
        projectNameLbl.setText(ScreenController.activeProject.getName());
        dateSpanLbl = new Label("(startDate) - (endDate)");
        dateSpanLbl.setText(ScreenController.activeProject.getStart() + " - " +
                ScreenController.activeProject.getEnd());

        overviewBtn = new Button("Overview");
        calendarBtn = new Button("Calendar");
        listBtn = new Button("List View");
        timeBtn = new Button("Time View");

        homeBtn = new Button("Home");
        minimizeBtn = new Button("Minimize");
        maximizeBtn = new Button("Maximize");
        closeBtn = new Button("Close");

        container = new HBox(5);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(projectNameLbl, dateSpanLbl, overviewBtn, calendarBtn, listBtn, timeBtn,
                homeBtn, minimizeBtn, maximizeBtn, closeBtn);

        for(Node n : container.getChildren()) {
            n.getStyleClass().add("menuBar");
        }

        setButtonFunctions();
        this.getChildren().add(container);
    }

    public void setButtonFunctions() {
        overviewBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.OVERVIEW));
        calendarBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.CALENDAR_VIEW));
        listBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.LIST_VIEW));
        timeBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.TIME_VIEW));

        homeBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.STARTUP_MENU));
        minimizeBtn.setOnMouseClicked((MouseEvent me) -> GUI.minimize());
        maximizeBtn.setOnMouseClicked((MouseEvent me) -> GUI.maximize());
        closeBtn.setOnMouseClicked((MouseEvent me) -> GUI.exit());
    }
}
