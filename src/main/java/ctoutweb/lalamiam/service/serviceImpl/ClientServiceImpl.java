package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.repository.UserRepository;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import ctoutweb.lalamiam.service.ClientService;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

  private final UserRepository userRepository;

  public ClientServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<UserEntity> findClient(Long clientId) {
    return userRepository.findById(clientId);
  }

}
