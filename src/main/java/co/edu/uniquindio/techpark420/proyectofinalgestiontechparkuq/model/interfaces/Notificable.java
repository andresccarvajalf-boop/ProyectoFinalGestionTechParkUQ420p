/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;

import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Notificacion;

public interface Notificable {

    void enviarNotificacion(String mensaje);

    void recibirNotificacion(Notificacion notificacion);

    void marcarNotificacionLeida(Notificacion notificacion);

    List<Notificacion> listarNotificaciones();
}
