package gui.controls.general;

import gui.GUI;
import gui.screens.ScreenController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

    public static final double HEIGHT = 20;

    private ScreenController screenController;

    private Button logo;
    private Button save;
    private Button settings;

    private Label appLabel;
    private Rectangle userColor;
    private Label userName;

    private Button help, minimize, maximize, close;

    private HBox projectControls;
    private HBox windowControls;

    public TitleBar(ScreenController screenController) {
        this.screenController = screenController;
        GUI.setHeight(this, HEIGHT);
        this.prefWidthProperty().bind(screenController.widthProperty());

        this.getStyleClass().add("titleBar");

        logo = new IconButton("images/triforce.png");
        save = new IconButton("images/save.png");
        settings = new IconButton("images/settings.png");
        help = new IconButton("images/help.png");
        minimize = new IconButton("images/minimize.png");
        maximize = new IconButton("images/maximize.png");
        close = new IconButton("images/close.png");

        userName = new Label("##Current User##");
        appLabel = new Label("planAmity");

        userColor = new Rectangle(10, 10, Color.LIMEGREEN); //TODO: Actual user's color

        projectControls = new HBox();
        windowControls = new HBox();

        projectControls.getChildren().addAll(logo, save, settings);
        windowControls.getChildren().addAll(minimize, maximize, close);

        anchorToRight(windowControls);
        anchorToCenter(appLabel);
        appLabel.setAlignment(Pos.CENTER);

//        GUI.setHeight(container, HEIGHT);
//        container.setAlignment(Pos.CENTER_RIGHT);
//        container.getChildren().addAll(logo, save, settings, appLabel, userColor, userName, help, minimize,
//                maximize, close);

        setButtonFunctions();
        this.getChildren().addAll(projectControls, windowControls);
    }

    public void setButtonFunctions() {
        logo.setOnMouseClicked(MouseEvent -> screenController.goTo(ScreenController.STARTUP_MENU));
        minimize.setOnMouseClicked(MouseEvent -> GUI.minimize());
        maximize.setOnMouseClicked(MouseEvent -> GUI.maximize());
        close.setOnMouseClicked(MouseEvent -> GUI.exit());
    }

    private class IconButton extends Button {
        final double ICON_WIDTH = 15;
        final double ICON_HEIGHT = 15;
        final double WIDTH = 33;
        final double HEIGHT = 20;

        public IconButton(String imageURL){
            Image img = new Image(imageURL);
            ImageView imgView = new ImageView(img);
            setGraphic(imgView);

            imgView.setFitWidth(ICON_WIDTH);
            imgView.setFitHeight(ICON_HEIGHT);

            GUI.setWidth(this, WIDTH);
            GUI.setHeight(this, HEIGHT);
        }
    }

    private void anchorToRight(Region region) {
        region.translateXProperty().bind(this.widthProperty().subtract(region.widthProperty()));
    }

    private void anchorToCenter(Region region){
        region.translateXProperty().bind(this.widthProperty().divide(2).subtract(region.widthProperty().divide(2)));
    }
}
