package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;
import ctoutweb.lalamiam.model.dto.AddProductsInCommandResponseDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;
import java.util.List;

/**
 * Gestion ajout produit dans une commande
 */
public interface CommandProductService {
//  /**
//   * Ajout de nouveaux produits dans une commande existante
//   * @param addProductsInCommand AddProductsInCommandDto - Données sur les produits à ajoutés
//   * @return AddProductsInCommandResponseDto
//   */
//  AddProductsInCommandResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand);
//
//  /**
//   * Récuperation de la liste des produits d'un commerce pour une commande existante
//   * @param storeId BigInteger - Identifiant commerce
//   * @param commandId BigInteger - Identifiant de la commande
//   * @return List<ProductEntity>
//   */
//  List<ProductEntity> getStoreProducts(BigInteger storeId, BigInteger commandId);

  /**
   * Récuperation de la liste des produits d'un commerce pour une commande en cours de création ou de modification
   * @param storeId BigInteger - Identifiant commerce
   * @return List<ProductEntity>
   */
  List<ProductEntity> getStoreProducts(Long storeId);
}
