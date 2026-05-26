/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;

public class Operador extends Empleado implements Serializable {
    private String codigoEmpleado;
    private Zona zonaAsignada;
    private List<Atraccion> atraccionesResponsables;
    private List<RevisionTecnica> revisionesRealizadas;

    public Operador() {
        this.atraccionesResponsables = new ArrayList<>();
        this.revisionesRealizadas = new ArrayList<>();
    }

    public Operador(String codigoEmpleado, Zona zonaAsignada) {
        this.codigoEmpleado = codigoEmpleado;
        this.zonaAsignada = zonaAsignada;

        this.atraccionesResponsables = new ArrayList<>();
        this.revisionesRealizadas = new ArrayList<>();
    }

    public boolean validarIngreso(Visitante visitante, Atraccion atraccion) {
        if (visitante == null || atraccion == null) {
            return false;
        }

        return atraccion.validarAcceso(visitante);
    }

    public void cambiarEstadoAtraccion(Atraccion atraccion, EstadoAtraccion estado) {
        if (verificarZona(atraccion)) {
            atraccion.cambiarEstado(estado);
        }
    }

    public void registrarRevisionTecnica(RevisionTecnica revision) {
        if (revision != null) {
            revisionesRealizadas.add(revision);

            Atraccion atraccion = revision.getAtraccion();

            if (atraccion != null) {
                atraccion.registrarRevision(revision);
            }
        }
    }

    public void agregarAtraccionResponsable(Atraccion atraccion) {
        if (atraccion != null && !atraccionesResponsables.contains(atraccion)) {
            atraccionesResponsables.add(atraccion);
        }
    }

    public void removerAtraccionResponsable(Atraccion atraccion) {
        atraccionesResponsables.remove(atraccion);
    }

    public List<Atraccion> consultarAtraccionesAsignadas() {
        return atraccionesResponsables;
    }

    public boolean verificarZona(Atraccion atraccion) {
        if (zonaAsignada == null || atraccion == null) {
            return false;
        }

        return zonaAsignada.getAtracciones().contains(atraccion);
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public Zona getZonaAsignada() {
        return zonaAsignada;
    }

    public void setZonaAsignada(Zona zonaAsignada) {
        this.zonaAsignada = zonaAsignada;
    }

    public List<Atraccion> getAtraccionesResponsables() {
        return atraccionesResponsables;
    }

    public List<RevisionTecnica> getRevisionesRealizadas() {
        return revisionesRealizadas;
    }
}
