package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ctoutweb.lalamiam.model.schema.AddProductSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product")
public class ProductEntity {
  @Id
  @SequenceGenerator(name="productPkSeq", sequenceName="PRODUCT_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productPkSeq")
  private BigInteger id;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private Double price;

  @Column(name = "description")
  private String description;

  @Column(name = "preparation_time")
  private Integer preparationTime;

  @Column(name = "photo")
  private String photo;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private StoreEntity store;

  @OneToMany(mappedBy = "product")
  Set<CommandProductEntity> commandProducts;

  /**
   *
   */

  public ProductEntity() {
  }

  public ProductEntity(BigInteger productId) {
    this.id = productId;
  }

  public ProductEntity(AddProductSchema addProductSchema) {
    this.name = addProductSchema.name();
    this.photo = addProductSchema.photo();
    this.preparationTime = addProductSchema.preparationTime();
    this.description = addProductSchema.description();
    this.price = addProductSchema.price();
    this.store = new StoreEntity(addProductSchema.storeId());
  }

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getPreparationTime() {
    return preparationTime;
  }

  public void setPreparationTime(Integer preparationTime) {
    this.preparationTime = preparationTime;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @JsonBackReference
  public StoreEntity getStore() {
    return store;
  }

  public void setStore(StoreEntity store) {
    this.store = store;
  }

  @JsonManagedReference
  public Set<CommandProductEntity> getCommandProducts() {
    return commandProducts;
  }

  public void setCommandProducts(Set<CommandProductEntity> commandProducts) {
    this.commandProducts = commandProducts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductEntity product = (ProductEntity) o;
    return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(description, product.description) && Objects.equals(preparationTime, product.preparationTime) && Objects.equals(photo, product.photo) && Objects.equals(createdAt, product.createdAt) && Objects.equals(updatedAt, product.updatedAt) && Objects.equals(store, product.store) && Objects.equals(commandProducts, product.commandProducts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, description, preparationTime, photo, createdAt, updatedAt, store, commandProducts);
  }

  @Override
  public String toString() {
    return "ProductEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", description='" + description + '\'' +
            ", preparationTime=" + preparationTime +
            ", photo='" + photo + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", store=" + store +
            //", cooks=" + cooks +
            '}';
  }
}
