/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;

import java.io.Serializable;

public class TicketFastPass extends Ticket implements Serializable {

    private boolean prioridadCola;
    private List<Atraccion> atraccionesHabilitadas;
    private int tiempoMaximoEspera;

    public TicketFastPass() {
        this.prioridadCola = true;
        this.atraccionesHabilitadas = new ArrayList<>();
        this.tiempoMaximoEspera = 0;
        setPrecio(25000);
    }

    public TicketFastPass(int tiempoMaximoEspera) {
        this.prioridadCola = true;
        this.atraccionesHabilitadas = new ArrayList<>();
        this.tiempoMaximoEspera = tiempoMaximoEspera;
        setPrecio(25000);
    }

    @Override
    public boolean validarAcceso(Atraccion atraccion) {
        return validarFastPass(atraccion);
    }

    public boolean accederFilaPrioritaria(Atraccion atraccion) {
        return validarFastPass(atraccion);
    }

    public boolean validarFastPass(Atraccion atraccion) {
        if (atraccion == null) return false;
        return atraccionesHabilitadas.contains(atraccion) && atraccion.getEstado() != null;
    }

    @Override
    public double calcularPrecio() {
        return 25000;
    }

    public String mostrarBeneficios() {
        return "FastPass: acceso prioritario en atracciones habilitadas y reducción de tiempo de espera.";
    }

    public void agregarAtraccion(Atraccion atraccion) {
        if (atraccion != null && !atraccionesHabilitadas.contains(atraccion)) {
            atraccionesHabilitadas.add(atraccion);
        }
    }

    public void removerAtraccion(Atraccion atraccion) {
        atraccionesHabilitadas.remove(atraccion);
    }

    public boolean isPrioridadCola() { return prioridadCola; }
    public List<Atraccion> getAtraccionesHabilitadas() { return atraccionesHabilitadas; }
    public int getTiempoMaximoEspera() { return tiempoMaximoEspera; }
}