package me.mjakubowski;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ToolController {

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextArea shaclTextArea;

    @FXML
    private TextArea dataTextArea;

    @FXML
    private void validateAndShow() {
        String shaclInput = shaclTextArea.getText();
        String dataInput = dataTextArea.getText();

        String validationReport = Validator.validate(shaclInput, dataInput);

        outputTextArea.setText(validationReport);
    }
}
