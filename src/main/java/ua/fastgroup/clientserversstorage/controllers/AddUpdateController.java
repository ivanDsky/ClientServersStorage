package ua.fastgroup.clientserversstorage.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import ua.fastgroup.clientserversstorage.models.Product;
import ua.fastgroup.clientserversstorage.remote.repository.Repository;
import ua.fastgroup.clientserversstorage.remote.result.Result;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class AddUpdateController {
    @FXML
    Label title;
    @FXML
    TextField productName;
    @FXML
    TextField groupName;
    @FXML
    TextField productPrice;
    @FXML
    TextField productAmount;
    @FXML
    TextField productManufacturer;
    @FXML
    TextField productDescription;

    @FXML
    Button addButton;

    private Repository repository;
    private final Alert alert = new Alert(Alert.AlertType.ERROR);

    private Product prevProduct;

    public void init(Repository repository, Product product){
        this.repository = repository;
        prevProduct = product;
        if (prevProduct == null) {
            title.setText("Add product");
            addButton.setText("Add");
        } else {
            title.setText("Update product");
            addButton.setText("Update");
            productName.setText(product.getProductName());
            groupName.setText(product.getGroupName());
            productPrice.setText(product.getPrice().toString());
            productAmount.setText(product.getAmount().toString());
            productManufacturer.setText(product.getManufacturer());
            productDescription.setText(product.getDescription());
        }

        TextFormatter<Integer> intFormatter = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );

        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter<Double> doubleFormatter = new TextFormatter<>(
                new DoubleStringConverter(),
                0.0,
                change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);

        productPrice.setTextFormatter(doubleFormatter);
        productAmount.setTextFormatter(intFormatter);
    }

    @FXML
    private void onAddPressed() {
        Product product = new Product();
        product.setProductName((String) getOrNull(
                (prevProduct == null) ? null : prevProduct.getProductName(),
                productName.getText()
        ));
        product.setGroupName((String) getOrNull(
                (prevProduct == null) ? null : prevProduct.getGroupName(),
                groupName.getText()
        ));
        product.setPrice((Double) getOrNull(
                (prevProduct == null) ? null : prevProduct.getPrice(),
                Double.parseDouble(productPrice.getText())
        ));
        product.setAmount((Integer) getOrNull(
                (prevProduct == null) ? null : prevProduct.getAmount(),
                Integer.parseInt(productAmount.getText())
        ));
        product.setManufacturer((String) getOrNull(
                (prevProduct == null) ? null : prevProduct.getManufacturer(),
                productManufacturer.getText()
        ));
        product.setDescription((String) getOrNull(
                (prevProduct == null) ? null : prevProduct.getDescription(),
                productDescription.getText()
        ));

        sendRequest(product);
    }

    private void sendRequest(Product product){
        CompletableFuture<Result<Object>> res = (prevProduct == null) ?
                repository.addProduct(product) :
                repository.updateProduct(prevProduct.getProductName(), product);

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

    private Object getOrNull(Object prev, Object cur){
        if (prev == null) return cur;
        if (prev.equals(cur)) return null;
        return cur;
    }
}
