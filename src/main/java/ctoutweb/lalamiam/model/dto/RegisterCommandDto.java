package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.CalculatedCommandInformation;
import ctoutweb.lalamiam.model.ManualCommandInformation;

import java.math.BigInteger;

/**
 * Contient l'ensemble des données d'un commande
 */
public class RegisterCommandDto {
  private BigInteger storeId;
  private BigInteger commandId;
  private ManualCommandInformation manualCommandInformation;
  private CalculatedCommandInformation calculatedCommandInformation;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public BigInteger getStoreId() {
    return storeId;
  }

  public void setStoreId(BigInteger storeId) {
    this.storeId = storeId;
  }

  public BigInteger getCommandId() {
    return commandId;
  }

  public void setCommandId(BigInteger commandId) {
    this.commandId = commandId;
  }

  public ManualCommandInformation getManualCommandInformation() {
    return manualCommandInformation;
  }

  public void setManualCommandInformation(ManualCommandInformation manualCommandInformation) {
    this.manualCommandInformation = manualCommandInformation;
  }

  public CalculatedCommandInformation getCalculatedCommandInformation() {
    return calculatedCommandInformation;
  }

  public void setCalculatedCommandInformation(CalculatedCommandInformation calculatedCommandInformation) {
    this.calculatedCommandInformation = calculatedCommandInformation;
  }
}
