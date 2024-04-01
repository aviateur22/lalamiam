package ctoutweb.lalamiam.model.dto;

/**
 * Données Pour créer un compte
 * @param email String
 * @param password String
 * @param confirmPassword String
 */
public record RegisterDto(String email, String password, String confirmPassword) {
}
