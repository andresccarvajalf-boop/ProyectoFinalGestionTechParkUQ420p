/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notificacion implements Serializable {
    private String id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean leida;
    private Visitante destinatario;

    public Notificacion() {
        this.fechaEnvio = LocalDateTime.now();
        this.leida = false;
    }

    public Notificacion(String id, String titulo, String mensaje, Visitante destinatario) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.destinatario = destinatario;
        this.fechaEnvio = LocalDateTime.now();
        this.leida = false;
    }

    public void enviar() {
        if (destinatario != null) {
            destinatario.recibirNotificacion(this);
        }
    }

    public void marcarComoLeida() {
        this.leida = true;
    }

    public String mostrarNotificacion() {
        return "Título: " + titulo +
                " | Mensaje: " + mensaje +
                " | Fecha: " + fechaEnvio +
                " | Leída: " + leida;
    }

    public void eliminarNotificacion() {
        destinatario = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public boolean isLeida() {
        return leida;
    }

    public Visitante getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Visitante destinatario) {
        this.destinatario = destinatario;
    }
}
