package ctoutweb.lalamiam.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {
private static final Logger LOGGER = LogManager.getLogger();
  @ExceptionHandler(value = {CommandException.class})
  public ResponseEntity<String> commandException(CommandException exception) {
    return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
  }
}
