package ua.fastgroup.clientserversstorage.remote.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.models.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSource {
    private final HttpClient client = HttpClient.newBuilder()
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final static String uri = "http://localhost:8765/api/";

    public CompletableFuture<HttpResponse<String>> addProduct(Product product) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product"))
                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(product)))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> updateProduct(String productName, Product product) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/" + productName))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(product)))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> deleteProduct(String productName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/" + productName))
                .DELETE()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getAllProducts() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/all"))
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getProduct(String productName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "product/" + productName))
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> addGroup(Group group) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "group"))
                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(group)))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> updateGroup(String groupName, Group group) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "group/" + groupName))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(group)))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
