package pe.edu.upeu.sysventas.security;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.sysventas.enums.Permiso;

import static org.assertj.core.api.Assertions.assertThat;

class PermisosPorRolTest {

    @Test
    void adminTieneTodosLosPermisos() {
        assertThat(PermisosPorRol.de("ADMIN"))
                .containsExactlyInAnyOrder(Permiso.values());
    }

    @Test
    void vendedorNoPuedeModificarCatalogo() {
        assertThat(PermisosPorRol.de("VENDEDOR"))
                .doesNotContain(Permiso.CATALOGO_ESCRIBIR);
    }
}
