package ctoutweb.lalamiam.model;

public class ProductWithQuantity {
  private Long productId;
  private Integer productQuantity;

  public ProductWithQuantity() {
  }

  public ProductWithQuantity(Long productId, Integer productQuantity) {
    this.productId = productId;
    this.productQuantity = productQuantity;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Integer getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
  }
}
