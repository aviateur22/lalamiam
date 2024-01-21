package ctoutweb.lalamiam.model.schema;

import ctoutweb.lalamiam.repository.entity.StoreEntity;

import java.math.BigInteger;

public record AddProductSchema(
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        BigInteger storeId) {
}
