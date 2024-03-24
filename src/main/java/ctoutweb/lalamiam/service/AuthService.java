package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.LoginResponse;
import ctoutweb.lalamiam.model.dto.LoginDto;

public interface AuthService {
  public LoginResponse login(LoginDto login);
}
