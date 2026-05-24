package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;



public class TicketGeneral extends Ticket implements Serializable {

    private boolean accesoBasico;
    private double saldoDisponible;

    public TicketGeneral() {
        this.accesoBasico = true;
        this.saldoDisponible = 0;

    }

    public TicketGeneral(double saldoDisponible) {
        this.accesoBasico = true;
        this.saldoDisponible = saldoDisponible;

    }

    @Override
    public boolean validarAcceso(Atraccion atraccion) {
        if (atraccion == null || !accesoBasico) return false;
        if (atraccion.getCostoAdicional() > 0) {
            return saldoDisponible >= atraccion.getCostoAdicional();
        }
        return true;
    }

    public boolean pagarCostoAdicional(double valor) {
        if (valor <= 0 || saldoDisponible < valor) return false;
        saldoDisponible -= valor;
        return true;
    }

    @Override
    public double calcularPrecio() {
        return 0;
    }

    public void recargarSaldo(double valor) {
        if (valor > 0) saldoDisponible += valor;
    }

    public double consultarSaldo() {
        return saldoDisponible;
    }

    public boolean isAccesoBasico() {
        return accesoBasico;
    }

    public void setAccesoBasico(boolean accesoBasico) {
        this.accesoBasico = accesoBasico;
    }
}