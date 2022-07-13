package ua.fastgroup.clientserversstorage.models;

import lombok.*;

import java.util.List;

@Data
public class Group {
    String groupName;
    String description;
    List<Product> groupProducts;
}