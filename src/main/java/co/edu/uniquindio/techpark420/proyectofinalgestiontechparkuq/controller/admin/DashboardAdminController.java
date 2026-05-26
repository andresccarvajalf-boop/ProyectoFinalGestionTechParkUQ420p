/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DashboardAdminController extends BaseController implements Initializable {

    @FXML private Label nombreAdminLabel;
    @FXML private Label visitantesActualesLabel;
    @FXML private Label capacidadTotalLabel;
    @FXML private Label atraccionesActivasLabel;
    @FXML private Label alertasActivasLabel;
    @FXML private Label totalZonasLabel;
    @FXML private Label totalEmpleadosLabel;
    @FXML private Label totalVisitantesLabel;

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
            nombreAdminLabel.setText("Bienvenido, " + administrador.getNombre());
        }

        if (parque == null) return;

        visitantesActualesLabel.setText(String.valueOf(parque.getVisitantesActuales()));
        capacidadTotalLabel.setText(String.valueOf(parque.getCapacidadMaxima()));
        totalZonasLabel.setText(String.valueOf(parque.getZonas().size()));
        totalVisitantesLabel.setText(String.valueOf(parque.getVisitantes().size()));

        long activas = parque.getZonas().stream()
                .flatMap(z -> z.getAtracciones().stream())
                .filter(a -> a.getEstado() == EstadoAtraccion.ACTIVA)
                .count();
        atraccionesActivasLabel.setText(String.valueOf(activas));

        long alertas = parque.getAlertasClimaticas().stream()
                .filter(a -> a.isActiva())
                .count();
        alertasActivasLabel.setText(String.valueOf(alertas));

        long empleados = parque.getEmpleados().stream()
                .filter(e -> e.isActivo())
                .count();
        totalEmpleadosLabel.setText(String.valueOf(empleados));
    }

    @FXML
    private void irAGestionZonas() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-zonas-atracciones.fxml");
    }


    @FXML
    private void irAGestionEmpleados() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-empleados.fxml");
    }

    @FXML
    private void irAGestionVisitantes() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/gestion-visitantes.fxml");
    }


    @FXML
    private void irAAlertasClimaticas() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/alertas-climaticas.fxml");
    }


    @FXML
    private void irAReportes() {
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