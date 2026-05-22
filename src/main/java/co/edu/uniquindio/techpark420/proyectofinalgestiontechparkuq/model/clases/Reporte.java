package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.time.LocalDateTime;

public class Reporte {

    private String id;
    private String tipoReporte;
    private LocalDateTime fechaGeneracion;
    private String descripcion;
    private String datos;
    private Administrador administradorGenerador;

    public Reporte() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public Reporte(String id, String tipoReporte, String descripcion, String datos, Administrador administradorGenerador) {
        this.id = id;
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.datos = datos;
        this.administradorGenerador = administradorGenerador;
        this.fechaGeneracion = LocalDateTime.now();
    }

    public void generarReporte() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public String exportarReporte() {
        return id + ";" + tipoReporte + ";" + fechaGeneracion + ";" + descripcion + ";" + datos;
    }

    public String mostrarReporte() {
        return "Reporte ID: " + id +
                " | Tipo: " + tipoReporte +
                " | Fecha: " + fechaGeneracion +
                " | Descripción: " + descripcion +
                " | Datos: " + datos;
    }

    public void filtrarDatos(String filtro) {
        if (datos != null && filtro != null) {
            datos = datos.replaceAll("(?i)" + filtro, "");
        }
    }

    public void calcularEstadisticas() {
        // lógica futura de estadísticas
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Administrador getAdministradorGenerador() {
        return administradorGenerador;
    }

    public void setAdministradorGenerador(Administrador administradorGenerador) {
        this.administradorGenerador = administradorGenerador;
    }
}
