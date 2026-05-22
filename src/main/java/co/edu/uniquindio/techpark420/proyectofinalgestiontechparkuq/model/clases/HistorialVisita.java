package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;

public class HistorialVisita {

    private String id;
    private LocalDate fechaVisita;
    private Visitante visitante;
    private List<Atraccion> atraccionesVisitadas;
    private List<Ticket> ticketsUsados;
    private int tiempoTotalParque;
    private double gastoTotal;

    public HistorialVisita() {
        this.fechaVisita = LocalDate.now();
        this.atraccionesVisitadas = new ArrayList<>();
        this.ticketsUsados = new ArrayList<>();
    }

    public HistorialVisita(String id, Visitante visitante) {
        this.id = id;
        this.visitante = visitante;
        this.fechaVisita = LocalDate.now();
        this.atraccionesVisitadas = new ArrayList<>();
        this.ticketsUsados = new ArrayList<>();
    }

    public void registrarAtraccion(Atraccion atraccion) {
        if (atraccion != null) {
            atraccionesVisitadas.add(atraccion);
        }
    }

    public void registrarTicket(Ticket ticket) {
        if (ticket != null) {
            ticketsUsados.add(ticket);
        }
    }

    public double calcularGastoTotal() {
        double total = 0;

        for (Ticket ticket : ticketsUsados) {
            total += ticket.getPrecio();
        }

        this.gastoTotal = total;
        return total;
    }

    public int calcularTiempoTotal() {
        this.tiempoTotalParque = atraccionesVisitadas.size() * 10;
        return tiempoTotalParque;
    }

    public List<Atraccion> obtenerAtraccionesFavoritas() {
        return new ArrayList<>(atraccionesVisitadas);
    }

    public String generarResumenVisita() {
        return "Visita ID: " + id +
                " | Fecha: " + fechaVisita +
                " | Atracciones: " + atraccionesVisitadas.size() +
                " | Tickets: " + ticketsUsados.size() +
                " | Gasto: " + calcularGastoTotal() +
                " | Tiempo: " + calcularTiempoTotal();
    }

    public void limpiarHistorial() {
        atraccionesVisitadas.clear();
        ticketsUsados.clear();
        gastoTotal = 0;
        tiempoTotalParque = 0;
    }

    public String getId() {
        return id;
    }

    public LocalDate getFechaVisita() {
        return fechaVisita;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public List<Atraccion> getAtraccionesVisitadas() {
        return atraccionesVisitadas;
    }

    public List<Ticket> getTicketsUsados() {
        return ticketsUsados;
    }

    public int getTiempoTotalParque() {
        return tiempoTotalParque;
    }

    public double getGastoTotal() {
        return gastoTotal;
    }
}
