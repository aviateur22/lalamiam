package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.CalculatedCommandInformation;
import ctoutweb.lalamiam.model.ManualCommandInformation;

/**
 * Contient l'ensemble des données d'un commande qui se trouve en base de données
 */
public class RegisterCommandDto {
  private Long storeId;
  private Long commandId;
  private Integer commandStatus;
  private String preparedBy;
  private ManualCommandInformation manualCommandInformation;
  private CalculatedCommandInformation calculatedCommandInformation;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  public Long getCommandId() {
    return commandId;
  }

  public void setCommandId(Long commandId) {
    this.commandId = commandId;
  }

  public Integer getCommandStatus() {
    return commandStatus;
  }

  public void setCommandStatus(Integer commandStatus) {
    this.commandStatus = commandStatus;
  }

  public String getPreparedBy() {
    return preparedBy;
  }

  public void setPreparedBy(String preparedBy) {
    this.preparedBy = preparedBy;
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
