package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaClimatica {

    private String id;
    private String tipoAlerta;
    private String descripcion;
    private LocalDateTime fechaActivacion;
    private boolean activa;
    private Administrador administradorResponsable;
    private List<Atraccion> atraccionesAfectadas;

    public AlertaClimatica() {
        this.atraccionesAfectadas = new ArrayList<>();
        this.activa = false;
    }

    public AlertaClimatica(String id, String tipoAlerta, String descripcion, Administrador administradorResponsable) {
        this.id = id;
        this.tipoAlerta = tipoAlerta;
        this.descripcion = descripcion;
        this.administradorResponsable = administradorResponsable;
        this.fechaActivacion = LocalDateTime.now();
        this.atraccionesAfectadas = new ArrayList<>();
        this.activa = false;
    }

    public void activarAlerta() {
        this.activa = true;
        this.fechaActivacion = LocalDateTime.now();
        cerrarAtraccionesRiesgo();
    }

    public void desactivarAlerta() {
        this.activa = false;
    }

    public void agregarAtraccion(Atraccion atraccion) {
        if (!atraccionesAfectadas.contains(atraccion)) {
            atraccionesAfectadas.add(atraccion);
        }
    }

    public void removerAtraccion(Atraccion atraccion) {
        atraccionesAfectadas.remove(atraccion);
    }

    public void notificarVisitantes() {
        for (Atraccion atraccion : atraccionesAfectadas) {
            atraccion.notificarCierre("Alerta climática activa: " + tipoAlerta);
        }
    }

    public void cerrarAtraccionesRiesgo() {
        for (Atraccion atraccion : atraccionesAfectadas) {
            atraccion.cerrarPorClima(this);
        }
    }

    public List<Atraccion> obtenerAtraccionesAfectadas() {
        return atraccionesAfectadas;
    }

    public String getId() {
        return id;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaActivacion() {
        return fechaActivacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public Administrador getAdministradorResponsable() {
        return administradorResponsable;
    }
}
