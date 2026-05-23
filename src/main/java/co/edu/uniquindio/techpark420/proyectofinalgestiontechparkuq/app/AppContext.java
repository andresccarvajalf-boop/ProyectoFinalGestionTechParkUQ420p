package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app;

import java.util.UUID;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.ColaVirtual;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Notificacion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Operador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.persistence.PersistenciaManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppContext {

    private static AppContext instancia;

private AppContext() {
    Parque cargado = PersistenciaManager.cargar();
    this.parque = (cargado != null) ? cargado : new Parque("Tech-Park UQ", 500);
}

    public static AppContext getInstance() {
        if (instancia == null) {
            instancia = new AppContext();
        }
        return instancia;
    }

    private Parque parque;
    private Stage stagePrincipal;
    private Empleado empleadoEnSesion;
    private Visitante visitanteEnSesion;
    private Atraccion atraccionSeleccionada;

    private Timeline procesadorColas;
    private Runnable callbackTurno;

    public void iniciarSesionEmpleado(Empleado empleado) {
        this.empleadoEnSesion = empleado;
        this.visitanteEnSesion = null;
        detenerProcesadorColas();
    }

    public void iniciarSesionVisitante(Visitante visitante) {
        this.visitanteEnSesion = visitante;
        this.empleadoEnSesion = null;
        iniciarProcesadorColas();
    }

    public void cerrarSesion() {
        detenerProcesadorColas();
        this.callbackTurno = null;
        this.empleadoEnSesion = null;
        this.visitanteEnSesion = null;
    }

    public boolean haySesionActiva() {
        return empleadoEnSesion != null || visitanteEnSesion != null;
    }

    public void iniciarProcesadorColas() {
        if (procesadorColas != null) procesadorColas.stop();

        procesadorColas = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            Visitante visitante = visitanteEnSesion;
            if (visitante == null) return;

            for (Zona zona : parque.getZonas()) {
                for (Atraccion atraccion : zona.getAtracciones()) {
                    ColaVirtual cola = atraccion.getColaVirtual();
                    if (cola == null || !cola.isActiva()) continue;

                    boolean estabaEnCola =
                            cola.getVisitantesEnCola().contains(visitante) ||
                            cola.getVisitantesFastPass().contains(visitante);

                    if (!estabaEnCola) continue;

                    Visitante atendido = cola.getVisitantesFastPass().isEmpty()
                            ? cola.atenderSiguiente()
                            : cola.atenderSiguienteFastPass();

                    if (atendido != null && atendido.equals(visitante)) {
                        String titulo = "¡Es tu turno!";
                        String mensaje = "Tu turno en " + atraccion.getNombre()
                                + " ha llegado. ¡Dirígete a la atracción!";
                        Notificacion notif = new Notificacion(
                                UUID.randomUUID().toString(), titulo, mensaje, visitante);
                        visitante.recibirNotificacion(notif);

                        if (callbackTurno != null) {
                            Platform.runLater(callbackTurno);
                        }
                    }
                }
            }
        }));

        procesadorColas.setCycleCount(Timeline.INDEFINITE);
        procesadorColas.play();
    }

    public void detenerProcesadorColas() {
        if (procesadorColas != null) {
            procesadorColas.stop();
            procesadorColas = null;
        }
    }

    public void setCallbackTurno(Runnable callback) {
        this.callbackTurno = callback;
    }

    public void clearCallbackTurno() {
        this.callbackTurno = null;
    }

    public void guardarDatos() {
    PersistenciaManager.guardar(parque);
}
    public Parque getParque() { return parque; }
    public void setParque(Parque parque) { this.parque = parque; }

    public Stage getStagePrincipal() { return stagePrincipal; }
    public void setStagePrincipal(Stage stagePrincipal) { this.stagePrincipal = stagePrincipal; }

    public Empleado getEmpleadoEnSesion() { return empleadoEnSesion; }
    public Visitante getVisitanteEnSesion() { return visitanteEnSesion; }

    public Visitante getVisitanteActivo() {
        return visitanteEnSesion;
    }

    public Operador getOperadorActivo() {
        if (empleadoEnSesion instanceof Operador) {
            return (Operador) empleadoEnSesion;
        }
        return null;
    }

    public Administrador getAdminActivo() {
        if (empleadoEnSesion instanceof Administrador) {
            return (Administrador) empleadoEnSesion;
        }
        return null;
    }

    public Administrador getAdministradorActivo() {
        return getAdminActivo();
    }

    public Atraccion getAtraccionSeleccionada() { return atraccionSeleccionada; }
    public void setAtraccionSeleccionada(Atraccion atraccion) { this.atraccionSeleccionada = atraccion; }
}