package ua.fastgroup.clientserversstorage.remote.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.models.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class DataSource {
    public HttpClient getClient() {
        return client;
    }

    private final HttpClient client = HttpClient.newBuilder()
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final static String uri = "http://localhost:8765/api/";


}