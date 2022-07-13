package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;

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
    private Button getGroupPrice;


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
    }

    private void reload() {
        repository.getAllGroups().thenAccept(result -> Platform.runLater(() -> {
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            } else {
                groupTable.getItems().clear();
                groupTable.getItems().addAll(result.getSuccess().getValue());
            }
        }));
    }

    private void selectGroup(Group group) {
        labelGroupName.setText("Name - " + group.getGroupName());
        labelDescription.setText("Description:\n" + group.getDescription());
    }
}
