package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MapaParque {

    private String id;
    private String nombreMapa;
    private List<Zona> zonas;
    private List<Atraccion> atracciones;
    private LocalDateTime fechaActualizacion;

    public MapaParque() {
        this.zonas = new ArrayList<>();
        this.atracciones = new ArrayList<>();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public MapaParque(String id, String nombreMapa) {
        this.id = id;
        this.nombreMapa = nombreMapa;
        this.zonas = new ArrayList<>();
        this.atracciones = new ArrayList<>();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void mostrarMapa() {
        for (Zona zona : zonas) {
            System.out.println("Zona: " + zona.getNombre());
        }
        for (Atraccion atraccion : atracciones) {
            System.out.println("Atracción: " + atraccion.getNombre());
        }
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
        for (Atraccion atraccion : atracciones) {
            if (atraccion.getNombre().equalsIgnoreCase(nombre)) {
                return atraccion;
            }
        }
        return null;
    }

    public void actualizarMapa() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void agregarZona(Zona zona) {
        zonas.add(zona);
        actualizarMapa();
    }

    public void agregarAtraccion(Atraccion atraccion) {
        atracciones.add(atraccion);
        actualizarMapa();
    }

    public String obtenerRuta(Zona zona, Atraccion atraccion) {
        if (zona == null || atraccion == null) {
            return "Ruta no disponible";
        }
        return "Ruta desde " + zona.getNombre() + " hacia " + atraccion.getNombre();
    }

    public String getId() {
        return id;
    }

    public String getNombreMapa() {
        return nombreMapa;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public List<Atraccion> getAtracciones() {
        return atracciones;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}