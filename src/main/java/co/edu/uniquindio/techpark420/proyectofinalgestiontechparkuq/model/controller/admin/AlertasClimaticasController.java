package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AlertasClimaticasController extends BaseController {

    @FXML private TableView<AlertaClimatica> tablaAlertas;
    @FXML private TableColumn<AlertaClimatica, String> colTipo;
    @FXML private TableColumn<AlertaClimatica, String> colDescripcion;
    @FXML private TableColumn<AlertaClimatica, String> colFecha;
    @FXML private TableColumn<AlertaClimatica, String> colEstado;

    @FXML private TableView<Atraccion> tablaAfectadas;
    @FXML private TableColumn<Atraccion, String> colNombreAtraccion;
    @FXML private TableColumn<Atraccion, String> colTipoAtraccion;
    @FXML private TableColumn<Atraccion, String> colEstadoAtraccion;

    @FXML private Label lblResumenAlerta;
    @FXML private Button btnNuevaAlerta;
    @FXML private Button btnDesactivar;
    @FXML private Button btnVolver;

    private ObservableList<AlertaClimatica> listaAlertas;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarAlertas();
        configurarSeleccion();
    }

    private void configurarColumnas() {
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipoAlerta()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colFecha.setCellValueFactory(data -> {
            if (data.getValue().getFechaActivacion() == null) return new SimpleStringProperty("—");
            return new SimpleStringProperty(data.getValue().getFechaActivacion()
                    .toLocalDate().toString());
        });
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isActiva() ? "Activa" : "Inactiva"));

        colNombreAtraccion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colTipoAtraccion.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTipoAtraccion().name()));
        colEstadoAtraccion.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getEstado().name()));
    }

    private void cargarAlertas() {
        List<AlertaClimatica> alertas = AppContext.getInstance().getParque().getAlertasClimaticas();
        listaAlertas = FXCollections.observableArrayList(alertas);
        tablaAlertas.setItems(listaAlertas);
        tablaAfectadas.setItems(FXCollections.emptyObservableList());
        lblResumenAlerta.setText("Selecciona una alerta para ver las atracciones afectadas.");
        btnDesactivar.setDisable(true);
    }

    private void configurarSeleccion() {
        tablaAlertas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                List<Atraccion> afectadas = actual.obtenerAtraccionesAfectadas();
                tablaAfectadas.setItems(FXCollections.observableArrayList(afectadas));
                lblResumenAlerta.setText("Alerta: " + actual.getTipoAlerta()
                        + " | Atracciones afectadas: " + afectadas.size());
                btnDesactivar.setDisable(!actual.isActiva());
            } else {
                tablaAfectadas.setItems(FXCollections.emptyObservableList());
                lblResumenAlerta.setText("Selecciona una alerta para ver las atracciones afectadas.");
                btnDesactivar.setDisable(true);
            }
        });
    }

    @FXML
    private void onNuevaAlerta() {
        Dialog<AlertaClimatica> dialogo = construirDialogoAlerta();
        Optional<AlertaClimatica> resultado = dialogo.showAndWait();
        resultado.ifPresent(alerta -> {
            resolverAtraccionesAfectadas(alerta);
            alerta.activarAlerta();
            alerta.notificarVisitantes();

            Administrador admin = AppContext.getInstance().getAdminActivo();
            admin.activarAlerta(alerta);
            AppContext.getInstance().getParque().activarAlerta(alerta);
            AppContext.getInstance().getParque().getAlertasClimaticas().add(alerta);

            cargarAlertas();
            mostrarAlerta("Alerta activada",
                    "La alerta fue activada. Atracciones afectadas: "
                            + alerta.obtenerAtraccionesAfectadas().size());
        });
    }

    @FXML
    private void onDesactivar() {
        AlertaClimatica seleccionada = tablaAlertas.getSelectionModel().getSelectedItem();
        if (seleccionada == null || !seleccionada.isActiva()) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Desactivar alerta");
        confirmacion.setHeaderText("¿Desactivar la alerta \"" + seleccionada.getTipoAlerta() + "\"?");
        confirmacion.setContentText("Las atracciones no se reactivarán automáticamente.");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            seleccionada.desactivarAlerta();
            tablaAlertas.refresh();
            btnDesactivar.setDisable(true);
            mostrarAlerta("Alerta desactivada", "La alerta fue desactivada correctamente.");
        }
    }

    @FXML
    private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }

    private void resolverAtraccionesAfectadas(AlertaClimatica alerta) {
        List<Atraccion> todasLasAtracciones = AppContext.getInstance().getParque().obtenerAtraccionesDisponibles();
        for (Atraccion atraccion : todasLasAtracciones) {
            TipoAtraccion tipo = atraccion.getTipoAtraccion();
            if (tipo == TipoAtraccion.ACUATICA || tipo == TipoAtraccion.EXTREMA) {
                alerta.agregarAtraccion(atraccion);
            }
        }
    }

    private Dialog<AlertaClimatica> construirDialogoAlerta() {
        Dialog<AlertaClimatica> dialogo = new Dialog<>();
        dialogo.setTitle("Nueva Alerta Climática");

        ButtonType btnActivar = new ButtonType("Activar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnActivar, ButtonType.CANCEL);

        ChoiceBox<String> choiceTipo = new ChoiceBox<>();
        choiceTipo.getItems().addAll("Tormenta eléctrica", "Lluvia fuerte", "Viento extremo", "Granizo");
        choiceTipo.setValue("Tormenta eléctrica");

        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción de la alerta");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(new Label("Tipo de alerta:"), 0, 0);  grid.add(choiceTipo, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);     grid.add(txtDescripcion, 1, 1);

        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton == btnActivar) {
                String tipo = choiceTipo.getValue();
                String descripcion = txtDescripcion.getText().trim();
                if (descripcion.isEmpty()) descripcion = tipo;
                Administrador admin = AppContext.getInstance().getAdminActivo();
                return new AlertaClimatica(UUID.randomUUID().toString(), tipo, descripcion, admin);
            }
            return null;
        });

        return dialogo;
    }
}