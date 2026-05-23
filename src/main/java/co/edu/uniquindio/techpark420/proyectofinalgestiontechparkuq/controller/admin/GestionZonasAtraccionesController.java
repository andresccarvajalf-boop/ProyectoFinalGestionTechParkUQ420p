package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
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

public class GestionZonasAtraccionesController extends BaseController implements Initializable {

    @FXML private TableView<Zona>           zonasTable;
    @FXML private TableColumn<Zona, String> colZonaNombre;
    @FXML private TableColumn<Zona, String> colZonaDescripcion;
    @FXML private TableColumn<Zona, String> colZonaAtracciones;
    @FXML private TableColumn<Zona, String> colZonaOperador;
    @FXML private TextField                 buscarZonaField;
    @FXML private Button                    editarZonaButton;
    @FXML private Button                    eliminarZonaButton;

    @FXML private TableView<Atraccion>           atraccionesTable;
    @FXML private TableColumn<Atraccion, String> colAtraccionNombre;
    @FXML private TableColumn<Atraccion, String> colAtraccionZona;
    @FXML private TableColumn<Atraccion, String> colAtraccionEstado;
    @FXML private TableColumn<Atraccion, String> colAtraccionCapacidad;
    @FXML private TableColumn<Atraccion, String> colAtraccionAltura;
    @FXML private TableColumn<Atraccion, String> colAtraccionEdad;
    @FXML private TextField                      buscarAtraccionField;
    @FXML private Button                         editarAtraccionButton;
    @FXML private Button                         eliminarAtraccionButton;

