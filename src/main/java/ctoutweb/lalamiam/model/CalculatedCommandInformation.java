package ctoutweb.lalamiam.model;

public class CalculatedCommandInformation {
  private String commandCode;
  private Double commandePrice;
  private Integer productQuantity;
  private Integer commandPreparationTime;

  ////////////////////////////////////////////////////////////////////////////////


  public String getCommandCode() {
    return commandCode;
  }

  public void setCommandCode(String commandCode) {
    this.commandCode = commandCode;
  }

  public Double getCommandePrice() {
    return commandePrice;
  }

  public void setCommandePrice(Double commandePrice) {
    this.commandePrice = commandePrice;
  }

  public Integer getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
  }

  public Integer getCommandPreparationTime() {
    return commandPreparationTime;
  }

  public void setCommandPreparationTime(Integer commandPreparationTime) {
    this.commandPreparationTime = commandPreparationTime;
  }
}
