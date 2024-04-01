package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.repository.entity.UserEntity;

import java.time.LocalDateTime;

public record ProInformationDto(Long id, String email, String phone, LocalDateTime createdAt) {
  public ProInformationDto(UserEntity pro) {
    this(pro.getId(), pro.getEmail(), pro.getPhone(), pro.getCreatedAt());
  }
}
