package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;
import ua.fastgroup.clientserversstorage.remote.result.Result;

import java.util.concurrent.CompletableFuture;

public class AddUpdateGroupController {
    @FXML
    Label title;
    @FXML
    TextField groupName;
    @FXML
    TextField description;
    @FXML
    Button addButton;

    private Repository repository;
    private final Alert alert = new Alert(Alert.AlertType.ERROR);

    private Group prevGroup;

    public void init(Repository repository, Group group) {
        this.repository = repository;
        prevGroup = group;
        if (prevGroup == null) {
            title.setText("Add group");
            addButton.setText("Add");
        } else {
            title.setText("Update group");
            addButton.setText("Update");

            groupName.setText(group.getGroupName());
            description.setText(group.getDescription());
        }
    }

    @FXML
    private void onAddPressed() {
        Group group = new Group();
        group.setGroupName((String) getOrNull(
                (prevGroup == null) ? null : prevGroup.getGroupName(),
                groupName.getText()
        ));
        group.setDescription((String) getOrNull(
                (prevGroup == null) ? null : prevGroup.getDescription(),
                description.getText()
        ));

        sendRequest(group);
    }

    private void sendRequest(Group group) {
        CompletableFuture<Result<Object>> res = (prevGroup == null) ?
                repository.addGroup(group) :
                repository.updateGroup(prevGroup.getGroupName(), group);

        addButton.setDisable(true);

        res.thenAccept(result -> Platform.runLater(() -> {
            if (result.isError()) {
                alert.setContentText(result.getError().getMessage());
                alert.show();
                System.out.println(result.getError().getMessage());
            }
            addButton.setDisable(false);
        }));
    }

    private Object getOrNull(Object prev, Object cur) {
        if (prev == null) return cur;
        if (prev.equals(cur)) return null;
        return cur;
    }
}
