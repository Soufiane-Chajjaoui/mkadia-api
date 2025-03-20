package fr.mkadia.mkadiaapi.exceptions;

import fr.mkadia.mkadiaapi.models.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleAuthenticationException(AuthenticationException e){
        if (e instanceof BadCredentialsException){
            ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED , e.getMessage());
            errorDetails.setProperty("message" , "Your email or secretKey incorrect. Please ");
            return errorDetails;
        }
        return null;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN , e.getMessage());
        errorDetails.setProperty("message" , "You are Not Authorize for this Resources");
        return errorDetails;
    }

    @ExceptionHandler(UnsupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ProblemDetail FileForbiddenException(UnsupportedException e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE , e.getMessage());
        errorDetails.setProperty("message" , e.getMessage());
        return errorDetails;
    }
    @ExceptionHandler(EntityExistedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseError> handlEntityExisted(EntityExistedException e){
        ResponseError responseError = ResponseError.builder().message(e.getMessage())
                .debugMessage(e.getLocalizedMessage())
                .success(false)
                .status(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseError> handleEntityNotFound(EntityNotFoundException e){
        ResponseError responseError = ResponseError.builder().message(e.getMessage())
                .debugMessage(e.getLocalizedMessage())
                .success(false)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleJwtException(JwtException ex) {
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED , ex.getMessage());
        errorDetails.setProperty("message" , "Your Session Has Expired, Go to Authentication Page");
        errorDetails.setTitle("Expiration SESSION");
        errorDetails.setType(URI.create("Expiration"));
        return errorDetails;
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , String>> handleNotValidArg(MethodArgumentNotValidException e){
        return new ResponseEntity<Map<String, String>>(e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField , FieldError::getDefaultMessage)) , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ProblemDetail handlePasswordIncorrectException(PasswordIncorrectException e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND , e.getMessage());
        errorDetails.setProperty("message" , "Password is Not match previous secretKey");
        return errorDetails;
    }

    @ExceptionHandler(VerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleVerificationOtp(VerificationException e){
        ResponseError responseError = ResponseError.builder().message(e.getMessage())
                .debugMessage(e.getLocalizedMessage())
                .success(false)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }
}
