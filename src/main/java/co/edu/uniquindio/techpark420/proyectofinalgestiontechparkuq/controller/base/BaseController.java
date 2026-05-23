package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base;

import java.io.IOException;
import java.util.Optional;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class BaseController {





    protected final AppContext appContext = AppContext.getInstance();
    protected final Parque parque = AppContext.getInstance().getParque();


    protected Parque getParque() {
        return AppContext.getInstance().getParque();
    }

    protected Empleado getEmpleadoEnSesion() {
        return AppContext.getInstance().getEmpleadoEnSesion();
    }

    protected Visitante getVisitanteEnSesion() {
        return AppContext.getInstance().getVisitanteEnSesion();
    }


    protected void navegarA(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            Stage stagePrincipal = AppContext.getInstance().getStagePrincipal();
            Scene escenaActual = stagePrincipal.getScene();

            if (escenaActual == null) {
                stagePrincipal.setScene(new Scene(root));
            } else {
                escenaActual.setRoot(root);
            }

            stagePrincipal.show();

        } catch (IOException e) {
            mostrarError("Error de navegación",
                    "No se pudo cargar la vista:\n" + rutaFxml + "\n\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    protected FXMLLoader abrirVentanaModal(String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            Stage ventanaModal = new Stage();
            ventanaModal.setTitle(titulo);
            ventanaModal.initModality(Modality.APPLICATION_MODAL);
            ventanaModal.initOwner(AppContext.getInstance().getStagePrincipal());
            ventanaModal.setScene(new Scene(root));
            ventanaModal.showAndWait();

            return loader;

        } catch (IOException e) {
            mostrarError("Error al abrir ventana",
                    "No se pudo abrir:\n" + rutaFxml + "\n\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    protected void mostrarInfo(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    protected void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    protected void mostrarError(String mensaje) {
        mostrarError("Error", mensaje);
    }

    protected void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    protected void mostrarAlerta(String titulo, String mensaje) {
        mostrarInfo(titulo, mensaje);
    }

    protected boolean mostrarConfirmacion(String titulo, String pregunta) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(pregunta);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }


    protected static final String VISTA_LOGIN =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/login-view.fxml";
    protected static final String VISTA_DASHBOARD_VISITANTE =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/dashboard-visitante.fxml";
    protected static final String VISTA_COMPRA_TICKET =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/compra-ticket.fxml";
    protected static final String VISTA_DETALLE_ATRACCION =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/detalle-atraccion.fxml";
    protected static final String VISTA_NOTIFICACIONES =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/notificaciones.fxml";
    protected static final String VISTA_DASHBOARD_OPERADOR =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/operador/dashboard-operador.fxml";
    protected static final String VISTA_GESTION_ATRACCION_OPERADOR =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/operador/gestion-atraccion-operador.fxml";
    protected static final String VISTA_DASHBOARD_ADMIN =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml";
    protected static final String VISTA_GESTION_ZONAS_ATRACCIONES =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-zonas-atracciones.fxml";
    protected static final String VISTA_GESTION_EMPLEADOS =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-empleados.fxml";
    protected static final String VISTA_GESTION_VISITANTES =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-visitantes.fxml";
    protected static final String VISTA_ALERTAS_CLIMATICAS =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/alertas-climaticas.fxml";
    protected static final String VISTA_REPORTES =
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/reportes.fxml";
}