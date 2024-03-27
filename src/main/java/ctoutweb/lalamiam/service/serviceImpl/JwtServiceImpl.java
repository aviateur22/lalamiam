package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.JwtIssue;
import ctoutweb.lalamiam.repository.JwtUserRepository;
import ctoutweb.lalamiam.repository.entity.JwtUserEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import ctoutweb.lalamiam.service.JwtService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JwtServiceImpl implements JwtService {

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
  public Boolean isJwtValid(Long userId, String jwtToken) {
    // Todo Faire Test
    if(!StringUtils.isNotBlank(jwtToken) || userId < 0) return false;

    UserEntity user = Factory.getUSer(userId);
    JwtUserEntity jwtUserEntity = Factory.getJwtUserToFind(user, jwtToken);
    return jwtUserRepository.findOneByUserAndJwt(user, jwtUserEntity.getJwt())
      .map(findJwtUser -> findJwtUser.getExpiredAt().isBefore(LocalDateTime.now()) && findJwtUser.getIsValid())
      .orElse(false);
  }

  @Override
  public void deleteJwt(Long userId) {
    UserEntity user = Factory.getUSer(userId);

    jwtUserRepository.findOneByUser(user).ifPresent((jwtToken)->jwtUserRepository.delete(jwtToken));
  }
}
