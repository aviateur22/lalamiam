package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;
import java.util.Objects;

public class UpdateProductQuantityInCommandSchema  {
  private BigInteger productId;
  private BigInteger commandId;
  private BigInteger storeId;
  private Integer productQuantity;

  /**
   *
   */
  public UpdateProductQuantityInCommandSchema() {
  }

  public UpdateProductQuantityInCommandSchema(BigInteger productId, BigInteger commandId, BigInteger storeId, Integer productQuantity) {
    this.productId = productId;
    this.commandId = commandId;
    this.storeId = storeId;
    this.productQuantity = productQuantity;
  }

  public BigInteger getProductId() {
    return productId;
  }

  public void setProductId(BigInteger productId) {
    this.productId = productId;
  }

  public BigInteger getCommandId() {
    return commandId;
  }

  public void setCommandId(BigInteger commandId) {
    this.commandId = commandId;
  }

  public BigInteger getStoreId() {
    return storeId;
  }

  public void setStoreId(BigInteger storeId) {
    this.storeId = storeId;
  }

  public Integer getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UpdateProductQuantityInCommandSchema that = (UpdateProductQuantityInCommandSchema) o;
    return Objects.equals(productId, that.productId) && Objects.equals(commandId, that.commandId) && Objects.equals(storeId, that.storeId) && Objects.equals(productQuantity, that.productQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, commandId, storeId, productQuantity);
  }

  @Override
  public String toString() {
    return "UpdateProductQuantityInCommandSchema{" +
            "productIdList=" + productId +
            ", commandId=" + commandId +
            ", storeId=" + storeId +
            ", productQuantity=" + productQuantity +
            '}';
  }
}
