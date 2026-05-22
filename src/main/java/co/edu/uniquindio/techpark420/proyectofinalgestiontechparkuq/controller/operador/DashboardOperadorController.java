package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.operador;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Operador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardOperadorController extends BaseController implements Initializable {

    // ── Labels del header ──
    @FXML private Label nombreOperadorLabel;
    @FXML private Label zonaAsignadaLabel;

    // ── Tarjetas de resumen ──
    @FXML private Label totalAtraccionesLabel;
    @FXML private Label abieratasLabel;          // nombre exacto del FXML (typo incluido)
    @FXML private Label mantenimientoLabel;
    @FXML private Label visitantesAcumuladosLabel;

    // ── Tabla ──
    @FXML private TableView<Atraccion>          atraccionesTable;
    @FXML private TableColumn<Atraccion, String> colNombre;
    @FXML private TableColumn<Atraccion, String> colEstado;
    @FXML private TableColumn<Atraccion, String> colCola;
    @FXML private TableColumn<Atraccion, String> colTiempoEspera;
    @FXML private TableColumn<Atraccion, String> colVisitantesHoy;

    // ── Botones ──
    @FXML private Button gestionarButton;

    private Operador operadorActivo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operadorActivo = AppContext.getInstance().getOperadorActivo();
        configurarColumnas();
        cargarDatos();
        configurarSeleccion();
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado() != null
                        ? data.getValue().getEstado().name() : "—"));

        colCola.setCellValueFactory(data -> {
            int personas = data.getValue().getColaVirtual() != null
                    ? data.getValue().getColaVirtual().obtenerCantidadPersonas() : 0;
            return new SimpleStringProperty(String.valueOf(personas));
        });

        colTiempoEspera.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTiempoEspera() + " min"));

        colVisitantesHoy.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getVisitantesAcumulados())));
    }

    private void cargarDatos() {
        if (operadorActivo == null) return;

        nombreOperadorLabel.setText(operadorActivo.getNombre());

        Zona zona = operadorActivo.getZonaAsignada();
        if (zona != null) {
            zonaAsignadaLabel.setText(zona.getNombre());
            List<Atraccion> atracciones = zona.getAtracciones();
            atraccionesTable.setItems(FXCollections.observableArrayList(atracciones));

            long totalAtracciones  = atracciones.size();
            long abiertas          = atracciones.stream()
                    .filter(a -> a.getEstado() == EstadoAtraccion.ACTIVA).count();
            long enMantenimiento   = atracciones.stream()
                    .filter(a -> a.getEstado() == EstadoAtraccion.EN_MANTENIMIENTO).count();
            long visitantesTotal   = atracciones.stream()
                    .mapToLong(Atraccion::getVisitantesAcumulados).sum();

            totalAtraccionesLabel.setText(String.valueOf(totalAtracciones));
            abieratasLabel.setText(String.valueOf(abiertas));
            mantenimientoLabel.setText(String.valueOf(enMantenimiento));
            visitantesAcumuladosLabel.setText(String.valueOf(visitantesTotal));
        } else {
            zonaAsignadaLabel.setText("Sin zona asignada");
            totalAtraccionesLabel.setText("0");
            abieratasLabel.setText("0");
            mantenimientoLabel.setText("0");
            visitantesAcumuladosLabel.setText("0");
        }
    }

    private void configurarSeleccion() {
        gestionarButton.setDisable(true);
        atraccionesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) ->
                        gestionarButton.setDisable(seleccionada == null));
    }

    @FXML
    private void irAGestionAtraccion() {
        Atraccion seleccionada = atraccionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        AppContext.getInstance().setAtraccionSeleccionada(seleccionada);
        navegarA(VISTA_GESTION_ATRACCION_OPERADOR);
    }

    @FXML
    private void cerrarSesion() {
        AppContext.getInstance().cerrarSesion();
        navegarA(VISTA_LOGIN);
    }
}