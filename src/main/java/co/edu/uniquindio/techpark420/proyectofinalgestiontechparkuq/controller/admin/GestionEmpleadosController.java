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
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Operador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class GestionEmpleadosController extends BaseController implements Initializable {

    @FXML private TableView<Empleado>           empleadosTable;
    @FXML private TableColumn<Empleado, String> colDocumento;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colRol;
    @FXML private TableColumn<Empleado, String> colZonaAsignada;
    @FXML private TableColumn<Empleado, String> colCorreo;
    @FXML private TableColumn<Empleado, String> colEstadoEmpleado;

    @FXML private TextField         buscarField;
    @FXML private ChoiceBox<String> filtroRolChoice;
    @FXML private Label             mensajeLabel;

    @FXML private Button editarButton;
    @FXML private Button asignarZonaButton;
    @FXML private Button desvincularButton;

    @FXML private Label totalEmpleadosLabel;
    @FXML private Label totalOperadoresLabel;
    @FXML private Label totalAdminsLabel;

    private ObservableList<Empleado> listaEmpleados;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarFiltroRol();
        configurarColumnas();
        cargarEmpleados();
        configurarSeleccion();
    }

    private void configurarFiltroRol() {
        filtroRolChoice.setItems(FXCollections.observableArrayList("Todos", "Operador", "Administrador"));
        filtroRolChoice.setValue("Todos");
        filtroRolChoice.getSelectionModel().selectedItemProperty().addListener((obs, ant, nuevo) -> filtrarEmpleados());
    }

    private void configurarColumnas() {
        colDocumento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDocumento()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colRol.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue() instanceof Administrador ? "Administrador" : "Operador"));
        colZonaAsignada.setCellValueFactory(d -> {
            if (d.getValue() instanceof Operador op && op.getZonaAsignada() != null) {
                return new SimpleStringProperty(op.getZonaAsignada().getNombre());
            }
            return new SimpleStringProperty("—");
        });
        colCorreo.setCellValueFactory(d -> new SimpleStringProperty("—"));
        colEstadoEmpleado.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().isActivo() ? "Activo" : "Inactivo"));
    }

    private void cargarEmpleados() {
        List<Empleado> empleados = AppContext.getInstance().getParque().getEmpleados();
        listaEmpleados = FXCollections.observableArrayList(empleados);
        empleadosTable.setItems(listaEmpleados);
        actualizarContadores();
    }

    private void actualizarContadores() {
        List<Empleado> todos = AppContext.getInstance().getParque().getEmpleados();
        long operadores = todos.stream().filter(e -> e instanceof Operador).count();
        long admins     = todos.stream().filter(e -> e instanceof Administrador).count();

        totalEmpleadosLabel.setText(String.valueOf(todos.size()));
        totalOperadoresLabel.setText(String.valueOf(operadores));
        totalAdminsLabel.setText(String.valueOf(admins));
    }

    private void filtrarEmpleados() {
        String texto = buscarField.getText().trim().toLowerCase();
        String rol   = filtroRolChoice.getValue();

        List<Empleado> filtrados = AppContext.getInstance().getParque().getEmpleados().stream()
                .filter(e -> {
                    boolean coincideTexto = texto.isEmpty()
                            || e.getNombre().toLowerCase().contains(texto)
                            || e.getDocumento().toLowerCase().contains(texto);
                    boolean coincideRol = "Todos".equals(rol)
                            || ("Administrador".equals(rol) && e instanceof Administrador)
                            || ("Operador".equals(rol) && e instanceof Operador);
                    return coincideTexto && coincideRol;
                })
                .toList();
        empleadosTable.setItems(FXCollections.observableArrayList(filtrados));
    }

    private void configurarSeleccion() {
        editarButton.setDisable(true);
        asignarZonaButton.setDisable(true);
        desvincularButton.setDisable(true);

        empleadosTable.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            boolean hay = sel != null;
            editarButton.setDisable(!hay);
            asignarZonaButton.setDisable(!hay || !(sel instanceof Operador));
            desvincularButton.setDisable(!hay || !sel.isActivo());
        });
    }

    @FXML private void buscarEmpleado() {
        filtrarEmpleados();
    }

    @FXML private void handleContratarEmpleado() {
        Dialog<Empleado> dialogo = construirDialogoEmpleado(null);
        dialogo.showAndWait().ifPresent(empleado -> {
            AppContext.getInstance().getParque().getEmpleados().add(empleado);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            cargarEmpleados();
            mostrarAlerta("Éxito", "Empleado contratado correctamente.");
        });
    }

    @FXML private void handleEditarEmpleado() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        construirDialogoEmpleado(seleccionado).showAndWait().ifPresent(actualizado -> {
            seleccionado.setNombre(actualizado.getNombre());
            seleccionado.setDocumento(actualizado.getDocumento());
            seleccionado.setSalario(actualizado.getSalario());
            if (seleccionado instanceof Operador op && actualizado instanceof Operador opAct) {
                op.setZonaAsignada(opAct.getZonaAsignada());
            }
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            empleadosTable.refresh();
            mostrarAlerta("Éxito", "Empleado actualizado correctamente.");
        });
    }

    @FXML private void handleAsignarZona() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (!(seleccionado instanceof Operador op)) return;

        List<Zona> zonas = AppContext.getInstance().getParque().getZonas();
        if (zonas.isEmpty()) { mostrarError("No hay zonas", "Crea una zona primero."); return; }

        Dialog<Zona> dialogo = new Dialog<>();
        dialogo.setTitle("Asignar Zona");
        dialogo.setHeaderText("Selecciona la zona para " + op.getNombre());

        ButtonType btnAsignar = new ButtonType("Asignar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnAsignar, ButtonType.CANCEL);

        ChoiceBox<Zona> choiceZona = new ChoiceBox<>(FXCollections.observableArrayList(zonas));
        choiceZona.setValue(zonas.get(0));
        choiceZona.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Zona z) { return z == null ? "" : z.getNombre(); }
            @Override public Zona fromString(String s) { return null; }
        });

        dialogo.getDialogPane().setContent(choiceZona);
        dialogo.setResultConverter(boton -> boton == btnAsignar ? choiceZona.getValue() : null);

        dialogo.showAndWait().ifPresent(zona -> {
            op.setZonaAsignada(zona);
            zona.agregarOperador(op);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            empleadosTable.refresh();
            mostrarAlerta("Zona asignada", op.getNombre() + " asignado a " + zona.getNombre());
        });
    }

    @FXML private void handleDesvincularEmpleado() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar desvinculación");
        confirm.setHeaderText("¿Desvincular a " + seleccionado.getNombre() + "?");
        confirm.setContentText("El empleado quedará como inactivo.");
        confirm.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> {
            seleccionado.setActivo(false);
            AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA
            empleadosTable.refresh();
            desvincularButton.setDisable(true);
            mostrarAlerta("Éxito", "Empleado desvinculado.");
        });
    }

    @FXML private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }

    private Dialog<Empleado> construirDialogoEmpleado(Empleado existente) {
        Dialog<Empleado> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Contratar Empleado" : "Editar Empleado");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField tfNombre    = new TextField(existente != null ? existente.getNombre() : "");
        TextField tfDocumento = new TextField(existente != null ? existente.getDocumento() : "");
        TextField tfSalario   = new TextField(existente != null ? String.valueOf(existente.getSalario()) : "");
        tfNombre.setPromptText("Nombre completo");
        tfDocumento.setPromptText("Documento");
        tfSalario.setPromptText("Salario");

        ChoiceBox<String> choiceRol = new ChoiceBox<>(FXCollections.observableArrayList("Operador", "Administrador"));
        choiceRol.setValue(existente instanceof Administrador ? "Administrador" : "Operador");

        ChoiceBox<Zona> choiceZona = new ChoiceBox<>(
                FXCollections.observableArrayList(AppContext.getInstance().getParque().getZonas()));
        choiceZona.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Zona z) { return z == null ? "" : z.getNombre(); }
            @Override public Zona fromString(String s) { return null; }
        });
        if (existente instanceof Operador op && op.getZonaAsignada() != null) {
            choiceZona.setValue(op.getZonaAsignada());
        }

        Label lblZona = new Label("Zona asignada:");
        boolean esAdmin = existente instanceof Administrador;
        choiceZona.setVisible(!esAdmin);
        lblZona.setVisible(!esAdmin);

        choiceRol.getSelectionModel().selectedItemProperty().addListener((obs, ant, nuevo) -> {
            boolean operador = "Operador".equals(nuevo);
            choiceZona.setVisible(operador);
            lblZona.setVisible(operador);
        });

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        grid.add(new Label("Nombre:"),    0, 0); grid.add(tfNombre,    1, 0);
        grid.add(new Label("Documento:"), 0, 1); grid.add(tfDocumento, 1, 1);
        grid.add(new Label("Salario:"),   0, 2); grid.add(tfSalario,   1, 2);
        grid.add(new Label("Rol:"),       0, 3); grid.add(choiceRol,   1, 3);
        grid.add(lblZona,                 0, 4); grid.add(choiceZona,  1, 4);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton != btnGuardar) return null;
            try {
                String nombre    = tfNombre.getText().trim();
                String documento = tfDocumento.getText().trim();
                double salario   = Double.parseDouble(tfSalario.getText().trim());
                if (nombre.isEmpty() || documento.isEmpty()) return null;

                if ("Administrador".equals(choiceRol.getValue())) {
                    Administrador admin = new Administrador();
                    admin.setId(existente != null ? existente.getId() : UUID.randomUUID().toString());
                    admin.setNombre(nombre);
                    admin.setDocumento(documento);
                    admin.setSalario(salario);
                    return admin;
                } else {
                    Operador op = new Operador();
                    op.setId(existente != null ? existente.getId() : UUID.randomUUID().toString());
                    op.setNombre(nombre);
                    op.setDocumento(documento);
                    op.setSalario(salario);
                    op.setZonaAsignada(choiceZona.getValue());
                    return op;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        });

        return dialogo;
    }
}