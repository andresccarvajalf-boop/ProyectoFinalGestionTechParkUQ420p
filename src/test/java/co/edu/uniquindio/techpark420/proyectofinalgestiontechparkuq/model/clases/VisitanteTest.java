package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    // Prueba 10 — Recarga de saldo virtual aumenta el saldo correctamente
    @Test
    void recargarSaldo_valorPositivo_aumentaSaldo() {
        visitante.recargarSaldo(20000);
        assertEquals(70000, visitante.getSaldoVirtual(), 0.01);
    }

    // Prueba 11 — Pago con saldo insuficiente es rechazado sin descontar
    @Test
    void realizarPago_saldoInsuficiente_retornaFalsoSinDescontar() {
        assertFalse(visitante.realizarPago(60000));
        assertEquals(50000, visitante.getSaldoVirtual(), 0.01);
    }
}