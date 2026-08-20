package pe.edu.upeu.sysventas.Exception;

public class ReglaNegocioException extends RuntimeException{
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
