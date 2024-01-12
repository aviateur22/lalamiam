package ctoutweb.lalamiam.model.schema;

import ctoutweb.lalamiam.repository.entity.StoreEntity;

public record AddProductSchema(String name, Double price, String description, Integer preparationTime, String photo, StoreEntity store) {
}
