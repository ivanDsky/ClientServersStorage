package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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


    @FXML
    private Label labelName;
    @FXML
    private Label labelPrice;
    @FXML
    private Label labelAmount;
    @FXML
    private Label labelGroup;
    @FXML
    private Label labelManufacturer;
    @FXML
    private Label labelDescription;

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

        reload();

        productTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, productTableViewSelectionModel, newValue) ->
                        selectProduct(newValue)
        );
    }

    private void reload(){
        repository.getAllProducts().thenAccept(result->Platform.runLater(()->{
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                productTable.getItems().clear();
                productTable.getItems().addAll(result.getSuccess().getValue());
            }
        }));
    }

    private void selectProduct(Product product){
        labelName.setText("Name - " + product.getProductName());
        labelPrice.setText("Price - " + product.getPrice());
        labelAmount.setText("Amount - " + product.getAmount());
        labelGroup.setText("Group - " + product.getGroupName());
        labelManufacturer.setText("Manufacturer - " + product.getManufacturer());
        labelDescription.setText("Description:\n" + product.getDescription());
    }

}