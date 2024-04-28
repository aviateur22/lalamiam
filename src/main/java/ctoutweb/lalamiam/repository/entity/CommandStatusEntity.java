package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "command_status")
public class CommandStatusEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Generated(GenerationTime.INSERT)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "status_name")
  private String statusName;
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "commandStatus")
  private List<CommandEntity> commands;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  public CommandStatusEntity() {}

  public CommandStatusEntity(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<CommandEntity> getCommands() {
    return commands;
  }

  public void setCommands(List<CommandEntity> commands) {
    this.commands = commands;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandStatusEntity that = (CommandStatusEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) && Objects.equals(statusName, that.statusName) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(commands, that.commands);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, statusName, updatedAt, commands);
  }

  @Override
  public String toString() {
    return "StatusEntity{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", statusName='" + statusName + '\'' +
            ", updatedAt=" + updatedAt +
            ", commands=" + commands +
            '}';
  }
}
