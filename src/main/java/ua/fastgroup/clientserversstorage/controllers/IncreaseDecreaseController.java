package ua.fastgroup.clientserversstorage.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class IncreaseDecreaseController {
    @FXML
    Button submit;
    @FXML
    TextField input;
    @FXML
    Label header;

    void init(String title, String submitText, Consumer<Integer> onSubmit) {
        TextFormatter<Integer> intFormatter = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
        input.setTextFormatter(intFormatter);
        header.setText(title);
        submit.setOnAction((event) -> {
            onSubmit.accept(Integer.parseInt(input.getText()));
            ((Stage) submit.getScene().getWindow()).close();
        });
        submit.setText(submitText);
    }
}
