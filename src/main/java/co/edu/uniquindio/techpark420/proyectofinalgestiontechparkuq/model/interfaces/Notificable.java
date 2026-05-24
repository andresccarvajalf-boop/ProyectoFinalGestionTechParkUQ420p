package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;

import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Notificacion;

public interface Notificable {

    void enviarNotificacion(String mensaje);

    void recibirNotificacion(Notificacion notificacion);

    void marcarNotificacionLeida(Notificacion notificacion);

    List<Notificacion> listarNotificaciones();
}
