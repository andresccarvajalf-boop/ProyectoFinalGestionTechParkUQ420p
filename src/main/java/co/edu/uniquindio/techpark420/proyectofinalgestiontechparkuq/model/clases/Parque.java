package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces.Gestionable;

public class Parque implements Gestionable, Serializable {

    private String nombre;
    private int capacidadMaxima;
    private int visitantesActuales;

    private List<Zona> zonas;
    private List<Empleado> empleados;
    private List<Visitante> visitantes;
    private List<Ticket> ticketsVendidos;
    private List<AlertaClimatica> alertasClimaticas;
    private List<Reporte> reportes;


    public Parque() {
        this.zonas = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.visitantes = new ArrayList<>();
        this.ticketsVendidos = new ArrayList<>();
        this.alertasClimaticas = new ArrayList<>();
        this.reportes = new ArrayList<>();
    }


    public Parque(String nombre, int capacidadMaxima) {
        this();
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.visitantesActuales = 0;
    }





    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getVisitantesActuales() {
        return visitantesActuales;
    }

    public void setVisitantesActuales(int visitantesActuales) {
        this.visitantesActuales = visitantesActuales;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public List<Visitante> getVisitantes() {
        return visitantes;
    }

    public List<Ticket> getTicketsVendidos() {
        return ticketsVendidos;
    }

    public List<AlertaClimatica> getAlertasClimaticas() {
        return alertasClimaticas;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }





    public void agregarZona(Zona zona) {
        if (zona != null) {
            zonas.add(zona);
        }
    }

    public void removerZona(Zona zona) {
        zonas.remove(zona);
    }

    public void registrarEmpleado(Empleado empleado) {
        if (empleado != null) {
            empleados.add(empleado);
        }
    }

    public void registrarVisitante(Visitante visitante) {
        if (visitante != null && verificarCapacidad()) {
            visitantes.add(visitante);
            visitantesActuales++;
        }
    }

    public boolean venderTicket(Ticket ticket) {

        if (ticket == null) {
            return false;
        }

        if (!verificarCapacidad()) {
            return false;
        }

        ticketsVendidos.add(ticket);
        return true;
    }

    public boolean verificarCapacidad() {
        return visitantesActuales < capacidadMaxima;
    }

    public List<Atraccion> obtenerAtraccionesDisponibles() {

        List<Atraccion> atraccionesDisponibles = new ArrayList<>();

        for (Zona zona : zonas) {

            if (zona.getAtracciones() != null) {
                atraccionesDisponibles.addAll(zona.getAtracciones());
            }
        }

        return atraccionesDisponibles;
    }

    public void activarAlerta(AlertaClimatica alerta) {

        if (alerta != null) {
            alertasClimaticas.add(alerta);
        }
    }

    public Reporte generarReporteGeneral() {

        Reporte reporte = new Reporte();
        reportes.add(reporte);

        return reporte;
    }

public double calcularIngresosDiarios() {
    double ingresos = 0;


    for (Ticket ticket : ticketsVendidos) {
        ingresos += ticket.getPrecio();
    }


    for (Visitante visitante : visitantes) {
        for (HistorialVisita historial : visitante.getHistorialVisitas()) {
            for (Atraccion atraccion : historial.getAtraccionesVisitadas()) {
                ingresos += atraccion.getCostoAdicional();
            }
        }
    }

    return ingresos;
}
    public Zona buscarZona(String nombre) {

        for (Zona zona : zonas) {

            if (zona.getNombre().equalsIgnoreCase(nombre)) {
                return zona;
            }
        }

        return null;
    }

    public Atraccion buscarAtraccion(String nombre) {

        for (Zona zona : zonas) {

            Atraccion encontrada = zona.buscarAtraccion(nombre);

            if (encontrada != null) {
                return encontrada;
            }
        }

        return null;
    }





    @Override
    public void crear() {
        System.out.println("Parque creado correctamente");
    }

    @Override
    public void actualizar() {
        System.out.println("Parque actualizado correctamente");
    }

    @Override
    public void eliminar() {
        System.out.println("Parque eliminado correctamente");
    }

    @Override
    public void consultar() {
        System.out.println(this.toString());
    }

    @Override
    public List<String> listarElementos() {

        List<String> elementos = new ArrayList<>();

        for (Zona zona : zonas) {
            elementos.add(zona.getNombre());
        }

        return elementos;
    }





    @Override
    public String toString() {
        return "Parque{" +
                "nombre='" + nombre + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", visitantesActuales=" + visitantesActuales +
                ", zonas=" + zonas.size() +
                ", empleados=" + empleados.size() +
                ", visitantes=" + visitantes.size() +
                '}';
    }
}