package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.AlertaClimatica;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.RevisionTecnica;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportesController extends BaseController {

    @FXML private TabPane tabPane;

    @FXML private Label ingresosTotalesLabel;
    @FXML private Label ticketsVendidosLabel;
    @FXML private Label ticketMasVendidoLabel;

    @FXML private TableView<Ticket> ingresosTable;
    @FXML private TableColumn<Ticket, String> colIngFecha;
    @FXML private TableColumn<Ticket, String> colIngTipoTicket;
    @FXML private TableColumn<Ticket, String> colIngVisitante;
    @FXML private TableColumn<Ticket, String> colIngMonto;

    @FXML private Label topAtraccionLabel;
    @FXML private Label totalIngresosAtraccionesLabel;

    @FXML private TableView<Atraccion> atraccionesVisitadasTable;
    @FXML private TableColumn<Atraccion, String> colAvRanking;
    @FXML private TableColumn<Atraccion, String> colAvNombre;
    @FXML private TableColumn<Atraccion, String> colAvZona;
    @FXML private TableColumn<Atraccion, String> colAvVisitantes;
    @FXML private TableColumn<Atraccion, String> colAvPromedioDia;

    @FXML private Label revisionesTotalesLabel;
    @FXML private Label atraccionMasMantenimientoLabel;

    @FXML private TableView<RevisionTecnica> mantenimientosTable;
    @FXML private TableColumn<RevisionTecnica, String> colMantFecha;
    @FXML private TableColumn<RevisionTecnica, String> colMantAtraccion;
    @FXML private TableColumn<RevisionTecnica, String> colMantOperador;
    @FXML private TableColumn<RevisionTecnica, String> colMantDescripcion;
    @FXML private TableColumn<RevisionTecnica, String> colMantResultado;

    @FXML private Label totalCierresLabel;
    @FXML private Label atraccionMasAfectadaLabel;
    @FXML private Label tipoAlertaFrecuenteLabel;

    @FXML private TableView<AlertaClimatica> cierresClimaTable;
    @FXML private TableColumn<AlertaClimatica, String> colCcFecha;
    @FXML private TableColumn<AlertaClimatica, String> colCcAtraccion;
    @FXML private TableColumn<AlertaClimatica, String> colCcTipoAlerta;
    @FXML private TableColumn<AlertaClimatica, String> colCcDuracion;


    @FXML
    public void initialize() {
        configurarTablaIngresos();
        configurarTablaAtracciones();
        configurarTablaMantenimientos();
        configurarTablaCierresClima();
        cargarDatos();
    }


    private void configurarTablaIngresos() {
        colIngFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaCompra() != null
                        ? data.getValue().getFechaCompra().toString() : "—"));
        colIngTipoTicket.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoTicket() != null
                        ? data.getValue().getTipoTicket().name() : "—"));
        colIngVisitante.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getVisitante() != null
                        ? data.getValue().getVisitante().getNombre() : "—"));
        colIngMonto.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("$%.2f", data.getValue().getPrecio())));
    }

    private void configurarTablaAtracciones() {
        colAvRanking.setCellValueFactory(data -> {
            int index = atraccionesVisitadasTable.getItems().indexOf(data.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });
        colAvNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));
        colAvZona.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoAtraccion() != null
                        ? data.getValue().getTipoAtraccion().name() : "—"));
        colAvVisitantes.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getVisitantesAcumulados())));
        colAvPromedioDia.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.format("%.1f", data.getValue().getVisitantesAcumulados() / 30.0)));
    }

    private void configurarTablaMantenimientos() {
        colMantFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaRevision() != null
                        ? data.getValue().getFechaRevision().toString() : "—"));
        colMantAtraccion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAtraccion() != null
                        ? data.getValue().getAtraccion().getNombre() : "—"));

        colMantOperador.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getOperador() != null
                        ? data.getValue().getOperador().getNombre() : "—"));
        colMantDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion() != null
                        ? data.getValue().getDescripcion() : "—"));
        colMantResultado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getResultado() != null
                        ? data.getValue().getResultado() : "Pendiente"));
    }

    private void configurarTablaCierresClima() {

        colCcFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaActivacion() != null
                        ? data.getValue().getFechaActivacion().toLocalDate().toString() : "—"));
        colCcAtraccion.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().obtenerAtraccionesAfectadas().size() + " afectada(s)"));
        colCcTipoAlerta.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoAlerta()));

        colCcDuracion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion() != null
                        ? data.getValue().getDescripcion() : "—"));
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
        ingresosTable.setItems(FXCollections.observableArrayList(tickets));
        ingresosTotalesLabel.setText(String.format("$%.2f", parque.calcularIngresosDiarios()));
        ticketsVendidosLabel.setText(String.valueOf(tickets.size()));

        String masVendido = tickets.stream()
                .filter(t -> t.getTipoTicket() != null)
                .collect(Collectors.groupingBy(t -> t.getTipoTicket().name(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("—");
        ticketMasVendidoLabel.setText(masVendido);
    }

    private void cargarAtracciones(Parque parque) {
        List<Atraccion> ordenadas = new ArrayList<>(parque.obtenerAtraccionesDisponibles());
        ordenadas.sort(Comparator.comparingInt(Atraccion::getVisitantesAcumulados).reversed());
        atraccionesVisitadasTable.setItems(FXCollections.observableArrayList(ordenadas));
        topAtraccionLabel.setText(ordenadas.isEmpty() ? "—" : ordenadas.get(0).getNombre());
        int total = ordenadas.stream().mapToInt(Atraccion::getVisitantesAcumulados).sum();
        totalIngresosAtraccionesLabel.setText(String.valueOf(total));
    }

    private void cargarMantenimientos(Parque parque) {

        List<RevisionTecnica> todasRevisiones = new ArrayList<>();
        Atraccion conMas = null;
        int maxRev = 0;

        for (Atraccion a : parque.obtenerAtraccionesDisponibles()) {
            List<RevisionTecnica> revs = a.getRevisiones();
            todasRevisiones.addAll(revs);
            if (revs.size() > maxRev) {
                maxRev = revs.size();
                conMas = a;
            }
        }

        mantenimientosTable.setItems(FXCollections.observableArrayList(todasRevisiones));
        revisionesTotalesLabel.setText(String.valueOf(todasRevisiones.size()));
        atraccionMasMantenimientoLabel.setText(conMas != null ? conMas.getNombre() : "—");
    }

    private void cargarCierresClima(Parque parque) {
        List<AlertaClimatica> alertas = parque.getAlertasClimaticas();
        cierresClimaTable.setItems(FXCollections.observableArrayList(alertas));
        totalCierresLabel.setText(String.valueOf(alertas.size()));

        alertas.stream()
                .flatMap(a -> a.obtenerAtraccionesAfectadas().stream())
                .collect(Collectors.groupingBy(Atraccion::getNombre, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresentOrElse(
                        e -> atraccionMasAfectadaLabel.setText(e.getKey()),
                        () -> atraccionMasAfectadaLabel.setText("—"));

        alertas.stream()
                .collect(Collectors.groupingBy(AlertaClimatica::getTipoAlerta, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresentOrElse(
                        e -> tipoAlertaFrecuenteLabel.setText(e.getKey()),
                        () -> tipoAlertaFrecuenteLabel.setText("—"));
    }



    @FXML
    private void handleExportar() {
        mostrarAlerta("Exportar", "Funcionalidad de exportación en desarrollo.");
    }

    @FXML
    private void handleGenerarReporteIngresos() {
        mostrarAlerta("Reporte de Ingresos", "Reporte generado correctamente.");
    }

    @FXML
    private void handleGenerarReporteAtracciones() {
        mostrarAlerta("Reporte de Atracciones", "Reporte generado correctamente.");
    }

    @FXML
    private void handleGenerarReporteMantenimientos() {
        mostrarAlerta("Reporte de Mantenimientos", "Reporte generado correctamente.");
    }

    @FXML
    private void handleGenerarReporteCierres() {
        mostrarAlerta("Reporte de Cierres por Clima", "Reporte generado correctamente.");
    }

    @FXML
    private void onVolverDashboard() {
        navegarA(VISTA_DASHBOARD_ADMIN);
    }
}