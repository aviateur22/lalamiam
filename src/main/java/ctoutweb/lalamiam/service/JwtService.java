package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.JwtIssue;

public interface JwtService {

  /**
   * Persistance d'un JWT
   * @param userId Long - Identitifiant utilisateur
   * @param jwt  JwtIssue - JWT
   */
  public void saveJwt(Long userId, JwtIssue jwt);

  /**
   * Vérification JWT
   * @param jwt String - JWT
   * @param userId Long - Identitifiant utilisateur
   * @return Boolean
   */
  public Boolean isJwtValid(Long userId, String jwt);

  /**
   * Suppression d'un JWT
   * @param userId Long - Identifiant utilisateur
   */
  public void deleteJwt(Long userId);



}
