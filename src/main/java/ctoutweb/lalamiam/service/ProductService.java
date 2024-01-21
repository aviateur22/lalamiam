package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.UpdateProductDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;

public interface ProductService {

  AddProductResponseDto addProduct(AddProductDto addProductSchema);

  ProductEntity updateProduct(UpdateProductDto updateProductSchema);

  ProductEntity findProduct(BigInteger productId);

  void deleteProduct(BigInteger prductId);
}
