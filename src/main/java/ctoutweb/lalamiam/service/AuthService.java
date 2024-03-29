package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.LoginResponse;
import ctoutweb.lalamiam.model.dto.LoginDto;
import ctoutweb.lalamiam.model.dto.RegisterDto;
import ctoutweb.lalamiam.model.dto.RegisterUserDto;

public interface AuthService {
  /**
   *
   * @param loginDto LoginDto
   * @return LoginResponse
   */
  public LoginResponse login(LoginDto loginDto);

  /**
   *
   * @param registerDto RegisterDto
   * @return RegisterResponse
   */
  public RegisterUserDto register(RegisterDto registerDto);
}
