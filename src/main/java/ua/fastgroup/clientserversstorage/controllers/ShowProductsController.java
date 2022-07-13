package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.models.Product;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;
import ua.fastgroup.clientserversstorage.remote.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShowProductsController {
    @FXML
    private Button closeButton;
    @FXML
    private TableView<Product> productTable;
    private final List<TableColumn<Product, ?>> columns = new ArrayList<>();

    private Repository repository;
    private final Alert alert = new Alert(Alert.AlertType.ERROR);

    private void init() {
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
    }

    public void init(Repository repository) {
        this.repository = repository;
        init();
        sendRequest();
    }

    public void init(Repository repository, Group group) {
        this.repository = repository;
        init();
        sendRequest(group);
    }

    private void sendRequest() {
        CompletableFuture<Result<List<Product>>> res = repository.getAllProducts();
        res.thenAccept(result -> Platform.runLater(() -> {
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                productTable.getItems().addAll(result.getSuccess().getValue());
            }
        }));
    }

    private void sendRequest(Group group) {
        CompletableFuture<Result<Group>> res = repository.getAllProductsByGroup(group.getGroupName());
        res.thenAccept(result -> Platform.runLater(() -> {
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                productTable.getItems().addAll(result.getSuccess().getValue().getGroupProducts());
            }
        }));
    }

    public void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.getOnCloseRequest().handle(null);
        stage.close();
    }
}
