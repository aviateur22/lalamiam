package ctoutweb.lalamiam.service;

import java.math.BigInteger;

public interface MoneyChangeService {

  /**
   * Clacule le rendu de monnaie
   * @param storeId BigInteger - Identifiant commerce
   * @param commandId BigInteger - Identifiant commande
   */
  void calculateMoneyChange(BigInteger storeId, BigInteger commandId);
}
