package exception.global;

import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.exception.auth.InvalidCredentialsException;
import com.renan.refyne.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Email/CPF/CNPJ DUPLICATED ACCOUNT HANDLER
  @ExceptionHandler(UserAlreadyInUseException.class)
  public ResponseEntity<Object> handleUserExists(UserAlreadyInUseException ex) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(Map.of(
        "error", ex.getError(),
        "message", ex.getMessage()
      ));
  }

  // INVALID CREDENTIALS HANDLER
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(Map.of(
        "error", "INVALID_CREDENTIALS",
        "message", ex.getMessage()
      ));
  }

  // USER NOT FOUND HANDLER
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(Map.of(
        "error", "USER_NOT_FOUND",
        "message", ex.getMessage()
      ));
  }
}
