package ctoutweb.lalamiam.model;

import java.math.BigInteger;

public class ProductWithQuantity {
  private BigInteger productId;
  private Integer productQuantity;

  public ProductWithQuantity() {
  }

  public ProductWithQuantity(BigInteger productId, Integer productQuantity) {
    this.productId = productId;
    this.productQuantity = productQuantity;
  }

  public BigInteger getProductId() {
    return productId;
  }

  public void setProductId(BigInteger productId) {
    this.productId = productId;
  }

  public Integer getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
  }
}
