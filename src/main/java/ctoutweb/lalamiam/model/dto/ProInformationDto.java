package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.repository.entity.ProEntity;

import java.time.LocalDateTime;

public record ProInformationDto(Long id, String email, String phone, LocalDateTime createdAt) {
  public ProInformationDto(ProEntity pro) {
    this(pro.getId(), pro.getEmail(), pro.getPhone(), pro.getCreatedAt());
  }
}
