package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ColaVirtual implements Serializable {
    private String id;
    private int capacidadMaxima;
    private List<Visitante> visitantesEnCola;
    private List<Visitante> visitantesFastPass;
    private int tiempoPromedioEspera;
    private Atraccion atraccion;
    private boolean activa;

    public ColaVirtual() {
        this.visitantesEnCola = new ArrayList<>();
        this.visitantesFastPass = new ArrayList<>();
        this.activa = true;
    }

    public ColaVirtual(String id, int capacidadMaxima, Atraccion atraccion) {
        this.id = id;
        this.capacidadMaxima = capacidadMaxima;
        this.atraccion = atraccion;
        this.visitantesEnCola = new ArrayList<>();
        this.visitantesFastPass = new ArrayList<>();
        this.activa = true;
    }

    public boolean agregarVisitante(Visitante visitante) {
        if (!activa || estaLlena()) return false;
        visitantesEnCola.add(visitante);
        return true;
    }

    public boolean agregarFastPass(Visitante visitante) {
        if (!activa || estaLlena()) return false;
        visitantesFastPass.add(visitante);
        return true;
    }

    public void removerVisitante(Visitante visitante) {
        visitantesEnCola.remove(visitante);
        visitantesFastPass.remove(visitante);
    }

    public Visitante obtenerSiguienteVisitante() {
        if (!visitantesFastPass.isEmpty()) {
            return visitantesFastPass.remove(0);
        }
        if (!visitantesEnCola.isEmpty()) {
            return visitantesEnCola.remove(0);
        }
        return null;
    }

    public int calcularTiempoEspera() {
        int total = obtenerCantidadPersonas();
        tiempoPromedioEspera = total * 5;
        return tiempoPromedioEspera;
    }

    public boolean estaLlena() {
        return obtenerCantidadPersonas() >= capacidadMaxima;
    }

    public void vaciarCola() {
        visitantesEnCola.clear();
        visitantesFastPass.clear();
    }

    public void cerrarCola() {
        activa = false;
    }

    public void abrirCola() {
        activa = true;
    }

    public int obtenerCantidadPersonas() {
        return visitantesEnCola.size() + visitantesFastPass.size();
    }

    public boolean verificarPrioridad(Visitante visitante) {
        return visitantesFastPass.contains(visitante);
    }

    public String getId() {
        return id;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public Atraccion getAtraccion() {
        return atraccion;
    }

    public boolean isActiva() {
        return activa;
    }
    public List<Visitante> getVisitantesEnCola() {
    return visitantesEnCola;
}

    public List<Visitante> getVisitantesFastPass() {
    return visitantesFastPass;
}



/**
 * Atiende al siguiente visitante de la cola normal (sin FastPass).
 * Retorna el visitante atendido, o null si la cola está vacía.
 */
public Visitante atenderSiguiente() {
    if (visitantesEnCola.isEmpty()) return null;
    return visitantesEnCola.remove(0);
}

/**
 * Atiende al siguiente visitante de la cola FastPass.
 * Retorna el visitante atendido, o null si la cola FastPass está vacía.
 */
public Visitante atenderSiguienteFastPass() {
    if (visitantesFastPass.isEmpty()) return null;
    return visitantesFastPass.remove(0);
}
}
