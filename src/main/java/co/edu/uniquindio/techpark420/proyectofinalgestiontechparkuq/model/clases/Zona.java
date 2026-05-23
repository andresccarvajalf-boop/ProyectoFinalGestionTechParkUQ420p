package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;

public class Zona implements Serializable {

    private String id;
    private String nombre;
    private int capacidadMaxima;
    private List<Atraccion> atracciones;
    private List<Operador> operadoresAsignados;
    private int visitantesActuales;
    private boolean activa;

    public Zona() {
        this.atracciones = new ArrayList<>();
        this.operadoresAsignados = new ArrayList<>();
        this.activa = true;
    }

    public Zona(String id, String nombre, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.visitantesActuales = 0;
        this.activa = true;

        this.atracciones = new ArrayList<>();
        this.operadoresAsignados = new ArrayList<>();
    }

    public void agregarAtraccion(Atraccion atraccion) {
        if (atraccion != null && !atracciones.contains(atraccion)) {
            atracciones.add(atraccion);
        }
    }

    public void removerAtraccion(Atraccion atraccion) {
        atracciones.remove(atraccion);
    }

    public void agregarOperador(Operador operador) {
        if (operador != null && !operadoresAsignados.contains(operador)) {
            operadoresAsignados.add(operador);
        }
    }

    public void removerOperador(Operador operador) {
        operadoresAsignados.remove(operador);
    }

    public int calcularCapacidadDisponible() {
        return capacidadMaxima - visitantesActuales;
    }

    public boolean registrarIngresoVisitante() {
        if (verificarAforo() && activa) {
            visitantesActuales++;
            return true;
        }
        return false;
    }

    public void registrarSalidaVisitante() {
        if (visitantesActuales > 0) {
            visitantesActuales--;
        }
    }

    public List<Atraccion> obtenerAtraccionesActivas() {
        List<Atraccion> atraccionesActivas = new ArrayList<>();

        for (Atraccion atraccion : atracciones) {
            if (atraccion.getEstado() == EstadoAtraccion.ACTIVA) {
                atraccionesActivas.add(atraccion);
            }
        }

        return atraccionesActivas;
    }

    public boolean verificarAforo() {
        return visitantesActuales < capacidadMaxima;
    }

    public void activarZona() {
        activa = true;
    }

    public void desactivarZona() {
        activa = false;
    }

    public Atraccion buscarAtraccion(String nombreAtraccion) {
        for (Atraccion atraccion : atracciones) {
            if (atraccion.getNombre().equalsIgnoreCase(nombreAtraccion)) {
                return atraccion;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public List<Atraccion> getAtracciones() {
        return atracciones;
    }

    public List<Operador> getOperadores() {
        return operadoresAsignados;
    }

    public int getVisitantesActuales() {
        return visitantesActuales;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
}
