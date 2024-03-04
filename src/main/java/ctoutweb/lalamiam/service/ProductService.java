package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.UpdateProductDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;
import java.util.List;

/**
 * Gestion des produits d'un commerce
 */
public interface ProductService {

  /**
   * Ajout d'un produit
   * @param addProductSchema AddProductDto - Données sur le produit
   * @return AddProductResponseDto
   */
  AddProductResponseDto addProduct(AddProductDto addProductSchema);

  /**
   * Mise a jour d'un produit
   * @param updateProductSchema UpdateProductDto - Nouvelle données du produit
   * @return ProductEntity
   */
  ProductEntity updateProduct(UpdateProductDto updateProductSchema);

  /**
   * Recherchr d'un produit
   * @param productId Long - Identifiant du produit
   * @return ProductEntity
   */
  ProductEntity findProduct(Long productId);

  /**
   * Suppression d'un produit
   * @param prductId Long - Identifiant du produit
   */
  void deleteProduct(Long prductId);

  /**
   * Récuperation de la liste des produits d'un commerce pour une commande en cours de création ou de modification
   * @param storeId Long - Identifiant commerce
   * @return List<ProductEntity>
   */
  List<ProductEntity> getStoreProducts(Long storeId);
}
