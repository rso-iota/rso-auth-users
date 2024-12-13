package rso.iota.authsvc.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import rso.iota.authsvc.dto.OutError;
import rso.iota.authsvc.dto.error.BadRequestException;
import rso.iota.authsvc.dto.error.NotFoundException;

import java.util.List;
import java.util.Set;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ErrorHandlerConfig  {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return createAndLogWarning(e.getMessage());
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return createAndLogWarning(String.format("Error converting input string '%s' for '%s' URL parameter.",
                e.getValue(),
                e.getName()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String message;

        try {
            throw e.getCause();
        } catch (UnrecognizedPropertyException ex) {
            message = String.format("Unrecognized property '%s'", ex.getPropertyName());
        } catch (InvalidFormatException ex) {
            message = String.format("Could not deserialize property value '%s' to type %s.",
                    ex.getValue(),
                    ex.getTargetType().getName());
        } catch (Throwable ex) {
            message = null;
        }

        if (message == null && e.getMessage() != null) {
            if (e.getMessage().startsWith("Required request body is missing")) {
                message = "Required request body is missing.";
            }
        }

        if (message == null || message.isEmpty()) {
            message = "Malformed request.";
        }

        return createAndLogWarning(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        StringBuilder messageBuilder = new StringBuilder();
        for (FieldError error : errors) {
            messageBuilder.append("Field '")
                    .append(error.getField())
                    .append("' ")
                    .append(error.getDefaultMessage())
                    .append(", ");
        }
        messageBuilder.delete(messageBuilder.length() - 2, messageBuilder.length());

        return createAndLogWarning(messageBuilder.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleConstraintViolationException(ConstraintViolationException e) {

        String errorMsg = e.getMessage();

        if (e.getConstraintViolations() != null) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            StringBuilder strBuilder = new StringBuilder();
            for (ConstraintViolation<?> violation : violations) {
                strBuilder.append("'")
                        .append(violation.getRootBean().toString())
                        .append("' ")
                        .append(violation.getMessage())
                        .append(", ");
            }
            strBuilder.delete(strBuilder.length() - 2, strBuilder.length());
            errorMsg = strBuilder.toString();
        }

        return createAndLogWarning(errorMsg);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleIllegalArgumentException(IllegalArgumentException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleBadRequestException(BadRequestException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OutError handleMissingRequestHeaderExceptionException(MissingRequestHeaderException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OutError handleNoHandlerFoundException(NoHandlerFoundException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OutError handleResourceNotFoundException(NotFoundException e) {
        return createAndLogWarning(e.getMessage());
    }


    // TODO: Handle 401 (missing x - )

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public OutError handle405(HttpRequestMethodNotSupportedException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public OutError handleUnsupportedMediaTypeStatusException(UnsupportedMediaTypeStatusException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public OutError handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return createAndLogWarning(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OutError handle500(Exception e) {
        return createAndLogError(e);
    }

    private OutError createAndLogError(Exception e) {
        OutError outError = OutError.builder()
                .message("Internal server error. If the error persists please contact support with the given error id.")
                .build();
        MDC.put("error_id", outError.getId().toString());
        log.error(e.getMessage(), e);
        MDC.clear();

        return outError;
    }

    private OutError createAndLogWarning(String message) {
        OutError errorOut = OutError.builder().message(message).build();
        MDC.put("error_id", errorOut.getId().toString());
        log.warn(message);
        MDC.clear();

        return errorOut;
    }
}
