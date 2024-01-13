package ctoutweb.lalamiam.repository.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

public final class StoreEntityBuilder {
  private BigInteger id;
  private String name;
  private String presentation;
  private String adress;
  private String city;
  private String cp;
  private String phone;
  private String photo;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private ProEntity pro;
  private Set<ProductEntity> products;
  private Set<CookEntity> cooks;

  private StoreEntityBuilder() {
  }

  public static StoreEntityBuilder aStoreEntity() {
    return new StoreEntityBuilder();
  }

  public StoreEntityBuilder withId(BigInteger id) {
    this.id = id;
    return this;
  }

  public StoreEntityBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public StoreEntityBuilder withPresentation(String presentation) {
    this.presentation = presentation;
    return this;
  }

  public StoreEntityBuilder withAdress(String adress) {
    this.adress = adress;
    return this;
  }

  public StoreEntityBuilder withCity(String city) {
    this.city = city;
    return this;
  }

  public StoreEntityBuilder withCp(String cp) {
    this.cp = cp;
    return this;
  }

  public StoreEntityBuilder withPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public StoreEntityBuilder withPhoto(String photo) {
    this.photo = photo;
    return this;
  }

  public StoreEntityBuilder withCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public StoreEntityBuilder withUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public StoreEntityBuilder withPro(ProEntity pro) {
    this.pro = pro;
    return this;
  }

  public StoreEntityBuilder withProducts(Set<ProductEntity> products) {
    this.products = products;
    return this;
  }

  public StoreEntityBuilder withCooks(Set<CookEntity> cooks) {
    this.cooks = cooks;
    return this;
  }

  public StoreEntity build() {
    StoreEntity storeEntity = new StoreEntity();
    storeEntity.setId(id);
    storeEntity.setName(name);
    storeEntity.setPresentation(presentation);
    storeEntity.setAdress(adress);
    storeEntity.setCity(city);
    storeEntity.setCp(cp);
    storeEntity.setPhone(phone);
    storeEntity.setPhoto(photo);
    storeEntity.setCreatedAt(createdAt);
    storeEntity.setUpdatedAt(updatedAt);
    storeEntity.setPro(pro);
    storeEntity.setProducts(products);
    storeEntity.setCooks(cooks);
    return storeEntity;
  }
}
