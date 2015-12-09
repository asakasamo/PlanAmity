package gui.controls;

import gui.GUI;
import gui.screens.ScreenController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Controller class for the Title Bar, which appears at the very top of every Screen (except the Startup Menu).
 *
 * The Title Bar comprises (from left to right):
 * - planAmity logo
 * - "save" icon
 * - "print" icon
 * - "settings" button
 * - "planAmity" text
 * - current user
 *      - color box
 *      - Name
 * - "help" icon
 * - "minimize" icon
 * - "restore" icon
 * - "close" icon
 *
 * @author Al-John
 */
public class TitleBar extends Pane {

    public final double HEIGHT = 20;

    private ScreenController screenController;

    private ImageView logo;
    private Button save, print, settings;
    private Label appLabel;
    private Rectangle userColor;
    private Label userName;
    private Button help, minimize, maximize, close;

    private HBox container;

    private Label projectNameLbl;
    private Label dateSpanLbl;

    public TitleBar(ScreenController screenController) {
        this.screenController = screenController;
        GUI.setHeight(this, HEIGHT);
        this.prefWidthProperty().bind(screenController.widthProperty());

        this.setStyle("-fx-background-color:lightgray");
        this.getStyleClass().add("titleBar");

        logo = new ImageView("logo.png");
        logo.setFitWidth(18);
        logo.setFitHeight(18);

        save = new Button("Save");
        print = new Button("Print");
        settings = new Button("Settings");
        appLabel = new Label("planAmity");

        userColor = new Rectangle(10, 10, Color.LIMEGREEN); //TODO: Actual user's color
        userName = new Label("##Current User##");

        help = new Button("Help");
        minimize = new Button("Minimize");
        maximize = new Button("Maximize");
        close = new Button("Close");

        container = new HBox(5);
        GUI.setHeight(container, HEIGHT);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(logo, save, print, settings, appLabel, userColor, userName, help, minimize,
                maximize, close);

        projectNameLbl = new Label("Project name");
        projectNameLbl.setText(screenController.getActiveProject().getName());
        dateSpanLbl = new Label("(startDate) - (endDate)");
        dateSpanLbl.setText(screenController.getActiveProject().getStart() + " - " +
                screenController.getActiveProject().getEnd());

        setButtonFunctions();
        this.getChildren().add(container);
    }

    public void setButtonFunctions() {
        logo.setOnMouseClicked(MouseEvent -> screenController.goTo(ScreenController.STARTUP_MENU));
        minimize.setOnMouseClicked(MouseEvent -> GUI.minimize());
        maximize.setOnMouseClicked(MouseEvent -> GUI.maximize());
        close.setOnMouseClicked(MouseEvent -> GUI.exit());
    }
}
