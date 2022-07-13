package ua.fastgroup.clientserversstorage.remote.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.models.Product;
import ua.fastgroup.clientserversstorage.remote.datasource.DataSourceGroup;
import ua.fastgroup.clientserversstorage.remote.datasource.DataSourceProduct;
import ua.fastgroup.clientserversstorage.remote.result.Error;
import ua.fastgroup.clientserversstorage.remote.result.Result;
import ua.fastgroup.clientserversstorage.remote.result.Success;

import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Repository {
    private final HttpClient client = HttpClient.newBuilder()
            .build();
    private final static String uri = "http://localhost:8765/api/";

    //    private final DataSource dataSource = new DataSource();
    private final DataSourceProduct dataSourceProduct = new DataSourceProduct(client, uri);
    private final DataSourceGroup dataSourceGroup = new DataSourceGroup(client, uri);
    private ObjectMapper mapper = new ObjectMapper();


    public CompletableFuture<Result<Object>> addProduct(Product product) {
        try {
            return dataSourceProduct.addProduct(product).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<Object>> updateProduct(String productName, Product product) {
        try {
            return dataSourceProduct.updateProduct(productName, product).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<Object>> deleteProduct(String productName) {
        return dataSourceProduct.deleteProduct(productName).thenApply(response -> {
            if (response.statusCode() == 200) return new Success(null);
            else return new Error(response.body());
        });
    }

    public CompletableFuture<Result<List<Product>>> getAllProducts() {
        return dataSourceProduct.getAllProducts().thenApply(response -> {
            try {
                if (response.statusCode() == 200)
                    return new Success(mapper.readValue(response.body(), new TypeReference<List<Product>>() {
                    }));
                else
                    return new Error(response.body());
            } catch (JsonProcessingException e) {
                return new Error("Incorrect json");
            }
        });
    }

    public CompletableFuture<Result<List<Product>>> searchProduct(String productName) {
        return dataSourceProduct.searchProduct(productName).thenApply(response -> {
            try {
                if (response.statusCode() == 200)
                    return new Success(mapper.readValue(response.body(), new TypeReference<List<Product>>(){}));
                else
                    return new Error(response.body());
            } catch (JsonProcessingException e) {
                return new Error("Incorrect json");
            }
        });
    }

    public CompletableFuture<Result<Product>> getProduct(String productName) {
        return dataSourceProduct.getProduct(productName).thenApply(response -> {
            try {
                if (response.statusCode() == 200)
                    return new Success(mapper.readValue(response.body(), Product.class));
                else
                    return new Error(response.body());
            } catch (JsonProcessingException e) {
                return new Error("Incorrect json");
            }
        });
    }

    public CompletableFuture<Result<Double>> getTotalPrice(String productName) {
        return dataSourceProduct.getTotalPrice(productName).thenApply(response -> {
            try {
                if (response.statusCode() == 200)
                    return new Success(mapper.readValue(response.body(), Double.class));
                else
                    return new Error(response.body());
            } catch (JsonProcessingException e) {
                return new Error("Incorrect json");
            }
        });
    }

    public CompletableFuture<Result<Object>> addGroup(Group group) {
        try {
            return dataSourceGroup.addGroup(group).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<Object>> updateGroup(String groupName, Group group) {
        try {
            return dataSourceGroup.updateGroup(groupName, group).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<List<Group>>> getAllGroups() {
        return dataSourceGroup.getAllGroups().thenApply(response -> {
            try {
                if (response.statusCode() == 200)
                    return new Success(mapper.readValue(response.body(), new TypeReference<List<Group>>() {
                    }));
                else
                    return new Error(response.body());
            } catch (JsonProcessingException e) {
                return new Error("Incorrect json");
            }
        });
    }
}
