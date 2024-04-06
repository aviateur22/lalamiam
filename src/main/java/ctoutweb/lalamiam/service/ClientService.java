package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.repository.entity.UserEntity;

import java.util.Optional;

public interface ClientService {
  /**
   * Renvoie les données d'une personnee
   * @param clientId
   * @return Optional<UserEntity>
   */
  public Optional<UserEntity> findClient(Long clientId);
}
