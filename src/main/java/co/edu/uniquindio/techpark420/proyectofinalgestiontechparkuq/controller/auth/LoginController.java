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

/**
 * Controlador para la vista de Login (login-view.fxml).
 *
 * Responsabilidades:
 *  - Validar que los campos no estén vacíos.
 *  - Buscar el usuario por documento en las listas del Parque.
 *  - Determinar el rol (Administrador / Operador / Visitante).
 *  - Registrar la sesión en AppContext.
 *  - Redirigir al dashboard correspondiente.
 *  - Ofrecer un formulario rápido de registro para nuevos Visitantes.
 *
 * Estrategia de autenticación:
 *   El campo "documento" actúa como identificador único.
 *   En este proyecto académico no hay contraseñas; la autenticación
 *   se basa en encontrar el documento en la lista de empleados o visitantes.
 *   Si se desea agregar contraseñas, basta con añadir el campo a Empleado/Visitante
 *   y compararlo aquí sin modificar el resto de la arquitectura.
 */
public class LoginController extends BaseController implements Initializable {

    // ─────────────────────────────────────────────
    // Componentes FXML
    // ─────────────────────────────────────────────

    @FXML private TextField txtDocumento;
    @FXML private Label lblError;

    /** Sección de registro rápido de visitante (visible solo al hacer clic en "Registrarse") */
    @FXML private VBox panelRegistro;

    // Campos del formulario de registro de visitante
    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumentoRegistro;
    @FXML private TextField txtEdad;
    @FXML private TextField txtEstatura;

    // ─────────────────────────────────────────────
    // Inicialización
    // ─────────────────────────────────────────────

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    lblError.setVisible(false);
    panelRegistro.setVisible(false);
    panelRegistro.setManaged(false);

    cargarDatosDemo(); // crea el operador y lo agrega al parque

    // Obtener el operador ya registrado en el parque
    Operador operadorDemo = null;
    for (Empleado e : parque.getEmpleados()) {
        if (e instanceof Operador) {
            operadorDemo = (Operador) e;
            break;
        }
    }

    Visitante visitante2 = new Visitante("V-002", "Ana Visitante", "1066602456", 22, 1.65);
    visitante2.recargarSaldo(50000);
    parque.registrarVisitante(visitante2);

    Zona zona1 = new Zona("Z-001", "Zona Extrema", 100);
    Zona zona2 = new Zona("Z-002", "Zona Acuática", 80);
    Zona zona3 = new Zona("Z-003", "Zona Infantil", 60);

    Atraccion a1 = new Atraccion("A-001", "Montaña Rusa", TipoAtraccion.EXTREMA, 30);
    a1.setAlturaMinima(1.40); a1.setEdadMinima(12);
    a1.setCostoAdicional(5000); a1.setTiempoEspera(1);
    a1.setColaVirtual(new ColaVirtual("cola-A-001", 30, a1)); // ← AGREGAR
    
    Atraccion a2 = new Atraccion("A-002", "Tobogán Gigante", TipoAtraccion.ACUATICA, 20);
    a2.setAlturaMinima(1.20); a2.setEdadMinima(8);
    a2.setCostoAdicional(3000); a2.setTiempoEspera(10);
    a2.setColaVirtual(new ColaVirtual("cola-A-002", 20, a2)); // ← AGREGAR
    
    Atraccion a3 = new Atraccion("A-003", "Torre del Terror", TipoAtraccion.EXTREMA, 15);
    a3.setAlturaMinima(1.50); a3.setEdadMinima(14);
    a3.setCostoAdicional(7000); a3.setTiempoEspera(25);
    a3.setColaVirtual(new ColaVirtual("cola-A-003", 15, a3)); // ← AGREGAR
    
    Atraccion a4 = new Atraccion("A-004", "Carrusel Mágico", TipoAtraccion.INFANTIL, 25);
    a4.setAlturaMinima(0.80); a4.setEdadMinima(3);
    a4.setCostoAdicional(0); a4.setTiempoEspera(5);
    a4.setColaVirtual(new ColaVirtual("cola-A-004", 25, a4)); // ← AGREGAR
    
    Atraccion a5 = new Atraccion("A-005", "Casa del Terror", TipoAtraccion.FAMILIAR, 20);
    a5.setAlturaMinima(1.10); a5.setEdadMinima(10);
    a5.setCostoAdicional(4000);
    a5.setEstado(EstadoAtraccion.EN_MANTENIMIENTO);
    a5.setMotivoCierre("Revisión técnica programada");
    a5.setColaVirtual(new ColaVirtual("cola-A-005", 20, a5)); // ← AGREGAR (aunque esté cerrada, no hace daño)

    zona1.agregarAtraccion(a1); zona1.agregarAtraccion(a3);
    zona2.agregarAtraccion(a2);
    zona3.agregarAtraccion(a4); zona3.agregarAtraccion(a5);

    if (operadorDemo != null) zona1.agregarOperador(operadorDemo);

