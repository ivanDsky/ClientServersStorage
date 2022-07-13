package ua.fastgroup.clientserversstorage.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Product {
    private String productName;
    private Double price;
    private Integer amount = 0;
    private String groupName;
    private String description = "";
    private String manufacturer = "";
}

