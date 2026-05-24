package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.auth;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Empleado;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.ColaVirtual;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Operador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController extends BaseController implements Initializable {

    @FXML private TextField txtDocumento;
    @FXML private Label lblError;

    @FXML private VBox panelRegistro;

    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumentoRegistro;
    @FXML private TextField txtEdad;
    @FXML private TextField txtEstatura;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblError.setVisible(false);
        panelRegistro.setVisible(false);
        panelRegistro.setManaged(false);

        cargarDatosDemo();
    }

    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String documento = txtDocumento.getText().trim();

        if (documento.isEmpty()) {
            mostrarMensajeError("Ingresa tu número de documento.");
            return;
        }

        Parque parque = getParque();

        Empleado empleado = buscarEmpleadoPorDocumento(parque, documento);
        if (empleado != null) {
            autenticarEmpleado(empleado);
            return;
        }

        Visitante visitante = buscarVisitantePorDocumento(parque, documento);
        if (visitante != null) {
            autenticarVisitante(visitante);
            return;
        }

        mostrarMensajeError("Documento no encontrado. Verifica el número o regístrate.");
    }

    @FXML
    private void onMostrarRegistro(ActionEvent event) {
        boolean visible = !panelRegistro.isVisible();
        panelRegistro.setVisible(visible);
        panelRegistro.setManaged(visible);
        lblError.setVisible(false);
    }

    @FXML
    private void onRegistrarVisitante(ActionEvent event) {
        String nombre      = txtNombre.getText().trim();
        String documento   = txtDocumentoRegistro.getText().trim();
        String edadStr     = txtEdad.getText().trim();
        String estaturaStr = txtEstatura.getText().trim();

        if (nombre.isEmpty() || documento.isEmpty() || edadStr.isEmpty() || estaturaStr.isEmpty()) {
            mostrarMensajeError("Todos los campos del registro son obligatorios.");
            return;
        }

        int edad;
        double estatura;
        try {
            edad     = Integer.parseInt(edadStr);
            estatura = Double.parseDouble(estaturaStr);
        } catch (NumberFormatException e) {
            mostrarMensajeError("Edad debe ser un número entero y estatura un número decimal.");
            return;
        }

        if (edad <= 0 || estatura <= 0) {
            mostrarMensajeError("Edad y estatura deben ser valores positivos.");
            return;
        }

        if (buscarVisitantePorDocumento(getParque(), documento) != null) {
            mostrarMensajeError("Ya existe un visitante con ese documento.");
            return;
        }

        String idGenerado = "V-" + System.currentTimeMillis();
        Visitante nuevoVisitante = new Visitante(idGenerado, nombre, documento, edad, estatura);
        getParque().registrarVisitante(nuevoVisitante);
        AppContext.getInstance().guardarDatos(); // ← PERSISTENCIA

        autenticarVisitante(nuevoVisitante);
    }

    private void autenticarEmpleado(Empleado empleado) {
        AppContext.getInstance().iniciarSesionEmpleado(empleado);

        if (empleado instanceof Administrador) {
            navegarA(VISTA_DASHBOARD_ADMIN);
        } else if (empleado instanceof Operador) {
            navegarA(VISTA_DASHBOARD_OPERADOR);
        } else {
            mostrarError("Rol desconocido",
                    "El tipo de empleado no tiene un dashboard asignado.");
        }
    }

    private void autenticarVisitante(Visitante visitante) {
        AppContext.getInstance().iniciarSesionVisitante(visitante);
        navegarA(VISTA_DASHBOARD_VISITANTE);
    }

    private Empleado buscarEmpleadoPorDocumento(Parque parque, String documento) {
        for (Empleado e : parque.getEmpleados()) {
            if (e.getDocumento().equalsIgnoreCase(documento)) {
                return e;
            }
        }
        return null;
    }

    private Visitante buscarVisitantePorDocumento(Parque parque, String documento) {
        for (Visitante v : parque.getVisitantes()) {
            if (v.getDocumento().equalsIgnoreCase(documento)) {
                return v;
            }
        }
        return null;
    }

    private void mostrarMensajeError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void cargarDatosDemo() {
        Parque parque = getParque();

        if (!parque.getEmpleados().isEmpty()) return;

        Administrador admin = new Administrador("Jefe de Operaciones", 0);
        admin.setId("EMP-001");
        admin.setNombre("Carlos Admin");
        admin.setDocumento("4242");
        admin.setSalario(5000000);
        parque.getEmpleados().add(admin);

        Operador operador = new Operador();
        operador.setId("EMP-002");
        operador.setNombre("Pepe");
        operador.setDocumento("1010");
        operador.setSalario(2000000);
        parque.getEmpleados().add(operador);



        Visitante visitante1 = new Visitante("V-001", "Andres Camilo Carvajal Figueroa", "1066602456", 19, 1.52);
        visitante1.recargarSaldo(50000);
        parque.registrarVisitante(visitante1);

        Zona zona1 = new Zona("Z-001", "Zona Extrema", 100);
        Zona zona2 = new Zona("Z-002", "Zona Acuática", 80);
        Zona zona3 = new Zona("Z-003", "Zona Infantil", 60);

        Atraccion a1 = new Atraccion("A-001", "Montaña Rusa", TipoAtraccion.EXTREMA, 30);
        a1.setAlturaMinima(1.40); a1.setEdadMinima(12);
        a1.setCostoAdicional(5000); a1.setTiempoEspera(1);
        a1.setColaVirtual(new ColaVirtual("cola-A-001", 30, a1));

        Atraccion a2 = new Atraccion("A-002", "Tobogán Gigante", TipoAtraccion.ACUATICA, 20);
        a2.setAlturaMinima(1.20); a2.setEdadMinima(8);
        a2.setCostoAdicional(3000); a2.setTiempoEspera(10);
        a2.setColaVirtual(new ColaVirtual("cola-A-002", 20, a2));

        Atraccion a3 = new Atraccion("A-003", "Torre del Terror", TipoAtraccion.EXTREMA, 15);
        a3.setAlturaMinima(1.50); a3.setEdadMinima(14);
        a3.setCostoAdicional(7000); a3.setTiempoEspera(25);
        a3.setColaVirtual(new ColaVirtual("cola-A-003", 15, a3));

        Atraccion a4 = new Atraccion("A-004", "Carrusel Mágico", TipoAtraccion.INFANTIL, 25);
        a4.setAlturaMinima(0.80); a4.setEdadMinima(3);
        a4.setCostoAdicional(0); a4.setTiempoEspera(5);
        a4.setColaVirtual(new ColaVirtual("cola-A-004", 25, a4));

        Atraccion a5 = new Atraccion("A-005", "Casa del Terror", TipoAtraccion.FAMILIAR, 20);
        a5.setAlturaMinima(1.10); a5.setEdadMinima(10);
        a5.setCostoAdicional(4000);
        a5.setEstado(EstadoAtraccion.EN_MANTENIMIENTO);
        a5.setMotivoCierre("Revisión técnica programada");
        a5.setColaVirtual(new ColaVirtual("cola-A-005", 20, a5));

        zona1.agregarAtraccion(a1); zona1.agregarAtraccion(a3);
        zona2.agregarAtraccion(a2);
        zona3.agregarAtraccion(a4); zona3.agregarAtraccion(a5);

        zona1.agregarOperador(operador);

        parque.agregarZona(zona1);
        parque.agregarZona(zona2);
        parque.agregarZona(zona3);

        AppContext.getInstance().guardarDatos(); 
    }
}