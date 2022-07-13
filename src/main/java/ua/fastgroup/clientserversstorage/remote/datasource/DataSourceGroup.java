package ua.fastgroup.clientserversstorage.remote.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.fastgroup.clientserversstorage.models.Group;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class DataSourceGroup {
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String uri;

    public DataSourceGroup(HttpClient httpClient, String uri) {
        this.httpClient = httpClient;
        this.uri = uri;
    }

    public CompletableFuture<HttpResponse<String>> addGroup(Group group) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "group"))
                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(group)))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> updateGroup(String groupName, Group group) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "group/" + groupName))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(group)))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getAllGroups() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "group/all"))
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> deleteGroup(String groupName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "group/" + groupName))
                .DELETE()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getGroupPrice(String groupName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "price/group/" + groupName))
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getProductByGroup(String groupName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create((uri + "group/" + groupName)))
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
