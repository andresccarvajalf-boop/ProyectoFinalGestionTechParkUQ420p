package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;



public class Administrador extends Empleado implements Serializable {
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

    }

    public void modificarZona(Zona zona) {

    }

    public void eliminarZona(Zona zona) {

    }

    public void crearAtraccion(Atraccion atraccion) {

    }

    public void modificarAtraccion(Atraccion atraccion) {

    }

    public void eliminarAtraccion(Atraccion atraccion) {

    }

    public void asignarOperador(Operador operador, Zona zona) {

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