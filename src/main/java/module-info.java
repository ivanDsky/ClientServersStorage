module ua.fastgroup.clientserversstorage {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.net.http;
    requires lombok;
    requires com.fasterxml.jackson.databind;

    opens ua.fastgroup.clientserversstorage to javafx.fxml;
    exports ua.fastgroup.clientserversstorage;
    exports ua.fastgroup.clientserversstorage.controllers;
    opens ua.fastgroup.clientserversstorage.controllers to javafx.fxml;
    exports ua.fastgroup.clientserversstorage.models to com.fasterxml.jackson.databind;
    opens ua.fastgroup.clientserversstorage.models to javafx.base;
}