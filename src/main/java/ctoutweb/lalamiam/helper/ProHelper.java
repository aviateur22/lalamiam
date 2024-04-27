package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.UserRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import ctoutweb.lalamiam.security.authentication.UserPrincipal;
import ctoutweb.lalamiam.service.StoreService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProHelper {

  private final UserRepository userRepository;
  private final StoreService storeService;
  private final CommandRepository commandRepository;

  public ProHelper(
          UserRepository userRepository,
          StoreService storeService, CommandRepository commandRepository
  ) {
    this.userRepository = userRepository;
    this.storeService = storeService;
    this.commandRepository = commandRepository;
  }

  /**
   * Verification si command appartient au commerce
   * @param proId Long - Identifiant pro
   * @param commandId Long - Identifiant Commande
   * @return Booelan
   */
  public Boolean isCommandVisibleByPro(Long proId, Long commandId) {

    // Recherche la liste des stores lié au professionel // employé
    List<Long> storeIdList = storeService.getAllStoreByPro(proId);

    // verifie si la commande est rattaché à l'un des stores
    Optional<CommandEntity> command = commandRepository.findOneCommandByStoreIn(
            storeIdList
                    .stream()
                    .map(storeId->Factory.getStore(storeId))
                    .collect(Collectors.toList())
    );

    return command.isPresent();
  }

  /**
   * Verification Profesionnel
   * @param proId Long - Identifiant pro
   * @return booelan
   */
  public boolean isProfessionalValid(Long proId){

    UserPrincipal userFromJwt = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if(!userFromJwt.getId().equals(proId))
      return false;

    Collection<? extends GrantedAuthority> authorities = userFromJwt.getAuthorities();

    if(authorities == null || authorities.isEmpty())
      return false;

    boolean isProAuthorityValid = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals("ROLE_PRO"));

    Optional<UserEntity> findPro = userRepository.findById(proId);

    // Si utilisateur non trouvé
    if(!findPro.isPresent())
      return false;

    // Verification Role en base de donnée
    if(!findPro.get().getRoles().contains("ROLE_PRO"))
      return false;

    return true;
  }

  /**
   * Verification si professionel travail dans commerce
   * @param proId Long - IDentifiant professionel
   * @param storeId Long - identifiant store
   * @return Boolean
   */
  public boolean isProWorkingInStore(Long proId, Long storeId) {
    // Recherche la liste des stores lié au professionel // employé
    List<Long> storeIdList = storeService.getAllStoreByPro(proId);

    return storeIdList.contains(storeId);
  }
}
