package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.util.List;

/**
 * Gestion ajout produit dans une commande
 */
public interface CommandProductService {

/**
   * Récuperation de la liste des produits d'un commerce pour une commande en cours de création ou de modification
   * @param storeId BigInteger - Identifiant commerce
   * @return List<ProductEntity>
   */
  List<ProductEntity> getStoreProducts(Long storeId);
}
