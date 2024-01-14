package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductSchema;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;

public interface ProductService {

  AddProductDto addProduct(AddProductSchema addProductSchema);

  ProductEntity updateProduct(UpdateProductSchema updateProductSchema);

  ProductEntity findProduct(BigInteger productId);

  void deleteProduct(BigInteger prductId);
}
