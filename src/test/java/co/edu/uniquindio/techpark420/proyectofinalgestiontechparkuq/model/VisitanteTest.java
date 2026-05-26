package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;

/**
 * Pruebas unitarias para la clase Visitante.
 * Autores: [Nombres de los integrantes del grupo]
 */
class VisitanteTest {

    private Visitante visitante;

    @BeforeEach
    void setUp() {
        visitante = new Visitante("V01", "María López", "987654321", 25, 1.65);
        visitante.setSaldoVirtual(50000);
    }


    @Test
    void recargarSaldo_valorPositivo_aumentaSaldo() {
        visitante.recargarSaldo(20000);
        assertEquals(70000, visitante.getSaldoVirtual(), 0.01);
    }


    @Test
    void realizarPago_saldoInsuficiente_retornaFalsoSinDescontar() {
        assertFalse(visitante.realizarPago(60000));
        assertEquals(50000, visitante.getSaldoVirtual(), 0.01);
    }
}