    parque.agregarZona(zona1);
    parque.agregarZona(zona2);
    parque.agregarZona(zona3);
}


    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String documento = txtDocumento.getText().trim();

        if (documento.isEmpty()) {
            mostrarMensajeError("Ingresa tu número de documento.");
            return;
        }

        Parque parque = getParque();

        // 1. Buscar entre empleados
        Empleado empleado = buscarEmpleadoPorDocumento(parque, documento);
        if (empleado != null) {
            autenticarEmpleado(empleado);
            return;
        }

        // 2. Buscar entre visitantes
        Visitante visitante = buscarVisitantePorDocumento(parque, documento);
        if (visitante != null) {
            autenticarVisitante(visitante);
            return;
        }

        // 3. No encontrado
        mostrarMensajeError("Documento no encontrado. Verifica el número o regístrate.");
    }

    /**
     * Muestra u oculta el panel de registro de visitante.
     */
    @FXML
    private void onMostrarRegistro(ActionEvent event) {
        boolean visible = !panelRegistro.isVisible();
        panelRegistro.setVisible(visible);
        panelRegistro.setManaged(visible);
        lblError.setVisible(false);
    }

    /**
     * Registra un nuevo visitante con los datos del formulario de registro.
     * Tras el registro exitoso, inicia sesión automáticamente y navega al dashboard.
     */
    @FXML
    private void onRegistrarVisitante(ActionEvent event) {
        String nombre     = txtNombre.getText().trim();
        String documento  = txtDocumentoRegistro.getText().trim();
        String edadStr    = txtEdad.getText().trim();
        String estaturaStr = txtEstatura.getText().trim();

        // Validación de campos vacíos
        if (nombre.isEmpty() || documento.isEmpty() || edadStr.isEmpty() || estaturaStr.isEmpty()) {
            mostrarMensajeError("Todos los campos del registro son obligatorios.");
            return;
        }

        // Validación de tipos numéricos
        int edad;
        double estatura;
        try {
            edad     = Integer.parseInt(edadStr);
            estatura = Double.parseDouble(estaturaStr);
        } catch (NumberFormatException e) {
            mostrarMensajeError("Edad debe ser un número entero y estatura un número decimal.");
            return;
        }

        // Validación de negativos
        if (edad <= 0 || estatura <= 0) {
            mostrarMensajeError("Edad y estatura deben ser valores positivos.");
            return;
        }

        // Verificar que el documento no esté ya registrado
        if (buscarVisitantePorDocumento(getParque(), documento) != null) {
            mostrarMensajeError("Ya existe un visitante con ese documento.");
            return;
        }

        // Crear e registrar visitante
        String idGenerado = "V-" + System.currentTimeMillis();
        Visitante nuevoVisitante = new Visitante(idGenerado, nombre, documento, edad, estatura);
        getParque().registrarVisitante(nuevoVisitante);

        // Iniciar sesión directamente
        autenticarVisitante(nuevoVisitante);
    }

    // ─────────────────────────────────────────────
    // Métodos privados de apoyo
    // ─────────────────────────────────────────────

    /**
     * Registra la sesión del empleado en AppContext y redirige al dashboard correcto
     * según si es Administrador u Operador.
     */
    private void autenticarEmpleado(Empleado empleado) {
        AppContext.getInstance().iniciarSesionEmpleado(empleado);

        if (empleado instanceof Administrador) {
            navegarA(VISTA_DASHBOARD_ADMIN);
        } else if (empleado instanceof Operador) {
            navegarA(VISTA_DASHBOARD_OPERADOR);
        } else {
            // Tipo de empleado desconocido — no debería ocurrir con el modelo actual
            mostrarError("Rol desconocido",
                    "El tipo de empleado no tiene un dashboard asignado.");
        }
    }

    /**
     * Registra la sesión del visitante en AppContext y navega a su dashboard.
     */
    private void autenticarVisitante(Visitante visitante) {
        AppContext.getInstance().iniciarSesionVisitante(visitante);
        navegarA(VISTA_DASHBOARD_VISITANTE);
    }

    /**
     * Busca un empleado (Administrador u Operador) en la lista del parque
     * cuyo documento coincida con el dado.
     *
     * @return el Empleado encontrado, o null si no existe.
     */
    private Empleado buscarEmpleadoPorDocumento(Parque parque, String documento) {
        for (Empleado e : parque.getEmpleados()) {
            if (e.getDocumento().equalsIgnoreCase(documento)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Busca un visitante en la lista del parque cuyo documento coincida.
     *
     * @return el Visitante encontrado, o null si no existe.
     */
    private Visitante buscarVisitantePorDocumento(Parque parque, String documento) {
        for (Visitante v : parque.getVisitantes()) {
            if (v.getDocumento().equalsIgnoreCase(documento)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Muestra el mensaje de error debajo del campo de documento.
     */
    private void mostrarMensajeError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    /**
     * Carga datos de prueba en el parque para facilitar el desarrollo.
     * ELIMINAR antes de la entrega final.
     */
    private void cargarDatosDemo() {
        Parque parque = getParque();

        // Solo cargar si el parque está vacío (evitar duplicados en hot-reload)
        if (!parque.getEmpleados().isEmpty()) return;

        // Administrador demo
        Administrador admin = new Administrador("Jefe de Operaciones", 0);
        admin.setId("EMP-001");
        admin.setNombre("Carlos Admin");
        admin.setDocumento("1000000001");
        admin.setSalario(5000000);
        parque.getEmpleados().add(admin);

        // Operador demo
        Operador operador = new Operador();
        operador.setId("EMP-002");
        operador.setNombre("María Operadora");
        operador.setDocumento("1000000002");
        operador.setSalario(2000000);
        parque.getEmpleados().add(operador);

        // Visitante demo
        Visitante visitante = new Visitante("V-001", "Juan Visitante", "1000000003", 25, 1.75);
        parque.registrarVisitante(visitante);
    }
}