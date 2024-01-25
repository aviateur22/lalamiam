package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
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

  @Column(name = "frequence_slot_time")
  private Integer frequenceSlotTime;

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

  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
  Set<ScheduleEntity> schedules;


  //////////////////////////////////////////////////////////////////////////////////////////

  public StoreEntity() {
    products = new HashSet<>();
    commands = new HashSet<>();
    schedules = new HashSet<>();

  }

  public StoreEntity(BigInteger storeId) {
    this.id = storeId;
  }

  public StoreEntity(AddStoreDto addStoreDto) {
    this.pro = new ProEntity(addStoreDto.proId());
    this.adress = addStoreDto.adress();
    this.city = addStoreDto.city();
    this.name = addStoreDto.name();
    this.cp = addStoreDto.cp();
    this.frequenceSlotTime = addStoreDto.frequenceSlotTime();

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

  public Integer getFrequenceSlotTime() {
    return frequenceSlotTime;
  }

  public void setFrequenceSlotTime(Integer frequenceSlotTime) {
    this.frequenceSlotTime = frequenceSlotTime;
  }

  public Set<ScheduleEntity> getSchedules() {
    return schedules;
  }

  public void setSchedules(Set<ScheduleEntity> schedules) {
    this.schedules = schedules;
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
            //", schedule=" + schedules +
            //", products=" + products +
            //", commands=" + commands +
            '}';
  }
}
