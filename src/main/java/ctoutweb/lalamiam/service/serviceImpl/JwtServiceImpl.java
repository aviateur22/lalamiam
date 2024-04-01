package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.JwtIssue;
import ctoutweb.lalamiam.repository.JwtUserRepository;
import ctoutweb.lalamiam.repository.entity.JwtUserEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import ctoutweb.lalamiam.service.JwtService;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {
  private static final Logger LOGGER = LogManager.getLogger();
  private final JwtUserRepository jwtUserRepository;
  public JwtServiceImpl(JwtUserRepository jwtUserRepository) {
    this.jwtUserRepository = jwtUserRepository;
  }

  @Override
  public void saveJwt(Long userId, JwtIssue jwt) {
    // Todo Faire Test
    UserEntity user = Factory.getUSer(userId);
    JwtUserEntity jwtUserToSave = Factory.createJwtUserToSave(user, jwt);

    // Verification existance
    jwtUserRepository.findOneByUser(user)
            .ifPresentOrElse((jwtToken)->{
              jwtUserRepository.delete(jwtToken);
              jwtUserRepository.save(jwtUserToSave);
              },()->jwtUserRepository.save(jwtUserToSave));
  }

  @Override
  public Boolean isJwtValid(Long userId, String jwtToken, String jwtId) {
    // Todo Faire Test
    if(!StringUtils.isNotBlank(jwtId) || userId < 0) return false;

    return this.findUserJwt(userId)
      .map(findJwtUser -> findJwtUser.getIsValid() && findJwtUser.getJwtId().equalsIgnoreCase(jwtId))
      .orElse(false);
  }

  @Override
  public void deleteJwt(Long userId) {
    UserEntity user = Factory.getUSer(userId);
    jwtUserRepository.findOneByUser(user).ifPresent((jwtToken)->jwtUserRepository.delete(jwtToken));
  }

  /**
   * Renvoie un JwtUserEntity
   * @param userId Long
   * @return JwtUserEntity
   */
  public Optional<JwtUserEntity> findUserJwt(Long userId) {
    //Todo test
    UserEntity user = Factory.getUSer(userId);
    Optional<JwtUserEntity> jwtUser = jwtUserRepository.findOneByUser(user);
    return jwtUser;

  }
}
