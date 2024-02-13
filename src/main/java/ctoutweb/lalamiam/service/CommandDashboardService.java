package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.repository.entity.CommandEntity;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

/**
 * Gestion des commandes afficheés sur le tableau de bord
 */
public interface CommandDashboardService {

  /**
   * Affichage des commande du jour
   * @param displayDay LocalDate - Date d'affichage
   * @param storeId BigInteger - Identiifiant commerce
   * @return List<CommandEntity>
   */
  List<CommandEntity> displayCommandsOfTheDay(LocalDate displayDay, BigInteger storeId);

  /**
   * Suppression d'une commande
   * @param storeId BigInteger - Identifiant commerce
   * @param commandId BigInteger - Identifiant commande
   */
  void deleteCommand(BigInteger storeId, BigInteger commandId);

  /**
   * Valider la préparationd'une commande
   * @param storeId BigInteger - Identifiant commerce
   * @param commandId BigInteger - Identifiant commande
   */
  void commandReady(BigInteger storeId, BigInteger commandId);

  /**
   * Modification date d'affichage des commandes
   * @param dayIncrementation Integer - Entier positif ou negatif permettant de faire varier la date
   * @param displayDay LocalDate - Date d'affichage en cours
   * @return List<CommandEntity>
   */
  List<CommandEntity> updateDisplayDate(LocalDate displayDay, Integer dayIncrementation);
}
