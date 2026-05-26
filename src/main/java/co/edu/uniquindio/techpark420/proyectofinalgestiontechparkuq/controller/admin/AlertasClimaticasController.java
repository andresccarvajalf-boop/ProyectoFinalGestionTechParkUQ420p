/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.AlertaClimatica;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AlertasClimaticasController extends BaseController implements Initializable {

    @FXML private Label alertasActivasLabel;
    @FXML private Label contadorActivasLabel;
    @FXML private Label atraccionesCerradasLabel;
    @FXML private Label totalHistoricasLabel;

    @FXML private TableView<AlertaClimatica>           alertasTable;
    @FXML private TableColumn<AlertaClimatica, String> colTipo;
    @FXML private TableColumn<AlertaClimatica, String> colDescripcion;
    @FXML private TableColumn<AlertaClimatica, String> colFechaInicio;
    @FXML private TableColumn<AlertaClimatica, String> colEstadoAlerta;
    @FXML private Button                               desactivarAlertaButton;

    @FXML private Label            sinAlertaLabel;
    @FXML private ListView<String> atraccionesAfectadasList;
    @FXML private VBox             detalleAlertaBox;
    @FXML private Label            detalleTipoLabel;
    @FXML private Label            detalleDescripcionLabel;
    @FXML private Label            detalleFechaInicioLabel;
    @FXML private Label            detalleFechaFinLabel;

    private ObservableList<AlertaClimatica> listaAlertas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        cargarAlertas();
        configurarSeleccion();
    }

    private void configurarColumnas() {
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoAlerta()));
        colDescripcion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescripcion()));
        colFechaInicio.setCellValueFactory(d -> {
            if (d.getValue().getFechaActivacion() == null) return new SimpleStringProperty("—");
            return new SimpleStringProperty(d.getValue().getFechaActivacion().toLocalDate().toString());
        });
        colEstadoAlerta.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().isActiva() ? "Activa" : "Inactiva"));
    }

    private void cargarAlertas() {
        List<AlertaClimatica> alertas = AppContext.getInstance().getParque().getAlertasClimaticas();
        listaAlertas = FXCollections.observableArrayList(alertas);
        alertasTable.setItems(listaAlertas);
        desactivarAlertaButton.setDisable(true);
        limpiarPanelDerecho();
        actualizarContadores(alertas);
    }

    private void actualizarContadores(List<AlertaClimatica> alertas) {
        long activas = alertas.stream().filter(AlertaClimatica::isActiva).count();
        long cerradas = AppContext.getInstance().getParque().getZonas().stream()
                .flatMap(z -> z.getAtracciones().stream())
                .filter(a -> a.getEstado().name().equals("CERRADA"))
                .count();

        contadorActivasLabel.setText(String.valueOf(activas));
        atraccionesCerradasLabel.setText(String.valueOf(cerradas));
        totalHistoricasLabel.setText(String.valueOf(alertas.size()));
        alertasActivasLabel.setText(activas > 0 ? "⚠ " + activas + " alerta(s) activa(s)" : "Sin alertas activas");
    }

    private void configurarSeleccion() {
        alertasTable.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            if (sel != null) mostrarDetalle(sel);
            else limpiarPanelDerecho();
        });
    }

    private void mostrarDetalle(AlertaClimatica alerta) {
        desactivarAlertaButton.setDisable(!alerta.isActiva());

        List<String> nombres = alerta.obtenerAtraccionesAfectadas().stream()
                .map(Atraccion::getNombre).toList();
        atraccionesAfectadasList.setItems(FXCollections.observableArrayList(nombres));

        sinAlertaLabel.setVisible(false);
        sinAlertaLabel.setManaged(false);
        atraccionesAfectadasList.setVisible(true);
        atraccionesAfectadasList.setManaged(true);
        detalleAlertaBox.setVisible(true);
        detalleAlertaBox.setManaged(true);

        detalleTipoLabel.setText(alerta.getTipoAlerta());
        detalleDescripcionLabel.setText(alerta.getDescripcion());
        detalleFechaInicioLabel.setText(alerta.getFechaActivacion() != null
                ? alerta.getFechaActivacion().toLocalDate().toString() : "—");
        detalleFechaFinLabel.setText("—");
    }

    private void limpiarPanelDerecho() {
        sinAlertaLabel.setVisible(true);
        sinAlertaLabel.setManaged(true);
        atraccionesAfectadasList.setVisible(false);
        atraccionesAfectadasList.setManaged(false);
        detalleAlertaBox.setVisible(false);
        detalleAlertaBox.setManaged(false);
    }

    @FXML private void handleActivarAlerta() {
        construirDialogoAlerta().showAndWait().ifPresent(alerta -> {
            resolverAtraccionesAfectadas(alerta);
            alerta.activarAlerta();
            alerta.notificarVisitantes();
            AppContext.getInstance().getParque().activarAlerta(alerta);
            AppContext.getInstance().guardarDatos();
            cargarAlertas();
            mostrarAlerta("Alerta activada",
                    "Atracciones afectadas: " + alerta.obtenerAtraccionesAfectadas().size());
        });
    }

    @FXML private void handleDesactivarAlerta() {
        AlertaClimatica seleccionada = alertasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null || !seleccionada.isActiva()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Desactivar alerta");
        confirm.setHeaderText("¿Desactivar \"" + seleccionada.getTipoAlerta() + "\"?");
        confirm.setContentText("Las atracciones no se reactivarán automáticamente.");
        confirm.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> {
            seleccionada.desactivarAlerta();
            AppContext.getInstance().guardarDatos();
            alertasTable.refresh();
            desactivarAlertaButton.setDisable(true);
            actualizarContadores(listaAlertas);
            mostrarAlerta("Alerta desactivada", "La alerta fue desactivada correctamente.");
        });
    }

    @FXML private void filtrarActivas() {
        List<AlertaClimatica> activas = AppContext.getInstance().getParque().getAlertasClimaticas()
                .stream().filter(AlertaClimatica::isActiva).toList();
        alertasTable.setItems(FXCollections.observableArrayList(activas));
        limpiarPanelDerecho();
    }

    @FXML private void filtrarTodas() {
        cargarAlertas();
    }

    @FXML private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }

    private void resolverAtraccionesAfectadas(AlertaClimatica alerta) {
        AppContext.getInstance().getParque().obtenerAtraccionesDisponibles().stream()
                .filter(a -> a.getTipoAtraccion() == TipoAtraccion.ACUATICA
                        || a.getTipoAtraccion() == TipoAtraccion.EXTREMA)
                .forEach(alerta::agregarAtraccion);
    }

    private Dialog<AlertaClimatica> construirDialogoAlerta() {
        Dialog<AlertaClimatica> dialogo = new Dialog<>();
        dialogo.setTitle("Nueva Alerta Climática");

        ButtonType btnActivar = new ButtonType("Activar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnActivar, ButtonType.CANCEL);

        ChoiceBox<String> choiceTipo = new ChoiceBox<>(FXCollections.observableArrayList(
                "Tormenta eléctrica", "Lluvia fuerte", "Viento extremo", "Granizo"));
        choiceTipo.setValue("Tormenta eléctrica");

        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción de la alerta");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        grid.add(new Label("Tipo de alerta:"), 0, 0); grid.add(choiceTipo,     1, 0);
        grid.add(new Label("Descripción:"),    0, 1); grid.add(txtDescripcion, 1, 1);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton != btnActivar) return null;
            String tipo = choiceTipo.getValue();
            String desc = txtDescripcion.getText().trim();
            if (desc.isEmpty()) desc = tipo;
            Administrador admin = AppContext.getInstance().getAdminActivo();
            return new AlertaClimatica(UUID.randomUUID().toString(), tipo, desc, admin);
        });

        return dialogo;
    }
}