package ua.fastgroup.clientserversstorage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.fastgroup.clientserversstorage.controllers.MainController;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Parent root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.init(new Repository());

        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}