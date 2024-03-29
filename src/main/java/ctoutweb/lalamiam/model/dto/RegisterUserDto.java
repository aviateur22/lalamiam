package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.repository.entity.RoleEntity;

import java.util.List;

/**
 * Renvoie d'un enregistrement d'un nouvel utilisateur
 * @param id Long - identifiant unique
 * @param email String - meil
 * @param roles List<RoleEntity> - Roles de l'utilisateur
 */
public record RegisterUserDto(Long id, String email, List<RoleEntity> roles) {

}
