package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Manipulation d'une commande
 */
public interface CommandService {
  /**
   * Ajout d'une nouvelle commande
   * @param addCommand AddCommandDto - Données de la nouvelle commande
   * @return CompleteCommandDetailResponseDto
   */
  CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommand);

  /**
   * Mise a jour de la quantité d'un produit
   * @param updateCommand  UpdateProductQuantityDto - Données sur les modifications
   * @return UpdateProductQuantityResponseDto
   */
  UpdateProductQuantityResponseDto updateProductQuantityInCommand(UpdateProductQuantityDto updateCommand);

  /**
   * Suppression d'un produit
   * @param deleteProductInCommand DeleteProductInCommandDto - Données sur le produit a supprimer
   * @return SimplifyCommandDetailResponseDto
   */
  SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand);
}
