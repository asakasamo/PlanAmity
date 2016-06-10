package gui.controls.general;

import gui.GUI;
import gui.screens.ScreenController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/**
 * Controller class for the View Bar, which appears directly below the title bar in every Screen (except the Startup
 * Menu).
 *
 * The View Bar comprises (from left to right):
 * - Project Name Label
 * - Date Range Label (start - end)
 * - View Pane
 *
 * @author Al-John
 */
public class ViewBar extends Pane {

    public static final double HEIGHT = 28;

    private Button overviewBtn;
    private Button calendarBtn;
    private Button listBtn;
    private Button timeBtn;
    private Label projectName;
    private Label dateRange;

    private ViewChooser viewChooser;

    public ViewBar(ScreenController screenController) {
        GUI.setHeight(this, HEIGHT);
        viewChooser = new ViewChooser();

        this.getStyleClass().add("viewBar");
//        this.setStyle("-fx-background-color:blue");
        this.prefWidthProperty().bind(screenController.widthProperty());

        projectName = new Label(screenController.getActiveProject().getName());
        dateRange = new Label(screenController.getActiveProject().getStart() + " - " +
                screenController.getActiveProject().getEnd());

        projectName.getStyleClass().add("projectName");
        dateRange.getStyleClass().add("dateRange");

        anchorToRight(viewChooser);
        anchorToCenter(dateRange);

        dateRange.setTranslateY(5);

        getChildren().addAll(projectName, dateRange, viewChooser);

        setButtonFunctions();
    }

    private class ViewChooser extends Pane {
        final int WIDTH = 118;
        final int HEIGHT = 26;

        HBox topRow;
        HBox botRow;
        VBox container;

        public ViewChooser() {
            getStyleClass().add("viewChooser");

            topRow = new HBox();
            botRow = new HBox();
            container = new VBox();

            overviewBtn = new Button("Overview");
            calendarBtn = new Button("Calendar");
            listBtn = new Button("List");
            timeBtn = new Button("Time View");

            setDims(overviewBtn, calendarBtn, listBtn, timeBtn);
            topRow.getChildren().addAll(overviewBtn, timeBtn);
            botRow.getChildren().addAll(listBtn, calendarBtn);
            container.getChildren().addAll(topRow, botRow);

            Line ns = new Line(WIDTH/2 + .5, 0, WIDTH/2 + .5, HEIGHT);
            Line ew = new Line(0, HEIGHT/2 + .5, WIDTH, HEIGHT/2 + .5);

            Line ns2 = new Line(0.5, 0, 0.5, HEIGHT);
            Line ew2 = new Line(0, 0.5, WIDTH, 0.5);

            Line ns3 = new Line(WIDTH + .5, 0, WIDTH + .5, HEIGHT + .5);
            Line ew3 = new Line(0, HEIGHT + .5, WIDTH, HEIGHT + .5);

            setStrokeLineCapButt(ns, ew, ns2, ew2, ns3, ew3);
            getChildren().addAll(container, ns, ns2, ew, ew2, ns3, ew3);
        }

        void setStrokeLineCapButt(Line... lines) {
            for(Line l : lines)
                l.setStrokeLineCap(StrokeLineCap.BUTT);
        }

        private void setDims(Button... buttons){
            for(Button b : buttons){
                GUI.setWidth(b, WIDTH / 2);
                GUI.setHeight(b, HEIGHT / 2);
            }
        }
    }

    public void setButtonFunctions() {
        overviewBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.OVERVIEW));
        calendarBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.CALENDAR_VIEW));
        listBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.LIST_VIEW));
        timeBtn.setOnMouseClicked((MouseEvent me) -> GUI.screenController.goTo(ScreenController.TIME_VIEW));
    }

    private void anchorToRight(Region region) {
        region.translateXProperty().bind(this.widthProperty().subtract(region.widthProperty()));
    }

    private void anchorToCenter(Region region){
        region.translateXProperty().bind(this.widthProperty().divide(2).subtract(region.widthProperty().divide(2)));
    }
}
