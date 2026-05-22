package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardAdminController extends BaseController implements Initializable {

    @FXML private Label lblNombreAdmin;
    @FXML private Label lblVisitantesActuales;
    @FXML private Label lblTotalZonas;
    @FXML private Label lblAtraccionesActivas;
    @FXML private Label lblAlertasActivas;
    @FXML private Label lblEmpleadosActivos;

    private Administrador administrador;
    private Parque parque;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        administrador = AppContext.getInstance().getAdministradorActivo();
        parque = AppContext.getInstance().getParque();
        cargarResumen();
    }

    private void cargarResumen() {
        if (administrador != null) {
            lblNombreAdmin.setText("Bienvenido, " + administrador.getNombre());
        }

        if (parque == null) return;

        lblVisitantesActuales.setText(String.valueOf(parque.getVisitantesActuales()));

        lblTotalZonas.setText(String.valueOf(parque.getZonas().size()));

        long activas = parque.getZonas().stream()
                .flatMap(z -> z.getAtracciones().stream())
                .filter(a -> a.getEstado() == EstadoAtraccion.ACTIVA)
                .count();
        lblAtraccionesActivas.setText(String.valueOf(activas));

        long alertas = parque.getAlertasClimaticas().stream()
                .filter(a -> a.isActiva())
                .count();
        lblAlertasActivas.setText(String.valueOf(alertas));

        long empleados = parque.getEmpleados().stream()
                .filter(e -> e.isActivo())
                .count();
        lblEmpleadosActivos.setText(String.valueOf(empleados));
    }

    @FXML
    private void onGestionZonasAtracciones() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-zonas-atracciones.fxml");
    }

    @FXML
    private void onGestionEmpleados() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-empleados.fxml");
    }

    @FXML
    private void onGestionVisitantes() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-visitantes.fxml");
    }

    @FXML
    private void onAlertasClimaticas() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/alertas-climaticas.fxml");
    }

    @FXML
    private void onReportes() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/reportes.fxml");
    }

    @FXML
    private void onRefrescar() {
        cargarResumen();
    }

    @FXML
    private void onCerrarSesion() {
        AppContext.getInstance().cerrarSesion();
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/login-view.fxml");
    }
}