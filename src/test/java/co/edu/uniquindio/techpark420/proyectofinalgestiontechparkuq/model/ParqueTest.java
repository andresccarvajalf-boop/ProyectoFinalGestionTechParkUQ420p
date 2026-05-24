package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.TicketGeneral;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;

class ParqueTest {

    private Parque parque;

    @BeforeEach
    void setUp() {
        parque = new Parque("Tech-Park UQ", 100);
    }

    // Prueba 07 — Parque lleno bloquea venta de ticket
    @Test
    void venderTicket_aforoLleno_retornaFalso() {
        parque.setCapacidadMaxima(2);
        parque.setVisitantesActuales(2);

        TicketGeneral ticket = new TicketGeneral();
        assertFalse(parque.venderTicket(ticket));
    }

    // Prueba 08 — Cálculo de ingresos diarios suma precios de tickets vendidos
    @Test
    void calcularIngresosDiarios_conDosTickets_retornaSumaCorrecta() {
        TicketGeneral t1 = new TicketGeneral();
        t1.setPrecio(30000);

        TicketGeneral t2 = new TicketGeneral();
        t2.setPrecio(20000);

        parque.venderTicket(t1);
        parque.venderTicket(t2);

        assertEquals(50000, parque.calcularIngresosDiarios(), 0.01);
    }

    // Prueba 09 — Búsqueda de zona insensible a mayúsculas
    @Test
    void buscarZona_insensibleAMayusculas_retornaZonaCorrecta() {
        Zona zona = new Zona("Z01", "Zona Norte", 30);
        parque.agregarZona(zona);

        assertNotNull(parque.buscarZona("zona norte"));
    }
}