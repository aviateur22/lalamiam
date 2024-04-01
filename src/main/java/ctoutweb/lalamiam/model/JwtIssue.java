package ctoutweb.lalamiam.model;

import java.time.LocalDateTime;

public class JwtIssue {
  private String jwtId;
  private String jwtToken;
  private LocalDateTime expiredAt;

  public JwtIssue(String token, LocalDateTime expiredAt, String jwtId) {
    this.jwtToken = token;
    this.expiredAt = expiredAt;
    this.jwtId = jwtId;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public LocalDateTime getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(LocalDateTime expiredAt) {
    this.expiredAt = expiredAt;
  }

  public String getJwtId() {
    return jwtId;
  }

  public void setJwtId(String jwtId) {
    this.jwtId = jwtId;
  }
}
