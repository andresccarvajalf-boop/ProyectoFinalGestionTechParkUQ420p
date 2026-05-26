/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.operador;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Operador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.RevisionTecnica;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class GestionAtraccionOperadorController extends BaseController implements Initializable {

    @FXML private Label nombreAtraccionLabel;
    @FXML private Label estadoActualLabel;

    @FXML private ChoiceBox<EstadoAtraccion> nuevoEstadoChoice;
    @FXML private Label errorEstadoLabel;

    @FXML private TextField documentoVisitanteField;
    @FXML private Label     resultadoValidacionLabel;

    @FXML private Label  totalRevisionesLabel;
    @FXML private Button registrarRevisionButton;

    @FXML private TableView<RevisionTecnica>           revisionesTable;
    @FXML private TableColumn<RevisionTecnica, String> colFechaRevision;
    @FXML private TableColumn<RevisionTecnica, String> colOperadorRevision;
    @FXML private TableColumn<RevisionTecnica, String> colDescripcion;
    @FXML private TableColumn<RevisionTecnica, String> colResultado;

    private Atraccion atraccion;
    private Operador  operadorActivo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        atraccion      = AppContext.getInstance().getAtraccionSeleccionada();
        operadorActivo = AppContext.getInstance().getOperadorActivo();

        nuevoEstadoChoice.setItems(FXCollections.observableArrayList(EstadoAtraccion.values()));
        configurarTablaRevisiones();
        cargarDatos();
    }

    private void configurarTablaRevisiones() {
        colFechaRevision.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFechaRevision() != null
                                ? data.getValue().getFechaRevision().toString() : "—"));
        colOperadorRevision.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getOperador() != null
                                ? data.getValue().getOperador().getNombre() : "—"));
        colDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion()));
        colResultado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isAprobada() ? "✔ Aprobada" : "✘ Rechazada"));
    }

    private void cargarDatos() {
        if (atraccion == null) return;

        nombreAtraccionLabel.setText(atraccion.getNombre());
        estadoActualLabel.setText(atraccion.getEstado() != null ? atraccion.getEstado().name() : "—");
        nuevoEstadoChoice.setValue(atraccion.getEstado());
        ocultarMensajes();

        int totalRevisiones = atraccion.getRevisiones() != null ? atraccion.getRevisiones().size() : 0;
        totalRevisionesLabel.setText(String.valueOf(totalRevisiones));
        registrarRevisionButton.setDisable(atraccion.getEstado() != EstadoAtraccion.EN_MANTENIMIENTO);

        if (atraccion.getRevisiones() != null) {
            revisionesTable.setItems(FXCollections.observableArrayList(atraccion.getRevisiones()));
        }
    }

    @FXML
    private void handleCambiarEstado() {
        ocultarMensajes();
        EstadoAtraccion nuevoEstado = nuevoEstadoChoice.getValue();

        if (nuevoEstado == null) {
            mostrarErrorEstado("Selecciona un estado.");
            return;
        }
        if (nuevoEstado == atraccion.getEstado()) {
            mostrarErrorEstado("La atracción ya está en ese estado.");
            return;
        }

        operadorActivo.cambiarEstadoAtraccion(atraccion, nuevoEstado);
        AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
        cargarDatos();
    }

    @FXML
    private void handleValidarIngreso() {
        ocultarMensajes();
        String documento = documentoVisitanteField.getText().trim();

        if (documento.isEmpty()) {
            mostrarResultadoValidacion("Ingresa el documento del visitante.", false);
            return;
        }
        if (atraccion.getEstado() != EstadoAtraccion.ACTIVA) {
            mostrarResultadoValidacion("La atracción no está activa.", false);
            return;
        }

        Visitante visitante = AppContext.getInstance().getParque()
                .getVisitantes().stream()
                .filter(v -> v.getDocumento().equalsIgnoreCase(documento))
                .findFirst()
                .orElse(null);

        if (visitante == null) {
            mostrarResultadoValidacion("No se encontró visitante con documento: " + documento, false);
            return;
        }

        boolean acceso = operadorActivo.validarIngreso(visitante, atraccion);
        if (acceso) {
            atraccion.registrarIngreso(visitante);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            documentoVisitanteField.clear();
            cargarDatos();
            mostrarResultadoValidacion("✔ Acceso autorizado: " + visitante.getNombre(), true);
        } else {
            mostrarResultadoValidacion(
                    "✘ Acceso denegado para " + visitante.getNombre()
                    + ". Verifica edad, altura o estado.", false);
        }
    }

    @FXML
    private void handleRegistrarRevision() {
        ocultarMensajes();

        if (atraccion.getEstado() != EstadoAtraccion.EN_MANTENIMIENTO) {
            mostrarAlerta("No disponible",
                    "Solo se puede registrar revisión cuando la atracción está EN_MANTENIMIENTO.");
            return;
        }

        abrirDialogoRevision().ifPresent(revision -> {
            operadorActivo.registrarRevisionTecnica(revision);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            cargarDatos();
            mostrarAlerta("Revisión registrada",
                    revision.isAprobada()
                            ? "Revisión aprobada. La atracción vuelve a estar ACTIVA."
                            : "Revisión registrada con observaciones pendientes.");
        });
    }

    @FXML
    private void onVolver() {
        navegarA(VISTA_DASHBOARD_OPERADOR);
    }

    private Optional<RevisionTecnica> abrirDialogoRevision() {
        Dialog<RevisionTecnica> dialogo = new Dialog<>();
        dialogo.setTitle("Registrar Revisión Técnica");
        dialogo.setHeaderText("Atracción: " + atraccion.getNombre());

        ButtonType btnAprobar  = new ButtonType("Aprobar",  ButtonBar.ButtonData.OK_DONE);
        ButtonType btnRechazar = new ButtonType("Rechazar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnAprobar, btnRechazar);

        TextField tfDescripcion   = new TextField();
        tfDescripcion.setPromptText("Descripción de la revisión");
        TextArea taObservaciones  = new TextArea();
        taObservaciones.setPromptText("Observaciones");
        taObservaciones.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Descripción:"),   0, 0); grid.add(tfDescripcion,  1, 0);
        grid.add(new Label("Observaciones:"), 0, 1); grid.add(taObservaciones, 1, 1);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (tfDescripcion.getText().isBlank()) return null;

            RevisionTecnica revision = new RevisionTecnica(
                    UUID.randomUUID().toString(),
                    tfDescripcion.getText().trim(),
                    operadorActivo,
                    atraccion
            );
            revision.actualizarObservaciones(taObservaciones.getText().trim());
            revision.realizarRevision();

            if (boton == btnAprobar) {
                revision.aprobarRevision();
            } else {
                revision.rechazarRevision(taObservaciones.getText().trim());
            }
            return revision;
        });

        return dialogo.showAndWait();
    }

    private void ocultarMensajes() {
        errorEstadoLabel.setVisible(false);
        errorEstadoLabel.setManaged(false);
        resultadoValidacionLabel.setVisible(false);
        resultadoValidacionLabel.setManaged(false);
    }

    private void mostrarErrorEstado(String mensaje) {
        errorEstadoLabel.setText(mensaje);
        errorEstadoLabel.setVisible(true);
        errorEstadoLabel.setManaged(true);
    }

    private void mostrarResultadoValidacion(String mensaje, boolean exito) {
        resultadoValidacionLabel.setText(mensaje);
        resultadoValidacionLabel.setStyle(exito
                ? "-fx-text-fill: #27ae60;"
                : "-fx-text-fill: #c0392b;");
        resultadoValidacionLabel.setVisible(true);
        resultadoValidacionLabel.setManaged(true);
    }
}