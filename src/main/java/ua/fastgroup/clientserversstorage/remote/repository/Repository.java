package ua.fastgroup.clientserversstorage.remote.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.fastgroup.clientserversstorage.models.Group;
import ua.fastgroup.clientserversstorage.models.Product;
import ua.fastgroup.clientserversstorage.remote.datasource.DataSource;
import ua.fastgroup.clientserversstorage.remote.result.Error;
import ua.fastgroup.clientserversstorage.remote.result.Result;
import ua.fastgroup.clientserversstorage.remote.result.Success;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Repository {
    private final DataSource dataSource = new DataSource();
    private ObjectMapper mapper = new ObjectMapper();

    public CompletableFuture<Result<Object>> addProduct(Product product) {
        try {
            return dataSource.addProduct(product).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<Object>> updateProduct(String productName, Product product) {
        try {
            return dataSource.updateProduct(productName, product).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<Object>> deleteProduct(String productName) {
        return dataSource.deleteProduct(productName).thenApply(response -> {
            if (response.statusCode() == 200) return new Success(null);
            else return new Error(response.body());
        });
    }

    public CompletableFuture<Result<List<Product>>> getAllProducts() {
        return dataSource.getAllProducts().thenApply(response -> {
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
        return dataSource.getProduct(productName).thenApply(response -> {
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

    public CompletableFuture<Result<Object>> addGroup(Group group) {
        try {
            return dataSource.addGroup(group).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }

    public CompletableFuture<Result<Object>> updateGroup(String groupName, Group group) {
        try {
            return dataSource.updateGroup(groupName, group).thenApply(response -> {
                if (response.statusCode() == 200) return new Success(null);
                else return new Error(response.body());
            });
        } catch (JsonProcessingException e) {
            return CompletableFuture.completedFuture(new Error("Incorrect json"));
        }
    }
}
