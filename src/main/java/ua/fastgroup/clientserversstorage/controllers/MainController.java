package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import ua.fastgroup.clientserversstorage.models.Product;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainController {
    @FXML
    private TableView<Product> productTable;
    private List<TableColumn<Product, ?>> columns = new ArrayList<>();

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private Repository repository;

    public void init(Repository repository){
        this.repository = repository;
        columns.add(new TableColumn<Product, String>("Name"));
        columns.add(new TableColumn<Product, Double>("Price"));
        columns.add(new TableColumn<Product, Integer>("Amount"));
        columns.add(new TableColumn<Product, String>("Group"));
        columns.add(new TableColumn<Product, String>("Manufacturer"));
        columns.add(new TableColumn<Product, String>("Description"));

        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("productName"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("amount"));
        columns.get(3).setCellValueFactory(new PropertyValueFactory<>("groupName"));
        columns.get(4).setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        columns.get(5).setCellValueFactory(new PropertyValueFactory<>("description"));

        productTable.getColumns().addAll(columns);

        repository.getAllProducts().thenAccept(result->Platform.runLater(()->{
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                productTable.getItems().addAll(result.getSuccess().getValue());
            }
        }));
    }

}