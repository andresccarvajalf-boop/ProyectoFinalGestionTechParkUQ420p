package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class GestionZonasAtraccionesController extends BaseController implements Initializable {

    @FXML private TableView<Zona> tablaZonas;
    @FXML private TableColumn<Zona, String> colZonaId;
    @FXML private TableColumn<Zona, String> colZonaNombre;
    @FXML private TableColumn<Zona, String> colZonaCapacidad;
    @FXML private TableColumn<Zona, String> colZonaAtracciones;
    @FXML private TableColumn<Zona, String> colZonaEstado;
    @FXML private Button btnNuevaZona;
    @FXML private Button btnEditarZona;
    @FXML private Button btnEliminarZona;

    @FXML private TableView<Atraccion> tablaAtracciones;
    @FXML private TableColumn<Atraccion, String> colAtraccionId;
    @FXML private TableColumn<Atraccion, String> colAtraccionNombre;
    @FXML private TableColumn<Atraccion, String> colAtraccionTipo;
    @FXML private TableColumn<Atraccion, String> colAtraccionZona;
    @FXML private TableColumn<Atraccion, String> colAtraccionEstado;
    @FXML private TableColumn<Atraccion, String> colAtraccionCapacidad;
    @FXML private Button btnNuevaAtraccion;
    @FXML private Button btnEditarAtraccion;
    @FXML private Button btnEliminarAtraccion;

    @FXML private Button btnVolver;

    private ObservableList<Zona> listaZonas;
    private ObservableList<Atraccion> listaAtracciones;
    private Administrador administrador;
    private Parque parque;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        administrador = AppContext.getInstance().getAdministradorActivo();
        parque = AppContext.getInstance().getParque();
        configurarColumnasZonas();
        configurarColumnasAtracciones();
        cargarZonas();
        cargarTodasLasAtracciones();
        configurarSeleccionZonas();
        configurarSeleccionAtracciones();
    }

    private void configurarColumnasZonas() {
        colZonaId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colZonaNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colZonaCapacidad.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCapacidadMaxima())));
        colZonaAtracciones.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getAtracciones().size())));
        colZonaEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isActiva() ? "Activa" : "Inactiva"));
    }

    private void configurarColumnasAtracciones() {
        colAtraccionId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colAtraccionNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colAtraccionTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoAtraccion() != null
                        ? data.getValue().getTipoAtraccion().name() : ""));
        colAtraccionZona.setCellValueFactory(data -> {
            String nombreZona = parque.getZonas().stream()
                    .filter(z -> z.getAtracciones().contains(data.getValue()))
                    .map(Zona::getNombre)
                    .findFirst()
                    .orElse("Sin zona");
            return new SimpleStringProperty(nombreZona);
        });
        colAtraccionEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado() != null
                        ? data.getValue().getEstado().name() : ""));
        colAtraccionCapacidad.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCapacidadMaxima())));
    }

    private void cargarZonas() {
        if (parque == null) return;
        listaZonas = FXCollections.observableArrayList(parque.getZonas());
        tablaZonas.setItems(listaZonas);
    }

    private void cargarTodasLasAtracciones() {
        if (parque == null) return;
        List<Atraccion> todas = parque.obtenerAtraccionesDisponibles();
        listaAtracciones = FXCollections.observableArrayList(todas);
        tablaAtracciones.setItems(listaAtracciones);
    }

    private void configurarSeleccionZonas() {
        tablaZonas.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean ninguna = sel == null;
            btnEditarZona.setDisable(ninguna);
            btnEliminarZona.setDisable(ninguna);
        });
        btnEditarZona.setDisable(true);
        btnEliminarZona.setDisable(true);
    }

    private void configurarSeleccionAtracciones() {
        tablaAtracciones.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean ninguna = sel == null;
            btnEditarAtraccion.setDisable(ninguna);
            btnEliminarAtraccion.setDisable(ninguna);
        });
        btnEditarAtraccion.setDisable(true);
        btnEliminarAtraccion.setDisable(true);
    }

    @FXML
    private void onNuevaZona() {
        Optional<Zona> resultado = abrirDialogoZona(null);
        resultado.ifPresent(zona -> {
            administrador.crearZona(zona);
            parque.agregarZona(zona);
            listaZonas.add(zona);
            mostrarAlerta("Zona creada", "La zona \"" + zona.getNombre() + "\" fue creada exitosamente.");
        });
    }

    @FXML
    private void onEditarZona() {
        Zona seleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Optional<Zona> resultado = abrirDialogoZona(seleccionada);
        resultado.ifPresent(zona -> {
            administrador.modificarZona(zona);
            tablaZonas.refresh();
            mostrarAlerta("Zona actualizada", "La zona fue actualizada correctamente.");
        });
    }

    @FXML
    private void onEliminarZona() {
        Zona seleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        if (!confirmar("Eliminar zona", "¿Eliminar la zona \"" + seleccionada.getNombre() + "\"? Esta acción no se puede deshacer.")) return;
        administrador.eliminarZona(seleccionada);
        parque.removerZona(seleccionada);
        listaZonas.remove(seleccionada);
        cargarTodasLasAtracciones();
    }

    @FXML
    private void onNuevaAtraccion() {
        if (parque.getZonas().isEmpty()) {
            mostrarError("Debes crear al menos una zona antes de agregar atracciones.");
            return;
        }
        Optional<Object[]> resultado = abrirDialogoAtraccion(null, null);
        resultado.ifPresent(datos -> {
            Atraccion atraccion = (Atraccion) datos[0];
            Zona zona = (Zona) datos[1];
            administrador.crearAtraccion(atraccion);
            zona.agregarAtraccion(atraccion);
            listaAtracciones.add(atraccion);
            tablaAtracciones.refresh();
            mostrarAlerta("Atracción creada", "La atracción \"" + atraccion.getNombre() + "\" fue creada en la zona \"" + zona.getNombre() + "\".");
        });
    }

    @FXML
    private void onEditarAtraccion() {
        Atraccion seleccionada = tablaAtracciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Zona zonaActual = parque.getZonas().stream()
                .filter(z -> z.getAtracciones().contains(seleccionada))
                .findFirst().orElse(null);
        Optional<Object[]> resultado = abrirDialogoAtraccion(seleccionada, zonaActual);
        resultado.ifPresent(datos -> {
            Zona nuevaZona = (Zona) datos[1];
            administrador.modificarAtraccion(seleccionada);
            if (zonaActual != null && !zonaActual.equals(nuevaZona)) {
                zonaActual.removerAtraccion(seleccionada);
                nuevaZona.agregarAtraccion(seleccionada);
            }
            tablaAtracciones.refresh();
            mostrarAlerta("Atracción actualizada", "La atracción fue actualizada correctamente.");
        });
    }

    @FXML
    private void onEliminarAtraccion() {
        Atraccion seleccionada = tablaAtracciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        if (!confirmar("Eliminar atracción", "¿Eliminar la atracción \"" + seleccionada.getNombre() + "\"?")) return;
        administrador.eliminarAtraccion(seleccionada);
        parque.getZonas().forEach(z -> z.removerAtraccion(seleccionada));
        listaAtracciones.remove(seleccionada);
    }

    private Optional<Zona> abrirDialogoZona(Zona existente) {
        Dialog<Zona> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Nueva Zona" : "Editar Zona");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        TextField tfNombre = new TextField(existente != null ? existente.getNombre() : "");
        tfNombre.setPromptText("Nombre de la zona");
        TextField tfCapacidad = new TextField(existente != null ? String.valueOf(existente.getCapacidadMaxima()) : "");
        tfCapacidad.setPromptText("Capacidad máxima");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(tfNombre, 1, 0);
        grid.add(new Label("Capacidad máxima:"), 0, 1);
        grid.add(tfCapacidad, 1, 1);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton != btnGuardar) return null;
            String nombre = tfNombre.getText().trim();
            String capStr = tfCapacidad.getText().trim();
            if (nombre.isEmpty() || capStr.isEmpty()) return null;
            try {
                int capacidad = Integer.parseInt(capStr);
                if (existente != null) {
                    existente.setNombre(nombre);
                    existente.setCapacidadMaxima(capacidad);
                    return existente;
                }
                return new Zona(UUID.randomUUID().toString(), nombre, capacidad);
            } catch (NumberFormatException e) {
                return null;
            }
        });

        return dialogo.showAndWait();
    }

    private Optional<Object[]> abrirDialogoAtraccion(Atraccion existente, Zona zonaActual) {
        Dialog<Object[]> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Nueva Atracción" : "Editar Atracción");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        TextField tfNombre = new TextField(existente != null ? existente.getNombre() : "");
        tfNombre.setPromptText("Nombre");
        TextField tfCapacidad = new TextField(existente != null ? String.valueOf(existente.getCapacidadMaxima()) : "");
        tfCapacidad.setPromptText("Capacidad máxima");
        TextField tfAlturaMin = new TextField(existente != null ? String.valueOf(existente.getAlturaMinima()) : "0.0");
        tfAlturaMin.setPromptText("Altura mínima (m)");
        TextField tfEdadMin = new TextField(existente != null ? String.valueOf(existente.getEdadMinima()) : "0");
        tfEdadMin.setPromptText("Edad mínima");
        TextField tfCosto = new TextField(existente != null ? String.valueOf(existente.getCostoAdicional()) : "0.0");
        tfCosto.setPromptText("Costo adicional");

        ChoiceBox<TipoAtraccion> choiceTipo = new ChoiceBox<>(FXCollections.observableArrayList(TipoAtraccion.values()));
        choiceTipo.setValue(existente != null ? existente.getTipoAtraccion() : TipoAtraccion.MECANICA);

        ChoiceBox<Zona> choiceZona = new ChoiceBox<>(FXCollections.observableArrayList(parque.getZonas()));
        choiceZona.setValue(zonaActual != null ? zonaActual : parque.getZonas().get(0));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Nombre:"), 0, 0);        grid.add(tfNombre, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1);           grid.add(choiceTipo, 1, 1);
        grid.add(new Label("Zona:"), 0, 2);           grid.add(choiceZona, 1, 2);
        grid.add(new Label("Capacidad:"), 0, 3);      grid.add(tfCapacidad, 1, 3);
        grid.add(new Label("Altura mínima (m):"), 0, 4); grid.add(tfAlturaMin, 1, 4);
        grid.add(new Label("Edad mínima:"), 0, 5);    grid.add(tfEdadMin, 1, 5);
        grid.add(new Label("Costo adicional:"), 0, 6); grid.add(tfCosto, 1, 6);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton != btnGuardar) return null;
            try {
                String nombre = tfNombre.getText().trim();
                int cap = Integer.parseInt(tfCapacidad.getText().trim());
                double altura = Double.parseDouble(tfAlturaMin.getText().trim());
                int edad = Integer.parseInt(tfEdadMin.getText().trim());
                double costo = Double.parseDouble(tfCosto.getText().trim());
                TipoAtraccion tipo = choiceTipo.getValue();
                Zona zona = choiceZona.getValue();
                if (nombre.isEmpty() || zona == null) return null;
                Atraccion a = existente != null ? existente : new Atraccion(UUID.randomUUID().toString(), nombre, tipo, cap);
                if (existente != null) {
                    existente.setNombre(nombre);
                    existente.setTipoAtraccion(tipo);
                    existente.setCapacidadMaxima(cap);
                }
                a.setAlturaMinima(altura);
                a.setEdadMinima(edad);
                a.setCostoAdicional(costo);
                return new Object[]{a, zona};
            } catch (NumberFormatException e) {
                return null;
            }
        });

        return dialogo.showAndWait();
    }

    private boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    @FXML
    private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }
}