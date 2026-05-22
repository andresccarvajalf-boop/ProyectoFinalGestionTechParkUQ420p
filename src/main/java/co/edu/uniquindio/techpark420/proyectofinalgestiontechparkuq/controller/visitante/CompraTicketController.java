package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.TicketFamiliar;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.TicketFastPass;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.TicketGeneral;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoTicket;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CompraTicketController extends BaseController implements Initializable {

    // ── Selección de tipo ────────────────────────────────────────────────────
    @FXML private ChoiceBox<String> choiceTipoTicket;

    // ── Panel de resumen dinámico ────────────────────────────────────────────
    @FXML private Label lblDescripcionTicket;
    @FXML private Label lblPrecioTicket;
    @FXML private Label lblSaldoActual;

    // ── Campos para Familiar ─────────────────────────────────────────────────
    @FXML private VBox panelFamiliar;
    @FXML private VBox panelAcompanantes;
    @FXML private Spinner<Integer> spinnerPersonas;

    // ── Estado interno ───────────────────────────────────────────────────────
    private Visitante visitante;

    // Cada fila de acompañante guarda sus 4 campos: nombre, edad, estatura, documento
    private final List<TextField[]> camposAcompanantes = new ArrayList<>();

    // ────────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visitante = appContext.getVisitanteEnSesion();

        choiceTipoTicket.getItems().addAll("GENERAL", "FAMILIAR", "FAST_PASS");
        choiceTipoTicket.setValue("GENERAL");

        spinnerPersonas.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 2));
        panelFamiliar.setVisible(false);
        panelFamiliar.setManaged(false);

        actualizarDetalle("GENERAL");

        choiceTipoTicket.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, nuevo) -> actualizarDetalle(nuevo));

        // Actualizar formulario cuando cambia el spinner con teclado también
        spinnerPersonas.valueProperty().addListener((obs, old, nuevo) -> {
            if ("FAMILIAR".equals(choiceTipoTicket.getValue())) {
                actualizarPrecioFamiliar();
                regenerarFormularioAcompanantes(nuevo - 1); // -1 porque el titular no cuenta
            }
        });

        lblSaldoActual.setText(
                String.format("Saldo disponible: $%.0f", visitante.getSaldoVirtual()));
    }

    // ── Actualiza la UI según el tipo elegido ────────────────────────────────
    private void actualizarDetalle(String tipo) {
        boolean esFamiliar = "FAMILIAR".equals(tipo);
        panelFamiliar.setVisible(esFamiliar);
        panelFamiliar.setManaged(esFamiliar);

        switch (tipo) {
            case "GENERAL" -> {
                lblDescripcionTicket.setText(
                        "Acceso básico al parque.\n" +
                        "Algunas atracciones requieren saldo adicional.");
                lblPrecioTicket.setText("Precio: $0 (acceso con saldo virtual propio)");
            }
            case "FAMILIAR" -> {
                lblDescripcionTicket.setText(
                        "Acceso para grupos familiares (mín. 2 personas).\n" +
                        "Descuento: 10% con 3+ personas | 20% con 5+ personas.");
                actualizarPrecioFamiliar();
                regenerarFormularioAcompanantes(spinnerPersonas.getValue() - 1);
            }
            case "FAST_PASS" -> {
                lblDescripcionTicket.setText(
                        "Prioridad en las colas virtuales.\n" +
                        "Reduce significativamente tu tiempo de espera.");
                lblPrecioTicket.setText("Precio: $25.000");
            }
            default -> lblDescripcionTicket.setText("");
        }
    }

    private void actualizarPrecioFamiliar() {
        if (spinnerPersonas == null) return;
        int personas = spinnerPersonas.getValue();
        double descuento = personas >= 5 ? 0.20 : (personas >= 3 ? 0.10 : 0.0);
        double total = 10000.0 * personas * (1 - descuento);
        lblPrecioTicket.setText(String.format(
                "Precio: $%.0f (%.0f%% desc. para %d personas)",
                total, descuento * 100, personas));
    }

    /**
     * Genera dinámicamente un formulario por cada acompañante (titular excluido).
     * Cada bloque incluye: documento, nombre, edad y estatura.
     *
     * @param cantidad número de acompañantes = totalPersonas - 1
     */
    private void regenerarFormularioAcompanantes(int cantidad) {
        panelAcompanantes.getChildren().clear();
        camposAcompanantes.clear();

        for (int i = 0; i < cantidad; i++) {
            int numero = i + 1;

            // Título del acompañante
            Label titulo = new Label("Acompañante " + numero);
            titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #444;");

            // Campos
            TextField txtDocumento = new TextField();
            TextField txtNombre    = new TextField();
            TextField txtEdad      = new TextField();
            TextField txtEstatura  = new TextField();

            txtDocumento.setPromptText("Número de documento");
            txtNombre.setPromptText("Nombre completo");
            txtEdad.setPromptText("Edad (años)");
            txtEstatura.setPromptText("Estatura (ej: 1.70)");

            // Fila de edad + estatura en horizontal
            HBox fila = new HBox(12, txtEdad, txtEstatura);
            txtEdad.setMaxWidth(Double.MAX_VALUE);
            txtEstatura.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(txtEdad, javafx.scene.layout.Priority.ALWAYS);
            HBox.setHgrow(txtEstatura, javafx.scene.layout.Priority.ALWAYS);

            VBox bloque = new VBox(6, titulo, txtDocumento, txtNombre, fila);
            bloque.setPadding(new Insets(8, 12, 8, 12));
            bloque.setStyle("-fx-background-color: white; -fx-background-radius: 6; " +
                            "-fx-border-color: #ddd; -fx-border-radius: 6;");

            panelAcompanantes.getChildren().add(bloque);

            // Orden del array: [0] nombre, [1] edad, [2] estatura, [3] documento
            camposAcompanantes.add(new TextField[]{txtNombre, txtEdad, txtEstatura, txtDocumento});
        }
    }

    // ── Spinner con mouse ────────────────────────────────────────────────────
    @FXML
    private void onPersonasCambian() {
        if ("FAMILIAR".equals(choiceTipoTicket.getValue())) {
            actualizarPrecioFamiliar();
            regenerarFormularioAcompanantes(spinnerPersonas.getValue() - 1);
        }
    }

    // ── Confirmar compra ─────────────────────────────────────────────────────
    @FXML
    private void confirmarCompra() {
        if (visitante.getTicketActivo() != null && visitante.getTicketActivo().isActivo()) {
            mostrarAlerta("Ticket existente",
                    "Ya tienes un ticket activo (" +
                    visitante.getTicketActivo().getTipoTicket().name() + ").\n" +
                    "Desactívalo antes de comprar uno nuevo.");
            return;
        }

        String tipoSeleccionado = choiceTipoTicket.getValue();
        Ticket nuevoTicket = construirTicket(tipoSeleccionado);
        if (nuevoTicket == null) return;

        double precio = nuevoTicket.calcularPrecio();

        if (precio > 0 && !visitante.validarPago(precio)) {
            mostrarError(String.format(
                    "Saldo insuficiente.\nNecesitas $%.0f pero tienes $%.0f.",
                    precio, visitante.getSaldoVirtual()));
            return;
        }

        if (precio > 0) visitante.realizarPago(precio);

        boolean vendido = parque.venderTicket(nuevoTicket);
        if (!vendido) {
            mostrarError("No se pudo vender el ticket. El parque ha alcanzado su capacidad máxima.");
            return;
        }

        visitante.comprarTicket(nuevoTicket);

        mostrarAlerta("¡Compra exitosa!",
                "Ticket " + tipoSeleccionado + " adquirido correctamente.\n" +
                (precio > 0 ? String.format("Se descontaron $%.0f de tu saldo.", precio) : ""));

        volverAlDashboard();
    }

    // ── Construcción del ticket ──────────────────────────────────────────────
    private Ticket construirTicket(String tipo) {
        String id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return switch (tipo) {
            case "GENERAL" -> {
                TicketGeneral tg = new TicketGeneral(visitante.getSaldoVirtual());
                tg.setId(id);
                tg.setTipoTicket(TipoTicket.GENERAL);
                tg.setVisitante(visitante);
                yield tg;
            }
            case "FAMILIAR" -> {
                int personas = spinnerPersonas.getValue();
                if (personas < 2) {
                    mostrarAlerta("Grupo insuficiente",
                            "El ticket familiar requiere al menos 2 personas.");
                    yield null;
                }

                // Validar y leer campos de acompañantes
                List<Visitante> acompanantes = new ArrayList<>();
                for (int i = 0; i < camposAcompanantes.size(); i++) {
                    TextField[] campos   = camposAcompanantes.get(i);
                    String nombre        = campos[0].getText().trim();
                    String edadStr       = campos[1].getText().trim();
                    String estStr        = campos[2].getText().trim();
                    String documento     = campos[3].getText().trim();

                    if (nombre.isEmpty() || edadStr.isEmpty() || estStr.isEmpty() || documento.isEmpty()) {
                        mostrarAlerta("Datos incompletos",
                                "Completa todos los campos del Acompañante " + (i + 1) + ".");
                        yield null;
                    }

                    int edadAcomp;
                    double estaturaAcomp;
                    try {
                        edadAcomp     = Integer.parseInt(edadStr);
                        estaturaAcomp = Double.parseDouble(estStr);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Datos inválidos",
                                "Edad y estatura del Acompañante " + (i + 1) +
                                " deben ser números válidos.");
                        yield null;
                    }

                    if (edadAcomp <= 0 || estaturaAcomp <= 0) {
                        mostrarAlerta("Datos inválidos",
                                "Edad y estatura del Acompañante " + (i + 1) +
                                " deben ser positivos.");
                        yield null;
                    }

                    acompanantes.add(new Visitante(
                            documento,
                            nombre, "", edadAcomp, estaturaAcomp));
                }

                TicketFamiliar tf = new TicketFamiliar();
                tf.setId(id);
                tf.setTipoTicket(TipoTicket.FAMILIAR);
                tf.setVisitante(visitante);
                tf.agregarMiembro(visitante);
                acompanantes.forEach(tf::agregarMiembro);
                yield tf;
            }
            case "FAST_PASS" -> {
                TicketFastPass tfp = new TicketFastPass(30);
                tfp.setId(id);
                tfp.setTipoTicket(TipoTicket.FAST_PASS);
                tfp.setVisitante(visitante);
                parque.obtenerAtraccionesDisponibles().forEach(tfp::agregarAtraccion);
                yield tfp;
            }
            default -> null;
        };
    }

    // ── Navegación ───────────────────────────────────────────────────────────
    @FXML
    private void volverAlDashboard() {
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/dashboard-visitante.fxml");
    }
}