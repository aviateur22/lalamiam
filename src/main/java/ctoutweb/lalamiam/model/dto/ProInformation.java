package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.repository.entity.ProEntity;

import java.time.LocalDateTime;

public record ProInformation(String email, String phone, LocalDateTime createdAt) {
  public ProInformation(ProEntity pro) {
    this(pro.getEmail(), pro.getPhone(), pro.getCreatedAt());
  }
}
