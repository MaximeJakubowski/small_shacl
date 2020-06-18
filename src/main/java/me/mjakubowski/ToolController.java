package me.mjakubowski;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;

public class ToolController {

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextArea shaclTextArea;

    @FXML
    private TextArea dataTextArea;

    @FXML
    private Text shaclErrorMsg;

    @FXML
    private Text dataErrorMsg;

    @FXML
    private Text validationErrorMsg;

    @FXML
    public void initialize() {
        loadBaseTemplate();
    }

    @FXML
    private void validateAndShowTQ() {
        validateAndShow("TQ");
    }

    @FXML
    private void validateAndShowJena() {
        try {
            validateAndShow("JENA");
        } catch (StackOverflowError e) {
            validationErrorMsg.setText("JENA ran out of memory.");
        }
    }

    @FXML
    public void loadBaseTemplate() {
        setTemplate("base");
    }

    @FXML
    private void loadEliteTemplate() {
        setTemplate("elite");
    }

    @FXML
    private void loadAddressTemplate() {
        setTemplate("addresses");
    }

    private void setTemplate(String template) {
        shaclTextArea.setText(loadTemplateFile(template + "/shacl.ttl"));
        dataTextArea.setText(loadTemplateFile(template + "/data.ttl"));
    }

    private String loadTemplateFile(String template) {
        try {
            File resource = SmallSHACL.getFileFromResources("templates/" + template);
            return new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void resetErrors() {
        validationErrorMsg.setText("");
        dataErrorMsg.setText("");
        shaclErrorMsg.setText("");
    }

    private void validateAndShow(String backend) {
        resetErrors();
        outputTextArea.setText("");

        String shaclInput = shaclTextArea.getText();
        String dataInput = dataTextArea.getText();

        try {
            String validationReport = Validator.validate(shaclInput, dataInput, backend);
            outputTextArea.setText(validationReport);
        } catch (DataParseException e) {
            dataErrorMsg.setText(e.getMessage());
        } catch (ShapeParseException e) {
            shaclErrorMsg.setText(e.getMessage());
        }
    }

}
