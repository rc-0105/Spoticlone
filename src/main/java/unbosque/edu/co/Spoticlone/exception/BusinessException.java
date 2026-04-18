package unbosque.edu.co.Spoticlone.exception;

/**
 * Excepción de negocio lanzada por los repositorios cuando un stored procedure
 * o una validación de BD falla. Lleva el código HTTP apropiado (400 o 409).
 */
public class BusinessException extends RuntimeException {

    private final int statusCode;

    public BusinessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /** Inferencia automática: mensajes de duplicado → 409, resto → 400. */
    public BusinessException(String message) {
        super(message);
        this.statusCode = inferStatus(message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    private static int inferStatus(String message) {
        if (message == null) return 400;
        String lower = message.toLowerCase();
        if (lower.contains("ya existe") || lower.contains("ya está registrado")
                || lower.contains("duplicado") || lower.contains("already exists")
                || lower.contains("unique") || lower.contains("ya se encuentra")) {
            return 409;
        }
        return 400;
    }
}
