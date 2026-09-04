package pe.edu.upeu.sysventas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class HealthController {
    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healt(){
        return ResponseEntity.ok("Backend funcionando correctamente");
    }

    @GetMapping("/health/db")
    public ResponseEntity<String> databaseHealth() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return ResponseEntity.ok("Conexion con la base de datos correcta");
            }
            return ResponseEntity.internalServerError().body("No se pudo validar la conexion con la base de datos");
        }
    }
}
