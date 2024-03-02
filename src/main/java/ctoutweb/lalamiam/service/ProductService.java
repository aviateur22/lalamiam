package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.UpdateProductDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;

/**
 * Gestion des produits d'un commerce
 */
public interface ProductService {

  AddProductResponseDto addProduct(AddProductDto addProductSchema);

  ProductEntity updateProduct(UpdateProductDto updateProductSchema);

  ProductEntity findProduct(Long productId);

  void deleteProduct(Long prductId);
}
