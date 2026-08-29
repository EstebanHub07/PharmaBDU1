package pe.edu.upeu.sysventas.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
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
    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;
    @NotNull(message = "El stock es obligatorio")
    private Integer stock;
    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoriaId;
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
