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

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.HistorialVisita;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GestionVisitantesController extends BaseController implements Initializable {

    @FXML private Label totalVisitantesLabel;
    @FXML private Label visitantesActivosLabel;
    @FXML private Label visitantesConTicketLabel;

    @FXML private TextField buscarField;
    @FXML private Label     resultadosBusquedaLabel;

    @FXML private TableView<Visitante>           visitantesTable;
    @FXML private TableColumn<Visitante, String> colDocumento;
    @FXML private TableColumn<Visitante, String> colNombre;
    @FXML private TableColumn<Visitante, String> colEdad;
    @FXML private TableColumn<Visitante, String> colAltura;
    @FXML private TableColumn<Visitante, String> colSaldo;
    @FXML private TableColumn<Visitante, String> colTicket;

    @FXML private Label sinSeleccionLabel;
    @FXML private VBox  perfilBox;
    @FXML private Label perfilNombreLabel;
    @FXML private Label perfilDocumentoLabel;
    @FXML private Label perfilEdadLabel;
    @FXML private Label perfilAlturaLabel;
    @FXML private Label perfilSaldoLabel;
    @FXML private Label perfilTicketLabel;

    @FXML private TableView<HistorialVisita>           historialTable;
    @FXML private TableColumn<HistorialVisita, String> colHistFecha;
    @FXML private TableColumn<HistorialVisita, String> colHistAtraccion;

    private ObservableList<Visitante> listaVisitantes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        cargarVisitantes();
        configurarSeleccion();
        limpiarPerfil();
    }

    private void configurarColumnas() {
        colDocumento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDocumento()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colEdad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getEdad())));
        colAltura.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.0f cm", d.getValue().getEstatura() * 100)));
        colSaldo.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("$%.0f", d.getValue().getSaldoVirtual())));
        colTicket.setCellValueFactory(d -> {
            if (d.getValue().getTicketActivo() == null) return new SimpleStringProperty("Sin ticket");
            return new SimpleStringProperty(d.getValue().getTicketActivo().getTipoTicket().name());
        });

        colHistFecha.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFechaVisita() != null ? d.getValue().getFechaVisita().toString() : "—"));
        colHistAtraccion.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getAtraccionesVisitadas() != null
                        ? String.valueOf(d.getValue().getAtraccionesVisitadas().size()) + " atracciones"
                        : "—"));
    }

    private void cargarVisitantes() {
        List<Visitante> visitantes = AppContext.getInstance().getParque().getVisitantes();
        listaVisitantes = FXCollections.observableArrayList(visitantes);
        visitantesTable.setItems(listaVisitantes);
        actualizarContadores(visitantes);
    }

    private void actualizarContadores(List<Visitante> visitantes) {
        long conTicket = visitantes.stream()
                .filter(v -> v.getTicketActivo() != null).count();

        totalVisitantesLabel.setText(String.valueOf(visitantes.size()));
        visitantesActivosLabel.setText(String.valueOf(visitantes.size()));
        visitantesConTicketLabel.setText(String.valueOf(conTicket));
        resultadosBusquedaLabel.setText(visitantes.size() + " resultado(s)");
    }

    private void configurarSeleccion() {
        visitantesTable.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            if (sel != null) mostrarPerfil(sel);
            else limpiarPerfil();
        });
    }

    private void mostrarPerfil(Visitante v) {
        sinSeleccionLabel.setVisible(false);
        sinSeleccionLabel.setManaged(false);
        perfilBox.setVisible(true);
        perfilBox.setManaged(true);

        perfilNombreLabel.setText(v.getNombre());
        perfilDocumentoLabel.setText(v.getDocumento());
        perfilEdadLabel.setText(v.getEdad() + " años");
        perfilAlturaLabel.setText(String.format("%.2f m", v.getEstatura()));
        perfilSaldoLabel.setText(String.format("$%.0f", v.getSaldoVirtual()));
        perfilTicketLabel.setText(v.getTicketActivo() != null
                ? v.getTicketActivo().getTipoTicket().name() : "Ninguno");

        historialTable.setItems(FXCollections.observableArrayList(v.getHistorialVisitas()));
    }

    private void limpiarPerfil() {
        sinSeleccionLabel.setVisible(true);
        sinSeleccionLabel.setManaged(true);
        perfilBox.setVisible(false);
        perfilBox.setManaged(false);
        historialTable.setItems(FXCollections.emptyObservableList());
    }

    @FXML private void buscarVisitante() {
        String texto = buscarField.getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarVisitantes(); return; }
        List<Visitante> filtrados = AppContext.getInstance().getParque().getVisitantes().stream()
                .filter(v -> v.getNombre().toLowerCase().contains(texto)
                        || v.getDocumento().toLowerCase().contains(texto))
                .toList();
        visitantesTable.setItems(FXCollections.observableArrayList(filtrados));
        actualizarContadores(filtrados);
        limpiarPerfil();
    }

    @FXML private void limpiarBusqueda() {
        buscarField.clear();
        cargarVisitantes();
        limpiarPerfil();
    }

    @FXML private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }
}