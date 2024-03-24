package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.LoginResponse;
import ctoutweb.lalamiam.model.dto.LoginDto;
import ctoutweb.lalamiam.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }


  @PostMapping("/login")
  ResponseEntity<LoginResponse> login(@RequestBody LoginDto login) {
    LoginResponse loginResponse = authService.login(login);
    return new ResponseEntity<>(loginResponse, HttpStatus.OK);
  }
}
