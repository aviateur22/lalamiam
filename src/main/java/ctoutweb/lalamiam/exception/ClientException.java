package ctoutweb.lalamiam.exception;

import org.springframework.http.HttpStatus;

public class ClientException extends RuntimeException {
  private HttpStatus status;
  public ClientException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
