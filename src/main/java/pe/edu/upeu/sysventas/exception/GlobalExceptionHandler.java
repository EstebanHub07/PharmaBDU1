package pe.edu.upeu.sysventas.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import pe.edu.upeu.sysventas.config.RequestIdFilter;
import pe.edu.upeu.sysventas.exception.dto.ErrorResponseDTO;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte las excepciones en ErrorResponseDTO. Toda respuesta de error lleva
 * el requestId que RequestIdFilter dejó en el MDC, el mismo que aparece en el
 * log y en la cabecera X-Request-Id.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthentication(
            AuthenticationException ex, HttpServletRequest request) {
        return responder(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Usuario o contraseña incorrectos", request, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        return responder(HttpStatus.FORBIDDEN, "Forbidden",
                "No tiene permisos para realizar esta acción", request, null);
    }

    /* Recurso no encontrado - HTTP 404 */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            RecursoNoEncontradoException ex, HttpServletRequest request) {
        return responder(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request, null);
    }

    /* Regla de negocio - HTTP 409 */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRule(
            ReglaNegocioException ex, HttpServletRequest request) {
        return responder(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request, null);
    }

    /* Errores de @Valid - HTTP 400 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage()));
        return responder(HttpStatus.BAD_REQUEST, "Bad Request",
                "Existen errores de validación", request, validationErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, "Bad Request",
                "El valor '" + ex.getValue() + "' no es válido para el parámetro '"
                        + ex.getName() + "'", request, null);
    }

    /* JSON mal formado o tipos incompatibles en el cuerpo - HTTP 400 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, "Bad Request",
                "El cuerpo de la petición no es válido", request, null);
    }

    /* Clave foránea, por ejemplo eliminar una categoría que tiene productos - HTTP 409 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Violación de integridad en {}: {}", request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());
        return responder(HttpStatus.CONFLICT, "Conflict",
                "La operación no es posible porque el registro tiene datos relacionados",
                request, null);
    }

    /*
     * Cualquier otro error. Las excepciones de Spring que ya traen su código
     * (ResponseStatusException de la paginación, 404 de recurso, 405, parámetro
     * faltante...) conservan ese código; sin este manejador irían a /error,
     * fuera de RequestIdFilter, y perderían el requestId. El resto es un 500
     * que no expone detalles internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpected(
            Exception ex, HttpServletRequest request) {
        if (ex instanceof ErrorResponse er) {
            HttpStatusCode codigo = er.getStatusCode();
            String mensaje = er.getBody().getDetail() != null
                    ? er.getBody().getDetail()
                    : ex.getMessage();
            return responder(codigo, razon(codigo), mensaje, request, null);
        }
        log.error("Error no controlado en {} {}", request.getMethod(),
                request.getRequestURI(), ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ocurrió un error inesperado. Comunique el código de seguimiento a soporte.",
                request, null);
    }

    private ResponseEntity<ErrorResponseDTO> responder(
            HttpStatusCode estado, String error, String mensaje,
            HttpServletRequest request, Map<String, String> validationErrors) {
        ErrorResponseDTO cuerpo = new ErrorResponseDTO(
                LocalDateTime.now(), estado.value(), error, mensaje,
                request.getRequestURI(), validationErrors,
                MDC.get(RequestIdFilter.CLAVE_MDC));
        return ResponseEntity.status(estado).body(cuerpo);
    }

    private static String razon(HttpStatusCode codigo) {
        HttpStatus estado = HttpStatus.resolve(codigo.value());
        return estado != null ? estado.getReasonPhrase() : "Error";
    }
}
