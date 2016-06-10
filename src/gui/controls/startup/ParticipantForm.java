package gui.controls.startup;

/**
 * @author Al-John
 */

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * A single line in the Participant list.
 */
public class ParticipantForm extends HBox {
    public final ColorPicker colorPicker;
    public final TextField nameField;
    public final TextField initialsField;
    public final TextField roleField;

    public ParticipantForm() {
        colorPicker = new ColorPicker(); //TODO: Make a custom color picker (https://community.oracle.com/thread/2318310)
        colorPicker.setPrefWidth(50);

        nameField = new TextField();
        nameField.setPromptText("Name");

        initialsField = new TextField();
        initialsField.setPromptText("Initials");
        initialsField.setPrefWidth(50);

        initialsField.textProperty().addListener(ChangeListener -> {
            if(initialsField.getText().length() > 2)
                initialsField.setText(initialsField.getText(0, 2));
        });

        roleField = new TextField();
        roleField.setPromptText("Role");

        this.getChildren().addAll(colorPicker, nameField, initialsField, roleField);
        this.setSpacing(7);
    }

    public boolean isFilled() {
        return !(nameField.getText().isEmpty() || initialsField.getText().isEmpty()
                || roleField.getText().isEmpty());
    }
}
