package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.HistorialVisita;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionVisitantesController extends BaseController {

    @FXML private TableView<Visitante> tablaVisitantes;
    @FXML private TableColumn<Visitante, String> colId;
    @FXML private TableColumn<Visitante, String> colNombre;
    @FXML private TableColumn<Visitante, String> colDocumento;
    @FXML private TableColumn<Visitante, String> colEdad;
    @FXML private TableColumn<Visitante, String> colEstatura;
    @FXML private TableColumn<Visitante, String> colSaldo;
    @FXML private TableColumn<Visitante, String> colTicket;

    @FXML private TableView<HistorialVisita> tablaHistorial;
    @FXML private TableColumn<HistorialVisita, String> colFechaVisita;
    @FXML private TableColumn<HistorialVisita, String> colAtraccionesVisitadas;
    @FXML private TableColumn<HistorialVisita, String> colGasto;

    @FXML private TextField txtBuscar;
    @FXML private Label lblNombreSeleccionado;
    @FXML private Label lblDocumento;
    @FXML private Label lblEdad;
    @FXML private Label lblEstatura;
    @FXML private Label lblSaldo;
    @FXML private Label lblTicketActivo;
    @FXML private Label lblFavoritas;
    @FXML private Button btnVolver;

    private ObservableList<Visitante> listaVisitantes;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarVisitantes();
        configurarSeleccion();
        limpiarPanelDetalle();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colDocumento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDocumento()));
        colEdad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEdad())));
        colEstatura.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.2f m", data.getValue().getEstatura())));
        colSaldo.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("$%.0f", data.getValue().getSaldoVirtual())));
        colTicket.setCellValueFactory(data -> {
            if (data.getValue().getTicketActivo() == null) return new SimpleStringProperty("Sin ticket");
            return new SimpleStringProperty(data.getValue().getTicketActivo().getTipoTicket().name());
        });

        colFechaVisita.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFechaVisita().toString()));
        colAtraccionesVisitadas.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getAtraccionesVisitadas().size())));
        colGasto.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("$%.0f", data.getValue().calcularGastoTotal())));
    }

    private void cargarVisitantes() {
        List<Visitante> visitantes = AppContext.getInstance().getParque().getVisitantes();
        listaVisitantes = FXCollections.observableArrayList(visitantes);
        tablaVisitantes.setItems(listaVisitantes);
    }

    private void configurarSeleccion() {
        tablaVisitantes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                mostrarDetalle(actual);
            } else {
                limpiarPanelDetalle();
            }
        });
    }

    private void mostrarDetalle(Visitante visitante) {
        lblNombreSeleccionado.setText(visitante.getNombre());
        lblDocumento.setText("Documento: " + visitante.getDocumento());
        lblEdad.setText("Edad: " + visitante.getEdad() + " años");
        lblEstatura.setText("Estatura: " + String.format("%.2f m", visitante.getEstatura()));
        lblSaldo.setText("Saldo virtual: $" + String.format("%.0f", visitante.getSaldoVirtual()));
        lblTicketActivo.setText("Ticket activo: " +
                (visitante.getTicketActivo() != null
                        ? visitante.getTicketActivo().getTipoTicket().name()
                        : "Ninguno"));
        lblFavoritas.setText("Atracciones favoritas: " + visitante.getAtraccionesFavoritas().size());

        List<HistorialVisita> historial = visitante.getHistorialVisitas();
        tablaHistorial.setItems(FXCollections.observableArrayList(historial));
    }

    private void limpiarPanelDetalle() {
        lblNombreSeleccionado.setText("— Selecciona un visitante —");
        lblDocumento.setText("");
        lblEdad.setText("");
        lblEstatura.setText("");
        lblSaldo.setText("");
        lblTicketActivo.setText("");
        lblFavoritas.setText("");
        tablaHistorial.setItems(FXCollections.emptyObservableList());
    }

    @FXML
    private void onBuscar() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            cargarVisitantes();
            return;
        }
        List<Visitante> filtrados = AppContext.getInstance().getParque().getVisitantes().stream()
                .filter(v -> v.getNombre().toLowerCase().contains(filtro)
                        || v.getDocumento().toLowerCase().contains(filtro))
                .toList();
        tablaVisitantes.setItems(FXCollections.observableArrayList(filtrados));
        limpiarPanelDetalle();
    }

    @FXML
    private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }
}