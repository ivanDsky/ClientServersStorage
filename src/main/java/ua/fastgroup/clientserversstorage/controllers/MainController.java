package ua.fastgroup.clientserversstorage.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ua.fastgroup.clientserversstorage.HelloApplication;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;

import java.io.IOException;

public class MainController {

    @FXML
    TabPane pane;
    public void init(Repository repository) throws IOException {
        FXMLLoader fxmlLoaderGroup = new FXMLLoader(HelloApplication.class.getResource("groups-view.fxml"));
        Node group = fxmlLoaderGroup.load();
        GroupsController controllerGroup = fxmlLoaderGroup.getController();
        controllerGroup.init(repository);
        Tab groupTab = new Tab("Groups", group);
        groupTab.setClosable(false);
        pane.getTabs().add(groupTab);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("products-view.fxml"));
        Node product = fxmlLoader.load();
        ProductsController controller = fxmlLoader.getController();
        controller.init(repository);
        Tab productTab = new Tab("Products", product);
        productTab.setClosable(false);
        pane.getTabs().add(productTab);
    }

}