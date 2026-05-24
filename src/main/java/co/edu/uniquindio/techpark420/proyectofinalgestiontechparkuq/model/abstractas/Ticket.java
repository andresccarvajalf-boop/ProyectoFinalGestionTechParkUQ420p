package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas;

import java.io.Serializable;
import java.time.LocalDate;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoTicket;
public abstract class Ticket implements Serializable {

    private String id;
    private LocalDate fechaCompra;
    private double precio;
    private TipoTicket tipoTicket;
    private boolean activo;
    private Visitante visitante;

    public Ticket() {
        this.fechaCompra = LocalDate.now();
        this.activo = true;
    }

    public Ticket(String id, TipoTicket tipoTicket, Visitante visitante) {
        this.id = id;
        this.tipoTicket = tipoTicket;
        this.visitante = visitante;
        this.fechaCompra = LocalDate.now();
        this.activo = true;
    }

    public abstract boolean validarAcceso(Atraccion atraccion);

    public abstract double calcularPrecio();

    public void activarTicket() {
        this.activo = true;
    }

    public void desactivarTicket() {
        this.activo = false;
    }

    public String mostrarInformacion() {
        return "Ticket ID: " + id +
                " | Fecha compra: " + fechaCompra +
                " | Precio: " + precio +
                " | Tipo: " + tipoTicket +
                " | Activo: " + activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public TipoTicket getTipoTicket() {
        return tipoTicket;
    }

    public void setTipoTicket(TipoTicket tipoTicket) {
        this.tipoTicket = tipoTicket;
    }

    public boolean isActivo() {
        return activo;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }
}
