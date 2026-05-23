package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.ColaVirtual;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;

/**
 * Pruebas unitarias para la clase ColaVirtual.
 * Autores: [Nombres de los integrantes del grupo]
 */
class ColaVirtualTest {

    private ColaVirtual cola;

    @BeforeEach
    void setUp() {
        Atraccion atraccion = new Atraccion("A01", "Montaña Rusa", TipoAtraccion.MECANICA, 30);
        cola = new ColaVirtual("C01", 10, atraccion);
    }

    private Visitante crearVisitante(String id) {
        return new Visitante(id, "Visitante " + id, "DOC" + id, 20, 1.60);
    }

    // Prueba 14 — FastPass tiene prioridad sobre visitante en cola normal
    @Test
    void obtenerSiguienteVisitante_conFastPassYNormal_priorizaFastPass() {
        Visitante normal = crearVisitante("VN1");
        Visitante fastPass = crearVisitante("VFP1");

        cola.agregarVisitante(normal);
        cola.agregarFastPass(fastPass);

        assertEquals(fastPass, cola.obtenerSiguienteVisitante());
    }

    // Prueba 15 — Tiempo de espera es proporcional a personas en cola (5 min por persona)
    @Test
    void calcularTiempoEspera_dosPersonas_retorna10Minutos() {
        cola.agregarVisitante(crearVisitante("VN2"));
        cola.agregarVisitante(crearVisitante("VN3"));

        assertEquals(10, cola.calcularTiempoEspera());
    }
}