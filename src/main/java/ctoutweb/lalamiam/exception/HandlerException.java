package ctoutweb.lalamiam.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {
  @ExceptionHandler(value = {CommandException.class})
  public ResponseEntity<String> commandException(CommandException exception) {
    return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
  }

  @ExceptionHandler(value = {AuthException.class})
  public ResponseEntity<String> authException(AuthException exception) {
    return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
  }
  @ExceptionHandler(value = {ProductException.class})
  public ResponseEntity<String> productException(ProductException exception) {
    return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
  }

  @ExceptionHandler(value = {ClientException.class})
  public ResponseEntity<String> clientException(ClientException exception) {
    return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
  }
}
