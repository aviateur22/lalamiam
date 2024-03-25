package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.model.LoginResponse;
import ctoutweb.lalamiam.model.dto.LoginDto;
import ctoutweb.lalamiam.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  @Override
  public LoginResponse login(LoginDto login) {
    return null;
  }
}
