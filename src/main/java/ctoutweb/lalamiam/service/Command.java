package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;

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
  StoreProductsInformationDto getStoreProductsForCommand(Long storeId, Long commandId);

  /**
   * Validation de la commande
   * @param storeId BigInteger - identifiant du commerce
   * @param commandId BigInteger - identifiant de la commande (null si commande en cours de création)
   * @param productSelectInformation ProductSelectInformationDto - Données renseignées dans la commande
   */
  void validateProductsSelection(Long storeId, Long commandId, ProductSelectInformationDto productSelectInformation);

  /**
   * Récupération d'une liste de slot disponible pour un commerce
   * @param storeSlotInformation CommandInformationDto - Information sur la commande
   * @return List<LocalDateTime> Liste des slots disponibles
   */
  List<LocalDateTime> getStoreSlotAvailibility(CommandInformationDto storeSlotInformation);

  /**
   * Validation d'un créneaux
   * @param commandInformation CommandInformationDto - Information sur la commande
   * @param selectSlotTime LocalDateTime - Créneau choisi pour la cammande
   */
  void validateSlot(CommandInformationDto commandInformation, LocalDateTime selectSlotTime);

  /**
   * Controle et persistance de la commande
   * @param persitCommand PersitCommandDto - Données pour enregistrée une commande
   * @return RegisterCommandDto
   */
  RegisterCommandDto persistCommand(PersitCommandDto persitCommand);

}
