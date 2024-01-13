package ctoutweb.lalamiam.repository.entity;

import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "store")
public class StoreEntity {
  @Id
  @SequenceGenerator(name="storePkSeq", sequenceName="STORE_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storePkSeq")
  private BigInteger id;
  @Column(name = "name")
  private String name;

  @Column(name = "presentation")
  private String presentation;

  @Column(name = "adress")
  private String adress;

  @Column(name = "city")
  private String city;

  @Column(name = "cp")
  private String cp;
  @Column(name = "phone")
  private String phone;

  @Column(name = "photo")
  private String photo;
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name="pro_id", nullable=false)
  private ProEntity pro;

  @OneToMany(mappedBy = "store")
  Set<ProductEntity> products;

  @OneToMany(mappedBy = "store")
  Set<CookEntity> cooks;

  /**
   *
   */
  public StoreEntity() {
  }

  public StoreEntity(AddStoreSchema addStoreSchema) {
    this.pro = new ProEntity(addStoreSchema.proId());
    this.adress = addStoreSchema.Adress();
    this.city = addStoreSchema.city();
    this.name = addStoreSchema.name();
    this.cp = addStoreSchema.cp();
  }

  public StoreEntity(BigInteger storeId) {
    this.id = storeId;
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

  public String getPresentation() {
    return presentation;
  }

  public void setPresentation(String presentation) {
    this.presentation = presentation;
  }

  public String getAdress() {
    return adress;
  }

  public void setAdress(String adress) {
    this.adress = adress;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public ProEntity getPro() {
    return pro;
  }

  public void setPro(ProEntity pro) {
    this.pro = pro;
  }

  public Set<ProductEntity> getProducts() {
    return products;
  }

  public void setProducts(Set<ProductEntity> products) {
    this.products = products;
  }

  public Set<CookEntity> getCooks() {
    return cooks;
  }

  public void setCooks(Set<CookEntity> cooks) {
    this.cooks = cooks;
  }
}
