package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

public interface StoreService {

  ProductEntity addProduct(AddProductSchema addProductSchema);
}
