package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity {
  @Id
  private Integer id;
  private String name;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public RoleEntity() {
  }

  public RoleEntity(Integer id) {
    this.id = id;
  }
  public RoleEntity(Integer id, String name) {
    this.id = id;
    this.name = name;
  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
