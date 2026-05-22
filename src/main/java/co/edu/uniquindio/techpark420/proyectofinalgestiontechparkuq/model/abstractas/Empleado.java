package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas;

import java.time.LocalDate;

public abstract class Empleado {

    private String id;
    private String nombre;
    private String documento;
    private double salario;
    private LocalDate fechaIngreso;
    private boolean activo;

    public Empleado() {
        this.fechaIngreso = LocalDate.now();
        this.activo = true;
    }

    public Empleado(String id, String nombre, String documento, double salario) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.salario = salario;
        this.fechaIngreso = LocalDate.now();
        this.activo = true;
    }

    public boolean iniciarSesion() {
        return activo;
    }

    public void cerrarSesion() {
        this.activo = false;
    }

    public void actualizarDatos() {
        // actualización de datos del empleado
    }

    public String mostrarInformacion() {
        return "Empleado ID: " + id +
                " | Nombre: " + nombre +
                " | Documento: " + documento +
                " | Salario: " + salario +
                " | Fecha ingreso: " + fechaIngreso +
                " | Activo: " + activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}