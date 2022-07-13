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
import ua.fastgroup.clientserversstorage.Storage;
import ua.fastgroup.clientserversstorage.controllers.add_and_update.AddUpdateGroupController;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;
import ua.fastgroup.clientserversstorage.remote.result.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupsController {
    @FXML
    private TableView<Group> groupTable;
    private final List<TableColumn<Group, ?>> columns = new ArrayList<>();

    private final Alert alert = new Alert(Alert.AlertType.ERROR);

    private Repository repository;
    private ProductsController productsController;

    @FXML
    private Label labelGroupName;
    @FXML
    private Label labelDescription;
    @FXML
    private Button updateGroup;
    @FXML
    private Button deleteGroup;
    @FXML
    private Button deleteAll;
    @FXML
    private Button getPriceGroup;
    @FXML
    private Button getAllProductsGroup;

    public void init(Repository repository, ProductsController productsController) {
        this.productsController = productsController;
        this.repository = repository;
        columns.add(new TableColumn<Group, String>("Name"));
        columns.add(new TableColumn<Group, String>("Description"));

        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("groupName"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("description"));

        groupTable.getColumns().addAll(columns);

        reload();

        groupTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        groupTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, groupTableViewSelectionModel, newValue) ->
                        selectGroup(newValue)
        );

        selectGroup(null);
    }

    public void onAdd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Storage.class.getResource("add-update-group-view.fxml"));

        Parent group = fxmlLoader.load();
        AddUpdateGroupController controller = fxmlLoader.getController();
        controller.init(repository, null);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(deleteGroup.getScene().getWindow());
        stage.setTitle("Add group");
        stage.setScene(new Scene(group));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> reload());
    }

    public void onUpdate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Storage.class.getResource("add-update-group-view.fxml"));

        Parent group = fxmlLoader.load();
        AddUpdateGroupController controller = fxmlLoader.getController();
        controller.init(repository, groupTable.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(deleteGroup.getScene().getWindow());
        stage.setTitle("Add group");
        stage.setScene(new Scene(group));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            reload();
            productsController.reload();
        });
    }

    public void onDelete() {
        String groupName = groupTable.getSelectionModel().getSelectedItem().getGroupName();
        repository.deleteGroup(groupName).thenAccept(result -> Platform.runLater(() ->
                showDialog(result,
                        "Deleted successfully", "The group " + groupName + " was deleted", true)));
    }

    public void onDeleteAll() {
        repository.clearDatabase().thenAccept(result -> Platform.runLater(() ->
                showDialog(result, "Deleted successfully", "All group were deleted", true)));
    }

    public void onGetPrice() {
        repository.getGroupPrice(groupTable.getSelectionModel().getSelectedItem().getGroupName())
                .thenAccept(result -> Platform.runLater(() -> showDialog(result, "Success", "Price of products in group  \"" + groupTable.getSelectionModel().getSelectedItem().getGroupName()
                        + "\" = " + result.getSuccess().getValue(), false)));
    }

    public void onGetAllProducts() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Storage.class.getResource("show-products-view.fxml"));
        Parent group = fxmlLoader.load();
        ShowProductsController controller = fxmlLoader.getController();
        controller.init(repository);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(deleteGroup.getScene().getWindow());
        stage.setTitle("Get all products");
        stage.setScene(new Scene(group));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> reload());
    }

    public void onGetAllProductsGroup() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Storage.class.getResource("show-products-view.fxml"));
        Parent group = fxmlLoader.load();
        ShowProductsController controller = fxmlLoader.getController();
        controller.init(repository, groupTable.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(deleteGroup.getScene().getWindow());
        stage.setTitle("Get all products from group");
        stage.setScene(new Scene(group));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> reload());
    }

    private void reload() {
        repository.getAllGroups().thenAccept(this::showResultList);
    }

    private void showResultList(Result<List<Group>> result) {
        Platform.runLater(() -> {
            if (result.isError()) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                groupTable.getItems().clear();
                groupTable.getItems().addAll(result.getSuccess().getValue());
            }
        });
    }

    private void selectGroup(Group group) {
        updateGroup.setDisable(group == null);
        deleteGroup.setDisable(group == null);
        deleteAll.setDisable(repository.getAllGroups() == null);
        getPriceGroup.setDisable(group == null);
        getAllProductsGroup.setDisable(group == null);
        labelGroupName.setText("Name - " + (group == null ? "" : group.getGroupName()));
        labelDescription.setText("Description:\n" + (group == null ? "" : group.getDescription()));
    }

    private void showDialog(Result<Object> result, String positiveTitle, String positiveMessage, boolean doReload) {
        if (result.isError()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(result.getError().getMessage());
            alert.setHeaderText("Something went wrong");
            alert.show();
            System.out.println(result.getError().getMessage());
        } else {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText(":)");
            alert.setTitle(positiveTitle);
            alert.setHeaderText(positiveMessage);
            alert.show();

            if (doReload) {
                reload();
                productsController.reload();
            }
        }
    }
}