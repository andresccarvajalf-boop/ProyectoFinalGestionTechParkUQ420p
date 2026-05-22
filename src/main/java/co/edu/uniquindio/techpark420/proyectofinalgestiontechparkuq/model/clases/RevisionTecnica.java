package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.time.LocalDate;

public class RevisionTecnica {

    private String id;
    private LocalDate fecha;
    private String descripcion;
    private String resultado;
    private boolean aprobada;
    private String observaciones;
    private Operador operadorResponsable;
    private Atraccion atraccion;

    public RevisionTecnica() {
        this.fecha = LocalDate.now();
        this.aprobada = false;
    }

    public RevisionTecnica(String id, String descripcion, Operador operadorResponsable, Atraccion atraccion) {
        this.id = id;
        this.descripcion = descripcion;
        this.operadorResponsable = operadorResponsable;
        this.atraccion = atraccion;
        this.fecha = LocalDate.now();
        this.aprobada = false;
    }

    public void realizarRevision() {
        this.resultado = "Revisión realizada";
    }

    public void aprobarRevision() {
        this.aprobada = true;
        this.resultado = "Aprobada";
    }

    public void rechazarRevision(String motivo) {
        this.aprobada = false;
        this.resultado = "Rechazada: " + motivo;
    }

    public Reporte generarReporte() {
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Revisión Técnica");
        reporte.setDescripcion(descripcion);
        reporte.setDatos(mostrarResumen());
        return reporte;
    }

    public void actualizarObservaciones(String obs) {
        this.observaciones = obs;
    }

    public boolean esValida() {
        return aprobada;
    }

    private String mostrarResumen() {
        return "ID: " + id +
                " | Resultado: " + resultado +
                " | Observaciones: " + observaciones +
                " | Fecha: " + fecha;
    }

    public String getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getResultado() {
        return resultado;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Operador getOperadorResponsable() {
        return operadorResponsable;
    }

    public Atraccion getAtraccion() {
        return atraccion;
    }
}
