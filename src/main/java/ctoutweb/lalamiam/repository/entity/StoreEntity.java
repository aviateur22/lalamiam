package ctoutweb.lalamiam.repository.entity;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.ProductRepository;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
  Set<ProductEntity> products;

  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
  Set<CommandEntity> commands;


  /**
   *
   * @param productsId
   * @param productRepository
   * @return
   */
  public boolean areProductsBelongToStore(
          List<ProductWithQuantity> productsInCommand,
          ProductRepository productRepository
  ) throws RuntimeException {
    return productsInCommand
            .stream()
            .map(product-> {
              return Factory.getProduct(product.getProductId())
                      .findProductById(productRepository)
                      .getStore()
                      .getId().equals(id);
            }).allMatch(Boolean::booleanValue);
  }

  public CommandEntity findCommandInList(BigInteger commandId) throws RuntimeException {
    return commands
            .stream()
            .filter(command->command.getId().equals(commandId))
            .findFirst()
            .orElseThrow(()-> new RuntimeException("La commande n'existe pas"));
  }

  //////////////////////////////////////////////////////////////////////////////////////////

  public StoreEntity() {
  }

  public StoreEntity(BigInteger storeId) {
    this.id = storeId;
  }

  public StoreEntity(AddStoreDto addStoreSchema) {
    this.pro = new ProEntity(addStoreSchema.proId());
    this.adress = addStoreSchema.adress();
    this.city = addStoreSchema.city();
    this.name = addStoreSchema.name();
    this.cp = addStoreSchema.cp();
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

  public Set<CommandEntity> getCommands() {
    return commands;
  }

  public void setCommands(Set<CommandEntity> commands) {
    this.commands = commands;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StoreEntity store = (StoreEntity) o;
    return Objects.equals(id, store.id) && Objects.equals(name, store.name) && Objects.equals(presentation, store.presentation) && Objects.equals(adress, store.adress) && Objects.equals(city, store.city) && Objects.equals(cp, store.cp) && Objects.equals(phone, store.phone) && Objects.equals(photo, store.photo) && Objects.equals(createdAt, store.createdAt) && Objects.equals(updatedAt, store.updatedAt) && Objects.equals(pro, store.pro) && Objects.equals(products, store.products) && Objects.equals(commands, store.commands);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, presentation, adress, city, cp, phone, photo, createdAt, updatedAt, pro, products, commands);
  }

  @Override
  public String toString() {
    return "StoreEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", presentation='" + presentation + '\'' +
            ", adress='" + adress + '\'' +
            ", city='" + city + '\'' +
            ", cp='" + cp + '\'' +
            ", phone='" + phone + '\'' +
            ", photo='" + photo + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", pro=" + pro +
            ", products=" + products +
            ", commands=" + commands +
            '}';
  }
}
