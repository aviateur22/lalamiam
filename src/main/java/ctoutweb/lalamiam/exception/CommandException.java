package ctoutweb.lalamiam.exception;

import org.springframework.http.HttpStatus;

public class CommandException extends RuntimeException {

  private HttpStatus httpStatus;
  public CommandException(String message, HttpStatus status) {
    super(message);
    this.httpStatus = status;
  }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }


}
