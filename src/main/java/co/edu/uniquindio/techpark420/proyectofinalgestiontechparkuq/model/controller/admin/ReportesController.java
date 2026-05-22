package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.AlertaClimatica;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportesController extends BaseController {

    @FXML private TabPane tabPaneReportes;

    @FXML private Label lblIngresosTotal;
    @FXML private Label lblTicketsVendidos;
    @FXML private TableView<Ticket> tablaTickets;
    @FXML private TableColumn<Ticket, String> colTicketId;
    @FXML private TableColumn<Ticket, String> colTicketTipo;
    @FXML private TableColumn<Ticket, String> colTicketPrecio;

    @FXML private TableView<Atraccion> tablaAtracciones;
    @FXML private TableColumn<Atraccion, String> colAtraccionNombre;
    @FXML private TableColumn<Atraccion, String> colAtraccionVisitantes;
    @FXML private TableColumn<Atraccion, String> colAtraccionTiempo;
    @FXML private TableColumn<Atraccion, String> colAtraccionEstado;

    @FXML private TableView<Atraccion> tablaMantenimientos;
    @FXML private TableColumn<Atraccion, String> colMantNombre;
    @FXML private TableColumn<Atraccion, String> colMantEstado;
    @FXML private TableColumn<Atraccion, String> colMantMotivo;
    @FXML private TableColumn<Atraccion, String> colMantRevisiones;

    @FXML private TableView<AlertaClimatica> tablaCierresClima;
    @FXML private TableColumn<AlertaClimatica, String> colClimaTipo;
    @FXML private TableColumn<AlertaClimatica, String> colClimaDescripcion;
    @FXML private TableColumn<AlertaClimatica, String> colClimaFecha;
    @FXML private TableColumn<AlertaClimatica, String> colClimaAtraccionesAfectadas;

    @FXML
    public void initialize() {
        configurarTablaIngresos();
        configurarTablaAtracciones();
        configurarTablaMantenimientos();
        configurarTablaCierresClima();
        cargarDatos();
    }

    private void configurarTablaIngresos() {
        colTicketId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getId() != null ? data.getValue().getId() : "—"));
        colTicketTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        colTicketPrecio.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$%.2f", data.getValue().getPrecio())));
    }

    private void configurarTablaAtracciones() {
        colAtraccionNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));
        colAtraccionVisitantes.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getVisitantesAcumulados())));
        colAtraccionTiempo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTiempoEspera() + " min"));
        colAtraccionEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado().name()));
    }

    private void configurarTablaMantenimientos() {
        colMantNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));
        colMantEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado().name()));
        colMantMotivo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMotivoCierre() != null
                        ? data.getValue().getMotivoCierre() : "—"));
        colMantRevisiones.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getRevisionesTecnicas().size())));
    }

    private void configurarTablaCierresClima() {
        colClimaTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoAlerta()));
        colClimaDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion()));
        colClimaFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaActivacion() != null
                        ? data.getValue().getFechaActivacion().toLocalDate().toString() : "—"));
        colClimaAtraccionesAfectadas.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().obtenerAtraccionesAfectadas().size())));
    }

    private void cargarDatos() {
        Parque parque = AppContext.getInstance().getParque();
        if (parque == null) return;

        cargarIngresos(parque);
        cargarAtracciones(parque);
        cargarMantenimientos(parque);
        cargarCierresClima(parque);
    }

    private void cargarIngresos(Parque parque) {
        List<Ticket> tickets = parque.getTicketsVendidos();
        tablaTickets.setItems(FXCollections.observableArrayList(tickets));
        double total = parque.calcularIngresosDiarios();
        lblIngresosTotal.setText(String.format("$%.2f", total));
        lblTicketsVendidos.setText(String.valueOf(tickets.size()));
    }

    private void cargarAtracciones(Parque parque) {
        List<Atraccion> todas = parque.obtenerAtraccionesDisponibles();
        List<Atraccion> ordenadas = new ArrayList<>(todas);
        ordenadas.sort(Comparator.comparingInt(Atraccion::getVisitantesAcumulados).reversed());
        tablaAtracciones.setItems(FXCollections.observableArrayList(ordenadas));
    }

    private void cargarMantenimientos(Parque parque) {
        List<Atraccion> enMantenimiento = new ArrayList<>();
        for (Atraccion a : parque.obtenerAtraccionesDisponibles()) {
            if (a.getEstado() == EstadoAtraccion.EN_MANTENIMIENTO || !a.getRevisionesTecnicas().isEmpty()) {
                enMantenimiento.add(a);
            }
        }
        tablaMantenimientos.setItems(FXCollections.observableArrayList(enMantenimiento));
    }

    private void cargarCierresClima(Parque parque) {
        tablaCierresClima.setItems(FXCollections.observableArrayList(parque.getAlertasClimaticas()));
    }

    @FXML
    private void onActualizarReportes() {
        cargarDatos();
        mostrarAlerta("Reportes", "Datos actualizados correctamente.");
    }

    @FXML
    private void onVolverDashboard() {
        navegarA("admin/dashboard-admin.fxml");
    }
}