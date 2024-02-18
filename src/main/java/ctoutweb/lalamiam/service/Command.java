package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.StoreProductsInformationDto;
import ctoutweb.lalamiam.model.dto.ProductSelectInformationDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.model.dto.CommandInformationDto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface Command {
  /**
   * Récupération des produits d'un commerce associant les quantités d'une commande.
   * Si commande en cours de création alors les quantité sont =0
   * @param storeId BigInteger - identifiant du commerce
   * @param commandId BigInteger - identifiant de la commande (null si commande en cours de création)
   * @return CommandInformationDto
   */
  StoreProductsInformationDto getStoreProductsForCommand(BigInteger storeId, BigInteger commandId);

  /**
   * Validation de la commande
   * @param storeId BigInteger - identifiant du commerce
   * @param commandId BigInteger - identifiant de la commande (null si commande en cours de création)
   * @param productSelectInformation ProductSelectInformationDto - Données renseignées dans la commande
   */
  void validateProductsSelection(BigInteger storeId, BigInteger commandId, ProductSelectInformationDto productSelectInformation);

  /**
   * Récupération d'une liste de slot disponible pour un commerce
   * @param storeSlotInformation StoreSlotInformationDto
   * @return List<LocalDateTime> Liste des slots disponibles
   */
  List<LocalDateTime> getStoreSlotAvailibility(CommandInformationDto storeSlotInformation);
  void validateSlot();
  RegisterCommandDto persistCommand();

}
