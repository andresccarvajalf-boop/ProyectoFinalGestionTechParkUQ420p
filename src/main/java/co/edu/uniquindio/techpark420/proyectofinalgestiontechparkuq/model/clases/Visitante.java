package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces.Notificable;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces.Pagable;

public class Visitante implements Notificable, Pagable, Serializable {

    private String id;
    private String nombre;
    private String documento;
    private int edad;
    private double estatura;
    private double saldoVirtual;
    private String fotoPerfil;
    private Ticket ticketActivo;
    private List<Atraccion> atraccionesFavoritas;
    private List<HistorialVisita> historialVisitas;
    private List<Notificacion> notificaciones;

    public Visitante() {
        this.atraccionesFavoritas = new ArrayList<>();
        this.historialVisitas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public Visitante(String id, String nombre, String documento, int edad, double estatura) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.edad = edad;
        this.estatura = estatura;
        this.saldoVirtual = 0;
        this.atraccionesFavoritas = new ArrayList<>();
        this.historialVisitas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public void comprarTicket(Ticket ticket) {
        this.ticketActivo = ticket;
    }

    public void consultarMapa(MapaParque mapaParque) {
        if (mapaParque != null) {
            mapaParque.mostrarMapa();
        }
    }

    public void recargarSaldo(double valor) {
        if (valor > 0) {
            saldoVirtual += valor;
        }
    }

    public boolean pagarCostoAdicional(double valor) {
        return realizarPago(valor);
    }

    public void agregarFavorita(Atraccion atraccion) {
        if (atraccion != null && !atraccionesFavoritas.contains(atraccion)) {
            atraccionesFavoritas.add(atraccion);
        }
    }

    public void removerFavorita(Atraccion atraccion) {
        atraccionesFavoritas.remove(atraccion);
    }

    @Override
    public void recibirNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            notificaciones.add(notificacion);
        }
    }

    public int consultarTiempoEspera(Atraccion atraccion) {
        if (atraccion != null) {
            return atraccion.getTiempoEspera();
        }
        return 0;
    }

    public boolean ingresarAtraccion(Atraccion atraccion) {
        if (ticketActivo == null || atraccion == null) return false;
        if (!ticketActivo.validarAcceso(atraccion)) return false;

        LocalDate hoy = LocalDate.now();
        HistorialVisita historialHoy = historialVisitas.stream()
                .filter(h -> h.getFechaVisita().equals(hoy))
                .findFirst()
                .orElse(null);

        if (historialHoy == null) {
            historialHoy = new HistorialVisita("H-" + System.currentTimeMillis(), this);
            historialVisitas.add(historialHoy);
        }

        historialHoy.registrarAtraccion(atraccion);
        if (ticketActivo != null) {
            historialHoy.registrarTicket(ticketActivo);
        }
        return true;
    }

    public List<HistorialVisita> consultarHistorial() {
        return historialVisitas;
    }

    @Override
    public void enviarNotificacion(String mensaje) {
        Notificacion nuevaNotificacion = new Notificacion();
        nuevaNotificacion.setMensaje(mensaje);
        notificaciones.add(nuevaNotificacion);
    }

    @Override
    public void marcarNotificacionLeida(Notificacion notificacion) {
        if (notificacion != null) {
            notificacion.marcarComoLeida();
        }
    }

    @Override
    public List<Notificacion> listarNotificaciones() {
        return notificaciones;
    }

    @Override
    public boolean realizarPago(double valor) {
        if (validarPago(valor)) {
            saldoVirtual -= valor;
            return true;
        }
        return false;
    }

    @Override
    public boolean validarPago(double valor) {
        return valor > 0 && saldoVirtual >= valor;
    }

    @Override
    public String generarFactura() {
        return "Factura visitante: " + nombre +
                " | Saldo restante: " + saldoVirtual;
    }

    @Override
    public double consultarSaldo() {
        return saldoVirtual;
    }



    public String getId()                              { return id; }
    public String getNombre()                          { return nombre; }
    public String getDocumento()                       { return documento; }
    public int getEdad()                               { return edad; }
    public double getEstatura()                        { return estatura; }
    public double getSaldoVirtual()                    { return saldoVirtual; }
    public String getFotoPerfil()                      { return fotoPerfil; }
    public Ticket getTicketActivo()                    { return ticketActivo; }
    public List<Atraccion> getAtraccionesFavoritas()   { return atraccionesFavoritas; }
    public List<HistorialVisita> getHistorialVisitas() { return historialVisitas; }
    public List<Notificacion> getNotificaciones()      { return notificaciones; }



    public void setId(String id)                       { this.id = id; }
    public void setNombre(String nombre)               { this.nombre = nombre; }
    public void setDocumento(String documento)         { this.documento = documento; }
    public void setEdad(int edad)                      { this.edad = edad; }
    public void setEstatura(double estatura)           { this.estatura = estatura; }
    public void setSaldoVirtual(double saldoVirtual)   { this.saldoVirtual = saldoVirtual; }

    /** Guarda la ruta relativa a la foto de perfil, ej: "fotos/123456.jpg" */
    public void setFotoPerfil(String fotoPerfil)       { this.fotoPerfil = fotoPerfil; }
}
