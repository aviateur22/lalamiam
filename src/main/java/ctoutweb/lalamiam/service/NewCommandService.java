package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.StoreProductsInformationDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;

import java.math.BigInteger;

public interface NewCommandService extends Command {

  /**
   * Creation d'une nouvelle commande
   * @param storeId BigInt - Identifiant commerce
   * @return CommandInformationDto
   */
  StoreProductsInformationDto createCommand(Long storeId);

  /**
   * Modification d'une commande
   * @param storeId BigInt - Identifiant commerce
   * @param commandId BigInt - Identifiant commande
   * @return CommandInformationDto
   */
  StoreProductsInformationDto updateCommand(Long storeId, Long commandId);

  /**
   *  Renvoie les données nécessaire à l'affichage d'une commande
   * @param storeId BigInt - Identifiant commerce
   * @param commandId BigInt - Identifiant commande
   * @return RegisterCommandDto
   */
  RegisterCommandDto getCommand(Long storeId, Long commandId);
}
