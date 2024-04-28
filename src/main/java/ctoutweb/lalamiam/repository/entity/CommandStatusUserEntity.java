package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "command_status_pro")
public class CommandStatusUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "command_id")
  private CommandEntity command;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private CommandStatusEntity commandStatus;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "is_pro_action")
  private boolean isProAction;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public CommandStatusUserEntity() {
  }

  public CommandStatusUserEntity(CommandEntity command, CommandStatusEntity commandStatus, UserEntity user, Boolean isProAction) {
    this.command = command;
    this.commandStatus = commandStatus;
    this.user = user;
    this.isProAction = isProAction;
  }

  public CommandStatusUserEntity(CommandEntity command, CommandStatusEntity commandStatus, UserEntity user, Boolean isProAction, LocalDateTime updatedAt) {
    this.command = command;
    this.commandStatus = commandStatus;
    this.user = user;
    this.isProAction = isProAction;
    this.updatedAt = updatedAt;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public CommandEntity getCommand() {
    return command;
  }

  public void setCommand(CommandEntity command) {
    this.command = command;
  }

  public CommandStatusEntity getCommandStatus() {
    return commandStatus;
  }

  public void setCommandStatus(CommandStatusEntity commandStatus) {
    this.commandStatus = commandStatus;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
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

  public boolean getIsProAction() {
    return isProAction;
  }

  public void setIsProAction(boolean proAction) {
    isProAction = proAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandStatusUserEntity that = (CommandStatusUserEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(command, that.command) && Objects.equals(commandStatus, that.commandStatus) && Objects.equals(user, that.user) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, command, commandStatus, user, createdAt, updatedAt);
  }
}
