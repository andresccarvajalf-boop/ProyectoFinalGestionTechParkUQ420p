package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;

/**
 * Pruebas unitarias para la clase Zona.
 * Autores: [Nombres de los integrantes del grupo]
 */
class ZonaTest {

    private Zona zona;

    @BeforeEach
    void setUp() {
        zona = new Zona("Z01", "Zona Aventura", 5);
    }

    // Prueba 12 — Zona llena rechaza ingreso de nuevo visitante
    @Test
    void registrarIngreso_zonaLlena_retornaFalso() {
        for (int i = 0; i < 5; i++) {
            zona.registrarIngresoVisitante();
        }
        assertFalse(zona.registrarIngresoVisitante());
        assertEquals(5, zona.getVisitantesActuales());
    }

    // Prueba 13 — Solo atracciones ACTIVAS aparecen en la lista
    @Test
    void obtenerAtraccionesActivas_mixDeEstados_soloRetornaActivas() {
        Atraccion activa = new Atraccion("A01", "Activa", TipoAtraccion.MECANICA, 10);
        activa.setEstado(EstadoAtraccion.ACTIVA);

        Atraccion cerrada = new Atraccion("A02", "Cerrada", TipoAtraccion.ACUATICA, 5);
        cerrada.setEstado(EstadoAtraccion.CERRADA);

        Atraccion enMantenimiento = new Atraccion("A03", "Mantenimiento", TipoAtraccion.MECANICA, 8);
        enMantenimiento.setEstado(EstadoAtraccion.EN_MANTENIMIENTO);

        zona.agregarAtraccion(activa);
        zona.agregarAtraccion(cerrada);
        zona.agregarAtraccion(enMantenimiento);

        List<Atraccion> activas = zona.obtenerAtraccionesActivas();
        assertIterableEquals(List.of(activa), activas);
    }
}