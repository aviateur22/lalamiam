package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface NewCommandService extends Command {

  /**
   * Creation d'une nouvelle commande
   * @param storeId Long - Identifiant commerce
   * @return CommandInformationDto
   */
  StoreProductsInformationDto getStoreProductToCreateCommand(Long storeId);

  /**
   * Modification d'une commande
   * @param storeId Long - Identifiant commerce
   * @param commandId Long - Identifiant commande
   * @return CommandInformationDto
   */
  StoreProductsInformationDto getStoreProductToUpdateCommand(Long storeId, Long commandId);

  /**
   *  Renvoie les données nécessaire à l'affichage d'une commande
   * @param storeId Long - Identifiant commerce
   * @param commandId Long - Identifiant commande
   * @return RegisterCommandDto
   */
  RegisterCommandDto getCommand(Long storeId, Long commandId);

  /**
   * Mise a jour du status d'une commande
   * @param proUpdateCommandStatus ProUpdateCommandStatusDto
   * @return StoreProductsInformationDto
   */
  RegisterCommandDto updateCommandStatus(ProUpdateCommandStatusDto proUpdateCommandStatus);

  /**
   * Récupération liste des commandes pour dashboard
   * @param proId Long - identifiant pro
   * @param storeId Long - Identifiant commerce
   * @param commandDate LocalDate- date de commande
   * @return DashboardDto
   */
  DashboardDto getDashboardInformation(Long proId, Long storeId, LocalDate commandDate);

  /**
   * Récupération liste des commandes pour dashboard filtrer par statut de commande
   * @param proId Long - identifiant pro
   * @param storeId Long - Identifiant commerce
   * @param commandDate LocalDate- date de commande
   * @param statusIdList List<Integer> - Liste des identitifiants du statut des commandes
   * @return DashboardDto
   */
  DashboardDto getDashboardCommandsByStatus(Long proId, Long storeId, LocalDate commandDate, List<Integer> statusIdList);

  /**
   * Récupération d'une commande sur le dashboard filtrer par code de commande
   * @param proId Long - identifiant pro
   * @param storeId Long - Identifiant commerce
   * @param commandDate LocalDate- date de commande
   * @param commandCode String - code de la commande
   * @return DashboardDto
   */
  DashboardDto getDashboardCommandByCode(Long proId, Long storeId, LocalDate commandDate, String commandCode);
}
