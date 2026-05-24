package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Notificacion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NotificacionesController extends BaseController implements Initializable {

    @FXML private TableView<Notificacion> tablaNotificaciones;
    @FXML private TableColumn<Notificacion, String> colTitulo;
    @FXML private TableColumn<Notificacion, String> colMensaje;
    @FXML private TableColumn<Notificacion, String> colFecha;
    @FXML private TableColumn<Notificacion, String> colEstado;
    @FXML private Button btnMarcarLeida;
    @FXML private Button btnMarcarTodas;
    @FXML private Button btnVolver;
    @FXML private Label lblNoLeidas;

    private ObservableList<Notificacion> listaNotificaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        appContext.clearCallbackTurno();

        configurarColumnas();
        cargarNotificaciones();
        configurarSeleccion();
    }

    private void configurarColumnas() {
        colTitulo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitulo()));

        colMensaje.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMensaje()));

        colFecha.setCellValueFactory(data -> {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(
                    data.getValue().getFechaEnvio() != null
                            ? data.getValue().getFechaEnvio().format(fmt)
                            : "");
        });

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isLeida() ? "Leída" : "No leída"));
    }

    private void cargarNotificaciones() {
        Visitante visitante = AppContext.getInstance().getVisitanteActivo();
        if (visitante == null) return;

        List<Notificacion> notificaciones = visitante.listarNotificaciones();
        listaNotificaciones = FXCollections.observableArrayList(notificaciones);
        tablaNotificaciones.setItems(listaNotificaciones);
        actualizarContadorNoLeidas();
    }

    private void configurarSeleccion() {
        tablaNotificaciones.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) ->
                        btnMarcarLeida.setDisable(seleccionada == null || seleccionada.isLeida())
        );
        btnMarcarLeida.setDisable(true);
    }

    @FXML
    private void onMarcarLeida() {
        Notificacion seleccionada = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        seleccionada.marcarComoLeida();
        tablaNotificaciones.refresh();
        actualizarContadorNoLeidas();
        btnMarcarLeida.setDisable(true);
    }

    @FXML
    private void onMarcarTodas() {
        listaNotificaciones.forEach(n -> {
            if (!n.isLeida()) n.marcarComoLeida();
        });
        tablaNotificaciones.refresh();
        actualizarContadorNoLeidas();
        btnMarcarLeida.setDisable(true);
    }

    private void actualizarContadorNoLeidas() {
        long noLeidas = listaNotificaciones.stream()
                .filter(n -> !n.isLeida())
                .count();
        lblNoLeidas.setText("Sin leer: " + noLeidas);
    }

    @FXML
    private void onFiltrarNoLeidas() {
        Visitante visitante = AppContext.getInstance().getVisitanteActivo();
        if (visitante == null) return;
        List<Notificacion> noLeidas = visitante.listarNotificaciones().stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
        tablaNotificaciones.setItems(FXCollections.observableArrayList(noLeidas));
    }

    @FXML
    private void onMostrarTodas() {
        cargarNotificaciones();
    }

    @FXML
    private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/dashboard-visitante.fxml");
    }
}