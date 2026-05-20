import java.util.ArrayList;
import java.util.List;

public class Visitante {

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

    public Visitante(String id, String nombre, String documento, int edad,
                      double estatura, double saldoVirtual, String fotoPerfil) {

        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.edad = edad;
        this.estatura = estatura;
        this.saldoVirtual = saldoVirtual;
        this.fotoPerfil = fotoPerfil;

        this.atraccionesFavoritas = new ArrayList<>();
        this.historialVisitas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public void comprarTicket(Ticket ticketComprado) {
        this.ticketActivo = ticketComprado;
    }

    public void consultarMapa(MapaParque mapaParque) {
        System.out.println("Consultando mapa del parque...");
    }

    public void recargarSaldo(double valorRecarga) {
        if (valorRecarga > 0) {
            saldoVirtual += valorRecarga;
        }
    }

    public boolean pagarCostoAdicional(double valorPago) {

        if (saldoVirtual >= valorPago) {
            saldoVirtual -= valorPago;
            return true;
        }

        return false;
    }

    public void agregarFavorita(Atraccion atraccionFavorita) {

        if (!atraccionesFavoritas.contains(atraccionFavorita)) {
            atraccionesFavoritas.add(atraccionFavorita);
        }
    }

    public void removerFavorita(Atraccion atraccionFavorita) {
        atraccionesFavoritas.remove(atraccionFavorita);
    }

    public void recibirNotificacion(Notificacion nuevaNotificacion) {
        notificaciones.add(nuevaNotificacion);
    }

    public int consultarTiempoEspera(Atraccion atraccionConsultada) {
        return atraccionConsultada.getTiempoEspera();
    }

    public boolean ingresarAtraccion(Atraccion atraccionIngreso) {

        if (edad < atraccionIngreso.getEdadMinima()) {
            return false;
        }

        if (estatura < atraccionIngreso.getAlturaMinima()) {
            return false;
        }

        if (!atraccionIngreso.getEstado().equalsIgnoreCase("Activa")) {
            return false;
        }

        double costoAdicional = atraccionIngreso.getCostoAdicional();

        if (costoAdicional > 0) {
            return pagarCostoAdicional(costoAdicional);
        }

        return true;
    }

    public List<HistorialVisita> consultarHistorial() {
        return historialVisitas;
    }

    public String getId() {
        return id;
    }

    public void setId(String idVisitante) {
        this.id = idVisitante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreVisitante) {
        this.nombre = nombreVisitante;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String docVisitante) {
        this.documento = docVisitante;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edadVisitante) {
        this.edad = edadVisitante;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estaturaVisitante) {
        this.estatura = estaturaVisitante;
    }

    public double getSaldoVirtual() {
        return saldoVirtual;
    }

    public void setSaldoVirtual(double saldoActual) {
        this.saldoVirtual = saldoActual;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Ticket getTicketActivo() {
        return ticketActivo;
    }

    public void setTicketActivo(Ticket ticketActivo) {
        this.ticketActivo = ticketActivo;
    }

    public List<Atraccion> getAtraccionesFavoritas() {
        return atraccionesFavoritas;
    }

    public void setAtraccionesFavoritas(List<Atraccion> atraccionesFavoritas) {
        this.atraccionesFavoritas = atraccionesFavoritas;
    }

    public List<HistorialVisita> getHistorialVisitas() {
        return historialVisitas;
    }

    public void setHistorialVisitas(List<HistorialVisita> historialVisitas) {
        this.historialVisitas = historialVisitas;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}