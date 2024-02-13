package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.CommandInformationDto;
import ctoutweb.lalamiam.model.dto.*;

import java.math.BigInteger;

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

  /**
   * Récupération des données d'une commande
   * @param storeId BigInteger - Identifiant du commerce
   * @param commandId BigInteger - Identifiant de la commande
   * @return CompleteCommandDetailResponseDto
   */
  RegisterCommandDto getCommand(BigInteger storeId, BigInteger commandId);

  /**
   * Creation d'une nouvelle commande
   * @param storeId BigInt - Identifiant commerce
   * @return CommandInformationDto
   */
  CommandInformationDto createCommand(BigInteger storeId);

  /**
   * Modification d'une commande
   * @param storeId BigInt - Identifiant commerce
   * @param commandId BigInt - Identifiant commande
   * @return CommandInformationDto
   */
  CommandInformationDto updateCommand(BigInteger storeId, BigInteger commandId);

}
