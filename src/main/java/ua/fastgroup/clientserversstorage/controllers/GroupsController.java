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
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;
import ua.fastgroup.clientserversstorage.remote.result.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupsController {
    @FXML
    private TableView<Group> groupTable;
    private List<TableColumn<Group, ?>> columns = new ArrayList<>();

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private Repository repository;

    @FXML
    private Label labelGroupName;
    @FXML
    private Label labelDescription;
    @FXML
    private Button addGroup;
    @FXML
    private Button updateGroup;
    @FXML
    private Button deleteGroup;
    @FXML
    private Button deleteAll;
    @FXML
    private Button getPriceGroup;
    @FXML
    private Button getAllProducts;
    @FXML
    private Button getAllProductsGroup;


    public void init(Repository repository) {
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-update-group-view.fxml"));

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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-update-group-view.fxml"));

        Parent group = fxmlLoader.load();
        AddUpdateGroupController controller = fxmlLoader.getController();
        controller.init(repository, groupTable.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(deleteGroup.getScene().getWindow());
        stage.setTitle("Add group");
        stage.setScene(new Scene(group));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> reload());
    }

    public void onDelete() {
//       repository.deleteGroup(groupTable.getSelectionModel().getSelectedItem().getGroupName())
//               .thenAccept(result -> Platform.runLater(() -> {
//                   if (result.isError()) {
//                       alert.setContentText(result.getError().getMessage());
//                       alert.show();
//                       System.out.println(result.getError().getMessage());
//                   } else {
////                       onSearch();
//                   }
//               }));
    }

    public void onDeleteAll() {
    }

    public void onGetPrice() {
        repository.getGroupPrice(groupTable.getSelectionModel().getSelectedItem().getGroupName())
                .thenAccept(result -> Platform.runLater(() -> {
                    if (result.isError()) {
                        alert.setContentText(result.getError().getMessage());
                        alert.show();
                        System.out.println(result.getError().getMessage());
                    } else {
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("Price of products in group  \"" + groupTable.getSelectionModel().getSelectedItem().getGroupName()
                                + "\" = " + result.getSuccess().getValue());
                        alert.show();
                        alert.setAlertType(Alert.AlertType.ERROR);
                    }
                }));
    }

    //TODO open new window with table
    public void onGetAllProducts() {

    }

    //TODO open new window with table
    public void onGetAllProductsGroup() {
        repository.getAllProductsByGroup(groupTable.getSelectionModel().getSelectedItem().getGroupName())
                .thenAccept(result -> Platform.runLater(() -> {
                    if (result.isError()) {
                        alert.setContentText(result.getError().getMessage());
                        alert.show();
                        System.out.println(result.getError().getMessage());
                    } else {
                        alert.setContentText(result.getSuccess().getValue() + "");
                        alert.show();
                    }
                }));
    }

    private void reload() {
        repository.getAllGroups().thenAccept(this::showResultList);
    }

    private void showResultList(Result<List<Group>> result) {
        Platform.runLater(() -> {
            if (result.isError()) {
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
        getAllProducts.setDisable(group != null);
        getAllProductsGroup.setDisable(group == null);
        labelGroupName.setText("Name - " + (group == null ? "" : group.getGroupName()));
        labelDescription.setText("Description:\n" + (group == null ? "" : group.getDescription()));
    }
}