package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.FindListOfSlotTimeAvailableDto;
import ctoutweb.lalamiam.model.dto.SelectCommandSlotDto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestion des creneaux de commande
 */
public interface SlotService {

  /**
   * Affichage des creneaux disponible pour la commande
   * @param findSlotTime FindListOfSlotTimeAvailableDto - Données sur la commande en cours
   * @return List<LocalDateTime>
   */
  List<LocalDateTime> findAllSlotAvailable(FindListOfSlotTimeAvailableDto findSlotTime);

  /**
   * Selection d'un creneau de commande
   * @param selectCommandSlot SelectCommandSlotDto - Données sur le creneau choisi
   */
  void selectCommandSlot(SelectCommandSlotDto selectCommandSlot);

  /**
   * Selection d'un créneau pour une commande déja enregistré
   * @param commandId BigInteger- Identifiant de la commande
   * @param storeId BigInteger - Identifiiant du commerce
   */
  void selectCommandSlot(BigInteger commandId, BigInteger storeId);
}
