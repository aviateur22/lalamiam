package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;

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
   * Récuperation des commandes du dashboard
   * @param getDashboard
   * @return List<DashboardCommandDto>
   */
  List<DashboardCommandDto> getDashboardCommands(GetDashboardCommandDto getDashboard);
}
