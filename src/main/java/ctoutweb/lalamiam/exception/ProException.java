package ctoutweb.lalamiam.exception;

import org.springframework.http.HttpStatus;

public class ProException extends RuntimeException {
  private HttpStatus status;
  public ProException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
