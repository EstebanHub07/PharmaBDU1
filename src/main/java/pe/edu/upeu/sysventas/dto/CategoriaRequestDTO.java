package pe.edu.upeu.sysventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaRequestDTO {
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(
            min = 3,
            max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres"
    )
    private String nombre;
    @Size(
            max = 200,
            message = "La descripción no debe superar los 200 caracteres"
    )
    private  String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

}