    private ObservableList<Zona>      listaZonas;
    private ObservableList<Atraccion> listaAtracciones;
    private Administrador administrador;
    private Parque        parque;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        administrador = AppContext.getInstance().getAdministradorActivo();
        parque        = AppContext.getInstance().getParque();
        configurarColumnasZonas();
        configurarColumnasAtracciones();
        cargarZonas();
        cargarTodasLasAtracciones();
        configurarSeleccionZonas();
        configurarSeleccionAtracciones();
    }

    private void configurarColumnasZonas() {
        colZonaNombre.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getNombre()));
        colZonaDescripcion.setCellValueFactory(d ->
                new SimpleStringProperty("Cap: " + d.getValue().getCapacidadMaxima()));
        colZonaAtracciones.setCellValueFactory(d ->
                new SimpleStringProperty(String.valueOf(d.getValue().getAtracciones().size())));
        colZonaOperador.setCellValueFactory(d -> {
            String op = d.getValue().getOperadores().isEmpty()
                    ? "Sin operador"
                    : d.getValue().getOperadores().get(0).getNombre();
            return new SimpleStringProperty(op);
        });
    }

    private void configurarColumnasAtracciones() {
        colAtraccionNombre.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getNombre()));
        colAtraccionZona.setCellValueFactory(d -> {
            String zona = parque.getZonas().stream()
                    .filter(z -> z.getAtracciones().contains(d.getValue()))
                    .map(Zona::getNombre).findFirst().orElse("Sin zona");
            return new SimpleStringProperty(zona);
        });
        colAtraccionEstado.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEstado() != null
                        ? d.getValue().getEstado().name() : ""));
        colAtraccionCapacidad.setCellValueFactory(d ->
                new SimpleStringProperty(String.valueOf(d.getValue().getCapacidadMaxima())));
        colAtraccionAltura.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getAlturaMinima() + " m"));
        colAtraccionEdad.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEdadMinima() + " años"));
    }

    private void cargarZonas() {
        if (parque == null) return;
        listaZonas = FXCollections.observableArrayList(parque.getZonas());
        zonasTable.setItems(listaZonas);
    }

    private void cargarTodasLasAtracciones() {
        if (parque == null) return;
        List<Atraccion> todas = parque.obtenerAtraccionesDisponibles();
        listaAtracciones = FXCollections.observableArrayList(todas);
        atraccionesTable.setItems(listaAtracciones);
    }

    private void configurarSeleccionZonas() {
        zonasTable.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean vacio = sel == null;
            editarZonaButton.setDisable(vacio);
            eliminarZonaButton.setDisable(vacio);
        });
        editarZonaButton.setDisable(true);
        eliminarZonaButton.setDisable(true);
    }

    private void configurarSeleccionAtracciones() {
        atraccionesTable.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean vacio = sel == null;
            editarAtraccionButton.setDisable(vacio);
            eliminarAtraccionButton.setDisable(vacio);
        });
        editarAtraccionButton.setDisable(true);
        eliminarAtraccionButton.setDisable(true);
    }

    @FXML private void buscarZona() {
        String texto = buscarZonaField.getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarZonas(); return; }
        List<Zona> filtradas = parque.getZonas().stream()
                .filter(z -> z.getNombre().toLowerCase().contains(texto))
                .toList();
        zonasTable.setItems(FXCollections.observableArrayList(filtradas));
    }

    @FXML private void handleNuevaZona() {
        abrirDialogoZona(null).ifPresent(zona -> {
            parque.agregarZona(zona);
            listaZonas.add(zona);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            mostrarAlerta("Zona creada", "La zona \"" + zona.getNombre() + "\" fue creada.");
        });
    }

    @FXML private void handleEditarZona() {
        Zona seleccionada = zonasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        abrirDialogoZona(seleccionada).ifPresent(z -> {
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            zonasTable.refresh();
            mostrarAlerta("Zona actualizada", "La zona fue actualizada.");
        });
    }

    @FXML private void handleEliminarZona() {
        Zona seleccionada = zonasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        if (!confirmar("Eliminar zona", "¿Eliminar \"" + seleccionada.getNombre() + "\"?")) return;
        parque.removerZona(seleccionada);
        listaZonas.remove(seleccionada);
        AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
        cargarTodasLasAtracciones();
    }

    @FXML private void buscarAtraccion() {
        String texto = buscarAtraccionField.getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarTodasLasAtracciones(); return; }
        List<Atraccion> filtradas = parque.getZonas().stream()
                .flatMap(z -> z.getAtracciones().stream())
                .filter(a -> a.getNombre().toLowerCase().contains(texto))
                .toList();
        atraccionesTable.setItems(FXCollections.observableArrayList(filtradas));
    }

    @FXML private void handleNuevaAtraccion() {
        if (parque.getZonas().isEmpty()) {
            mostrarError("Sin zonas", "Crea al menos una zona antes de agregar atracciones.");
            return;
        }
        abrirDialogoAtraccion(null, null).ifPresent(datos -> {
            Atraccion a = (Atraccion) datos[0];
            Zona zona   = (Zona)      datos[1];
            zona.agregarAtraccion(a);
            listaAtracciones.add(a);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            atraccionesTable.refresh();
            mostrarAlerta("Atracción creada", "\"" + a.getNombre() + "\" agregada a \"" + zona.getNombre() + "\".");
        });
    }

    @FXML private void handleEditarAtraccion() {
        Atraccion seleccionada = atraccionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Zona zonaActual = parque.getZonas().stream()
                .filter(z -> z.getAtracciones().contains(seleccionada))
                .findFirst().orElse(null);
        abrirDialogoAtraccion(seleccionada, zonaActual).ifPresent(datos -> {
            Zona nuevaZona = (Zona) datos[1];
            if (zonaActual != null && !zonaActual.equals(nuevaZona)) {
                zonaActual.removerAtraccion(seleccionada);
                nuevaZona.agregarAtraccion(seleccionada);
            }
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            atraccionesTable.refresh();
            mostrarAlerta("Atracción actualizada", "La atracción fue actualizada.");
        });
    }

    @FXML private void handleEliminarAtraccion() {
        Atraccion seleccionada = atraccionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        if (!confirmar("Eliminar atracción", "¿Eliminar \"" + seleccionada.getNombre() + "\"?")) return;
        parque.getZonas().forEach(z -> z.removerAtraccion(seleccionada));
        listaAtracciones.remove(seleccionada);
        AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
    }

    @FXML private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }

    private Optional<Zona> abrirDialogoZona(Zona existente) {
        Dialog<Zona> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Nueva Zona" : "Editar Zona");
        ButtonType btnGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        TextField tfNombre    = new TextField(existente != null ? existente.getNombre() : "");
        TextField tfCapacidad = new TextField(existente != null ? String.valueOf(existente.getCapacidadMaxima()) : "");
        tfNombre.setPromptText("Nombre de la zona");
        tfCapacidad.setPromptText("Capacidad máxima");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Nombre:"),           0, 0); grid.add(tfNombre,    1, 0);
        grid.add(new Label("Capacidad máxima:"), 0, 1); grid.add(tfCapacidad, 1, 1);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton != btnGuardar) return null;
            String nombre = tfNombre.getText().trim();
            String capStr = tfCapacidad.getText().trim();
            if (nombre.isEmpty() || capStr.isEmpty()) return null;
            try {
                int cap = Integer.parseInt(capStr);
                if (existente != null) {
                    existente.setNombre(nombre);
                    existente.setCapacidadMaxima(cap);
                    return existente;
                }
                return new Zona(UUID.randomUUID().toString(), nombre, cap);
            } catch (NumberFormatException e) { return null; }
        });
        return dialogo.showAndWait();
    }

    private Optional<Object[]> abrirDialogoAtraccion(Atraccion existente, Zona zonaActual) {
        Dialog<Object[]> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Nueva Atracción" : "Editar Atracción");
        ButtonType btnGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        TextField tfNombre    = new TextField(existente != null ? existente.getNombre() : "");
        TextField tfCapacidad = new TextField(existente != null ? String.valueOf(existente.getCapacidadMaxima()) : "");
        TextField tfAltura    = new TextField(existente != null ? String.valueOf(existente.getAlturaMinima()) : "0.0");
        TextField tfEdad      = new TextField(existente != null ? String.valueOf(existente.getEdadMinima()) : "0");
        TextField tfCosto     = new TextField(existente != null ? String.valueOf(existente.getCostoAdicional()) : "0.0");

        ChoiceBox<TipoAtraccion> choiceTipo = new ChoiceBox<>(FXCollections.observableArrayList(TipoAtraccion.values()));
        choiceTipo.setValue(existente != null ? existente.getTipoAtraccion() : TipoAtraccion.values()[0]);

        ChoiceBox<Zona> choiceZona = new ChoiceBox<>(FXCollections.observableArrayList(parque.getZonas()));
        choiceZona.setValue(zonaActual != null ? zonaActual : parque.getZonas().get(0));
        choiceZona.setConverter(new javafx.util.StringConverter<Zona>() {
            @Override public String toString(Zona zona) { return zona != null ? zona.getNombre() : ""; }
            @Override public Zona fromString(String string) { return null; }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(10));
        grid.add(new Label("Nombre:"),            0, 0); grid.add(tfNombre,    1, 0);
        grid.add(new Label("Tipo:"),              0, 1); grid.add(choiceTipo,  1, 1);
        grid.add(new Label("Zona:"),              0, 2); grid.add(choiceZona,  1, 2);
        grid.add(new Label("Capacidad:"),         0, 3); grid.add(tfCapacidad, 1, 3);
        grid.add(new Label("Altura mínima (m):"), 0, 4); grid.add(tfAltura,    1, 4);
        grid.add(new Label("Edad mínima:"),       0, 5); grid.add(tfEdad,      1, 5);
        grid.add(new Label("Costo adicional:"),   0, 6); grid.add(tfCosto,     1, 6);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton != btnGuardar) return null;
            try {
                String nombre = tfNombre.getText().trim();
                int    cap    = Integer.parseInt(tfCapacidad.getText().trim());
                double altura = Double.parseDouble(tfAltura.getText().trim());
                int    edad   = Integer.parseInt(tfEdad.getText().trim());
                double costo  = Double.parseDouble(tfCosto.getText().trim());
                TipoAtraccion tipo = choiceTipo.getValue();
                Zona zona = choiceZona.getValue();
                if (nombre.isEmpty() || zona == null) return null;
                Atraccion a = existente != null ? existente
                        : new Atraccion(UUID.randomUUID().toString(), nombre, tipo, cap);
                if (existente != null) {
                    existente.setNombre(nombre);
                    existente.setTipoAtraccion(tipo);
                    existente.setCapacidadMaxima(cap);
                }
                a.setAlturaMinima(altura);
                a.setEdadMinima(edad);
                a.setCostoAdicional(costo);
                return new Object[]{a, zona};
            } catch (NumberFormatException e) { return null; }
        });
        return dialogo.showAndWait();
    }

    private boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
}