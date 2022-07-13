package ua.fastgroup.clientserversstorage.remote.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.fastgroup.clientserversstorage.models.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class DataSourceProduct {
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String uri;

    public DataSourceProduct(HttpClient httpClient, String uri) {
        this.httpClient = httpClient;
        this.uri = uri;
    }

    public CompletableFuture<HttpResponse<String>> addProduct(Product product) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product"))
                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(product)))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> updateProduct(String productName, Product product) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/" + productName))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(product)))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> deleteProduct(String productName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/" + productName))
                .DELETE()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getAllProducts() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/all"))
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getProduct(String productName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/" + productName))
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

}
