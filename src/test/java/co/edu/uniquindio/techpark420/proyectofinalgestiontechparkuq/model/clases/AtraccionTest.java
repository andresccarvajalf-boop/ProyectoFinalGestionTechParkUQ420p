package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;


import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Atraccion.
 * Autores: [Nombres de los integrantes del grupo]
 */
class AtraccionTest {

    private Atraccion atraccion;
    private Visitante visitanteValido;

    @BeforeEach
    void setUp() {
        atraccion = new Atraccion("A01", "Montaña Rusa", TipoAtraccion.MECANICA, 30);
        atraccion.setAlturaMinima(1.40);
        atraccion.setEdadMinima(12);

        visitanteValido = new Visitante("V01", "Carlos", "123456", 15, 1.50);
    }

    // Prueba 01 — Visitante válido accede a atracción activa
    @Test
    void validarAcceso_visitanteCumpleRequisitos_retornaVerdadero() {
        assertTrue(atraccion.validarAcceso(visitanteValido));
    }

    // Prueba 02 — Acceso rechazado por edad insuficiente
    @Test
    void validarAcceso_edadInsuficiente_retornaFalso() {
        Visitante menor = new Visitante("V02", "Nina", "999", 8, 1.50);
        assertFalse(atraccion.validarAcceso(menor));
    }

    // Prueba 03 — Mantenimiento preventivo al alcanzar umbral (umbral = 3 en el código de ejemplo)
    @Test
    void verificarMantenimiento_alLlegarAlUmbral_cambiaAMantenimiento() {
        atraccion.setVisitantesAcumulados(3);
        atraccion.verificarMantenimiento();
        assertEquals(EstadoAtraccion.EN_MANTENIMIENTO, atraccion.getEstado());
    }

    // Prueba 04 — Revisión aprobada reactiva atracción y reinicia contador
    @Test
    void registrarRevision_aprobada_reactivaAtraccionYReiniciaContador() {
        atraccion.cambiarEstado(EstadoAtraccion.EN_MANTENIMIENTO);
        atraccion.setVisitantesAcumulados(3);

        RevisionTecnica revision = new RevisionTecnica();
        revision.aprobarRevision();
        atraccion.registrarRevision(revision);

        assertEquals(EstadoAtraccion.ACTIVA, atraccion.getEstado());
        assertEquals(0, atraccion.getVisitantesAcumulados());
    }

    // Prueba 05 — Revisión nula no lanza NullPointerException
    @Test
    void registrarRevision_nula_noLanzaExcepcion() {
        assertDoesNotThrow(() -> atraccion.registrarRevision(null));
    }

    // Prueba 06 — Cierre por alerta climática cambia estado y guarda motivo
    @Test
    void cerrarPorClima_conAlerta_cambiaACerradaYGuardaMotivo() {
        AlertaClimatica alerta = new AlertaClimatica("AL01", "Tormenta eléctrica", "Tormenta fuerte", null);

        atraccion.cerrarPorClima(alerta);

        assertEquals(EstadoAtraccion.CERRADA, atraccion.getEstado());
        assertTrue(atraccion.getMotivoCierre().contains("Tormenta eléctrica"));
    }
}