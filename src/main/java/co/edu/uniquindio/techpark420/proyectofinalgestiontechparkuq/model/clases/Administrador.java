package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;

public class Administrador extends Empleado {

    private String cargo;
    private int usuariosGestionados;
    private List<Reporte> reportesGenerados;
    private List<AlertaClimatica> alertasActivadas;

    public Administrador() {
        this.reportesGenerados = new ArrayList<>();
        this.alertasActivadas = new ArrayList<>();
    }

    public Administrador(String cargo, int usuariosGestionados) {
        this.cargo = cargo;
        this.usuariosGestionados = usuariosGestionados;
        this.reportesGenerados = new ArrayList<>();
        this.alertasActivadas = new ArrayList<>();
    }

    public void crearZona(Zona zona) {
        // lógica de creación de zona
    }

    public void modificarZona(Zona zona) {
        // lógica de modificación de zona
    }

    public void eliminarZona(Zona zona) {
        // lógica de eliminación de zona
    }

    public void crearAtraccion(Atraccion atraccion) {
        // lógica de creación de atracción
    }

    public void modificarAtraccion(Atraccion atraccion) {
        // lógica de modificación de atracción
    }

    public void eliminarAtraccion(Atraccion atraccion) {
        // lógica de eliminación de atracción
    }

    public void asignarOperador(Operador operador, Zona zona) {
        // asignar operador a zona
    }

    public void activarAlerta(AlertaClimatica alerta) {
        alertasActivadas.add(alerta);
    }

    public Reporte generarReporteIngresos() {
        return new Reporte();
    }

    public Reporte generarReporteAtracciones() {
        return new Reporte();
    }

    public List<Reporte> consultarReportes() {
        return reportesGenerados;
    }

    public void contratarEmpleado(Empleado empleado) {
        usuariosGestionados++;
    }

    public void desvincularEmpleado(Empleado empleado) {
        usuariosGestionados--;
    }

    // Getters y setters básicos
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getUsuariosGestionados() {
        return usuariosGestionados;
    }
}