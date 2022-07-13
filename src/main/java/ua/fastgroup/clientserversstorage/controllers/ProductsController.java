package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.fastgroup.clientserversstorage.HelloApplication;
import ua.fastgroup.clientserversstorage.models.Product;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;
import ua.fastgroup.clientserversstorage.remote.result.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductsController {
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

    @FXML
    private TextField search;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonTotalPrice;

    public void init(Repository repository) {
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
                (observableValue, oldValue, newValue) ->
                        selectProduct(newValue)
        );
        selectProduct(null);
    }

    @FXML
    private void onAdd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-update-product-view.fxml"));

        Parent product = fxmlLoader.load();
        AddUpdateProductController controller = fxmlLoader.getController();
        controller.init(repository, null);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(buttonDelete.getScene().getWindow());
        stage.setTitle("Add product");
        stage.setScene(new Scene(product));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> reload());
    }

    @FXML
    private void onUpdate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-update-product-view.fxml"));

        Parent product = fxmlLoader.load();
        AddUpdateProductController controller = fxmlLoader.getController();
        controller.init(repository, productTable.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(buttonDelete.getScene().getWindow());
        stage.setTitle("Update product");
        stage.setScene(new Scene(product));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> reload());
    }

    @FXML
    private void onDelete() {
        repository.deleteProduct(productTable.getSelectionModel().getSelectedItem().getProductName())
                .thenAccept(result -> Platform.runLater(() -> {
                    if (result.isError()) {
                        alert.setContentText(result.getError().getMessage());
                        alert.show();
                        System.out.println(result.getError().getMessage());
                    } else {
                        onSearch();
                    }
                }));
    }

    @FXML
    private void onSearch() {
        repository.searchProduct(search.getText()).thenAccept(result -> showResultList(result));
    }

    @FXML
    private void onTotalPrice() {
        repository.getTotalPrice(productTable.getSelectionModel().getSelectedItem().getProductName())
            .thenAccept(result -> Platform.runLater(() -> {
                if (result.isError()) {
                    alert.setContentText(result.getError().getMessage());
                    alert.show();
                    System.out.println(result.getError().getMessage());
                } else {
                    alert.setContentText("Total price = " + result.getSuccess().getValue());
                    alert.show();
                }
            }));
    }

    private void reload() {
        repository.getAllProducts().thenAccept(result -> showResultList(result));
    }

    private void showResultList(Result<List<Product>> result) {
        Platform.runLater(() -> {
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                productTable.getItems().clear();
                productTable.getItems().addAll(result.getSuccess().getValue());
            }
        });
    }

    private void selectProduct(Product product) {
        buttonUpdate.setDisable(product == null);
        buttonDelete.setDisable(product == null);
        buttonTotalPrice.setDisable(product == null);
        labelName.setText("Name - " + (product == null ? "" : product.getProductName()));
        labelPrice.setText("Price - " + (product == null ? "" : product.getPrice()));
        labelAmount.setText("Amount - " + (product == null ? "" : product.getAmount()));
        labelGroup.setText("Group - " + (product == null ? "" : product.getGroupName()));
        labelManufacturer.setText("Manufacturer - " + (product == null ? "" : product.getManufacturer()));
        labelDescription.setText("Description:\n" + (product == null ? "" : product.getDescription()));
    }
}
