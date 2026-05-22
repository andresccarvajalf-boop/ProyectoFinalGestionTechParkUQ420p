package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.admin;

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
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GestionEmpleadosController extends BaseController {

    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, String> colId;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colDocumento;
    @FXML private TableColumn<Empleado, String> colSalario;
    @FXML private TableColumn<Empleado, String> colRol;
    @FXML private TableColumn<Empleado, String> colActivo;

    @FXML private TextField txtBuscar;
    @FXML private Button btnNuevoEmpleado;
    @FXML private Button btnEditar;
    @FXML private Button btnDesvincular;
    @FXML private Button btnVolver;

    private ObservableList<Empleado> listaEmpleados;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarEmpleados();
        configurarSeleccion();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colDocumento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDocumento()));
        colSalario.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("$%.0f", data.getValue().getSalario())));
        colRol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue() instanceof Administrador ? "Administrador" : "Operador"));
        colActivo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isActivo() ? "Activo" : "Inactivo"));
    }

    private void cargarEmpleados() {
        List<Empleado> empleados = AppContext.getInstance().getParque().getEmpleados();
        listaEmpleados = FXCollections.observableArrayList(empleados);
        tablaEmpleados.setItems(listaEmpleados);
    }

    private void configurarSeleccion() {
        btnEditar.setDisable(true);
        btnDesvincular.setDisable(true);
        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            boolean haySeleccion = actual != null;
            btnEditar.setDisable(!haySeleccion);
            btnDesvincular.setDisable(!haySeleccion || !actual.isActivo());
        });
    }

    @FXML
    private void onBuscar() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            cargarEmpleados();
            return;
        }
        List<Empleado> filtrados = AppContext.getInstance().getParque().getEmpleados().stream()
                .filter(e -> e.getNombre().toLowerCase().contains(filtro)
                        || e.getDocumento().toLowerCase().contains(filtro))
                .toList();
        tablaEmpleados.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void onNuevoEmpleado() {
        Dialog<Empleado> dialogo = construirDialogoEmpleado(null);
        Optional<Empleado> resultado = dialogo.showAndWait();
        resultado.ifPresent(empleado -> {
            AppContext.getInstance().getParque().getEmpleados().add(empleado);
            AppContext.getInstance().getAdminActivo().contratarEmpleado(empleado);
            cargarEmpleados();
            mostrarAlerta("Éxito", "Empleado registrado correctamente.");
        });
    }

    @FXML
    private void onEditar() {
        Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Dialog<Empleado> dialogo = construirDialogoEmpleado(seleccionado);
        Optional<Empleado> resultado = dialogo.showAndWait();
        resultado.ifPresent(empleadoActualizado -> {
            seleccionado.setNombre(empleadoActualizado.getNombre());
            seleccionado.setDocumento(empleadoActualizado.getDocumento());
            seleccionado.setSalario(empleadoActualizado.getSalario());
            if (seleccionado instanceof Operador op && empleadoActualizado instanceof Operador opActualizado) {
                op.setZonaAsignada(opActualizado.getZonaAsignada());
            }
            tablaEmpleados.refresh();
            mostrarAlerta("Éxito", "Empleado actualizado correctamente.");
        });
    }

    @FXML
    private void onDesvincular() {
        Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar desvinculación");
        confirmacion.setHeaderText("¿Desvincular a " + seleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción marcará al empleado como inactivo.");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            seleccionado.setActivo(false);
            AppContext.getInstance().getAdminActivo().desvincularEmpleado(seleccionado);
            tablaEmpleados.refresh();
            btnDesvincular.setDisable(true);
            mostrarAlerta("Éxito", "Empleado desvinculado.");
        }
    }

    @FXML
    private void onVolver() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/admin/dashboard-admin.fxml");
    }

    private Dialog<Empleado> construirDialogoEmpleado(Empleado existente) {
        Dialog<Empleado> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Nuevo Empleado" : "Editar Empleado");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");

        TextField txtDocumento = new TextField();
        txtDocumento.setPromptText("Documento");

        TextField txtSalario = new TextField();
        txtSalario.setPromptText("Salario");

        ChoiceBox<String> choiceRol = new ChoiceBox<>();
        choiceRol.getItems().addAll("Operador", "Administrador");
        choiceRol.setValue("Operador");

        ChoiceBox<Zona> choiceZona = new ChoiceBox<>();
        List<Zona> zonas = AppContext.getInstance().getParque().getZonas();
        choiceZona.getItems().addAll(zonas);
        choiceZona.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Zona z) { return z == null ? "" : z.getNombre(); }
            @Override public Zona fromString(String s) { return null; }
        });

        Label lblZona = new Label("Zona asignada:");

        choiceRol.getSelectionModel().selectedItemProperty().addListener((obs, ant, nuevo) -> {
            boolean esOperador = "Operador".equals(nuevo);
            choiceZona.setVisible(esOperador);
            lblZona.setVisible(esOperador);
        });

        if (existente != null) {
            txtNombre.setText(existente.getNombre());
            txtDocumento.setText(existente.getDocumento());
            txtSalario.setText(String.valueOf(existente.getSalario()));
            if (existente instanceof Administrador) {
                choiceRol.setValue("Administrador");
                choiceZona.setVisible(false);
                lblZona.setVisible(false);
            } else if (existente instanceof Operador op && op.getZonaAsignada() != null) {
                choiceZona.setValue(op.getZonaAsignada());
            }
        }

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        grid.add(new Label("Nombre:"), 0, 0);       grid.add(txtNombre, 1, 0);
        grid.add(new Label("Documento:"), 0, 1);    grid.add(txtDocumento, 1, 1);
        grid.add(new Label("Salario:"), 0, 2);      grid.add(txtSalario, 1, 2);
        grid.add(new Label("Rol:"), 0, 3);          grid.add(choiceRol, 1, 3);
        grid.add(lblZona, 0, 4);                    grid.add(choiceZona, 1, 4);

        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                try {
                    String nombre = txtNombre.getText().trim();
                    String documento = txtDocumento.getText().trim();
                    double salario = Double.parseDouble(txtSalario.getText().trim());
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
                    mostrarError("El salario debe ser un número válido.");
                    return null;
                }
            }
            return null;
        });

        return dialogo;
    }
}