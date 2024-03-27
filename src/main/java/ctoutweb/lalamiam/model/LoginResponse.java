package ctoutweb.lalamiam.model;

import java.util.List;

public class LoginResponse {
  private Long id;
  private String jwt;
  private List<String> roles;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public LoginResponse() {
  }

  public LoginResponse(Long id, String jwt, List<String> roles) {
    this.id = id;
    this.jwt = jwt;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
