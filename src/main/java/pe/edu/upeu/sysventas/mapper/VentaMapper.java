package pe.edu.upeu.sysventas.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.dto.DetalleVentaResponseDTO;
import pe.edu.upeu.sysventas.dto.VentaResponseDTO;
import pe.edu.upeu.sysventas.model.DetalleVenta;
import pe.edu.upeu.sysventas.model.Venta;

import java.util.List;

/*
 * Requiere que cliente, detalles y detalles.producto ya estén cargados
 * (fetch join / entity graph); no dispara consultas por su cuenta.
 */
@Component
public class VentaMapper {

    public VentaResponseDTO toResponse(Venta venta) {

        List<DetalleVentaResponseDTO> detalles =
                venta.getDetalles()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        String clienteNombre =
                venta.getCliente().getNombres()
                        + " "
                        + venta.getCliente().getApellidos();

        return new VentaResponseDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getCliente().getId(),
                clienteNombre,
                venta.getEstado().name(),
                venta.getTotal(),
                detalles
        );
    }

    public DetalleVentaResponseDTO toResponse(DetalleVenta detalle) {
        return new DetalleVentaResponseDTO(
                detalle.getId(),
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecio(),
                detalle.getSubtotal()
        );
    }
}