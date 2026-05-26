package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.ColaVirtual;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.TicketFastPass;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class DetalleAtraccionController extends BaseController implements Initializable {

    @FXML private Label lblNombreAtraccion;
    @FXML private Label lblTipo;
    @FXML private Label lblZona;
    @FXML private Circle circuloEstado;
    @FXML private Label lblEstado;
    @FXML private Label lblMotivoCierre;

    @FXML private Label lblAlturaMinima;
    @FXML private Label lblEdadMinima;
    @FXML private Label lblCapacidad;
    @FXML private Label lblCostoAdicional;

    @FXML private Label lblTiempoEspera;
    @FXML private Label lblPersonasEnCola;
    @FXML private Label lblFastPassDisponible;
    @FXML private ProgressBar barraOcupacion;

    @FXML private Button btnUnirseACola;
    @FXML private Button btnAgregarFavorita;
    @FXML private Label lblMensajeAcceso;

    private Atraccion atraccion;
    private Visitante visitante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atraccion = appContext.getAtraccionSeleccionada();
        visitante = appContext.getVisitanteEnSesion();

        if (atraccion == null) {
            mostrarError("No se pudo cargar la atracción.");
            volverAlDashboard();
            return;
        }

        cargarInfoGeneral();
        cargarInfoCola();
        evaluarAcceso();
        actualizarBotonFavorita();

        appContext.setCallbackTurno(this::mostrarVentanaTurno);
    }

    private void cargarInfoGeneral() {
        lblNombreAtraccion.setText(atraccion.getNombre());
        lblTipo.setText("Tipo: " + atraccion.getTipoAtraccion().name());
        lblZona.setText("Zona: " + encontrarZona());

        EstadoAtraccion estado = atraccion.getEstado();
        lblEstado.setText(estado.name().replace("_", " "));
        switch (estado) {
            case ACTIVA           -> circuloEstado.setFill(Color.web("#4caf50"));
            case EN_MANTENIMIENTO -> circuloEstado.setFill(Color.web("#ff9800"));
            case CERRADA          -> circuloEstado.setFill(Color.web("#f44336"));
        }

        boolean mostrarMotivo = estado != EstadoAtraccion.ACTIVA
                && atraccion.getMotivoCierre() != null
                && !atraccion.getMotivoCierre().isBlank();
        lblMotivoCierre.setVisible(mostrarMotivo);
        lblMotivoCierre.setManaged(mostrarMotivo);
        if (mostrarMotivo) {
            lblMotivoCierre.setText("Motivo: " + atraccion.getMotivoCierre());
        }

        lblAlturaMinima.setText(String.format("%.0f cm", atraccion.getAlturaMinima() * 100));
        lblEdadMinima.setText(atraccion.getEdadMinima() + " años");
        lblCapacidad.setText(String.valueOf(atraccion.getCapacidadMaxima()));

        if (atraccion.getCostoAdicional() > 0) {
            lblCostoAdicional.setText(String.format("$%.0f", atraccion.getCostoAdicional()));
        } else {
            lblCostoAdicional.setText("Sin costo adicional");
        }
    }

    private void cargarInfoCola() {
        ColaVirtual cola = atraccion.getColaVirtual();

        if (cola == null) {
            lblTiempoEspera.setText("N/D");
            lblPersonasEnCola.setText("0");
            lblFastPassDisponible.setText("Fast Pass: No disponible");
            barraOcupacion.setProgress(0);
            return;
        }

        int personas     = cola.obtenerCantidadPersonas();
        int tiempoEspera = cola.calcularTiempoEspera();

        lblTiempoEspera.setText("~" + tiempoEspera + " min");
        lblPersonasEnCola.setText(String.valueOf(personas));
        lblFastPassDisponible.setText(
                atraccion.esFastPassDisponible()
                        ? "Fast Pass: ✅ Disponible"
                        : "Fast Pass: ❌ No disponible");

        double ocupacion = atraccion.getCapacidadMaxima() > 0
                ? (double) personas / atraccion.getCapacidadMaxima()
                : 0;
        barraOcupacion.setProgress(Math.min(ocupacion, 1.0));
    }

    private void mostrarVentanaTurno() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(600);
        stage.setHeight(420);
        stage.centerOnScreen();

        MediaPlayer[] mediaPlayerRef = new MediaPlayer[1];
        try {
            File audioFile = new File("audio/aud001.mp3");
            if (audioFile.exists()) {
                Media media = new Media(audioFile.toURI().toString());
                MediaPlayer mp = new MediaPlayer(media);
                mp.setVolume(0.5);
                mp.setOnReady(mp::play);
                mp.setOnError(() -> mediaPlayerRef[0] = null);
                mediaPlayerRef[0] = mp;
            }
        } catch (Exception ignored) {}

        java.time.LocalDate hoy = java.time.LocalDate.now();
        co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.HistorialVisita historialHoy =
                visitante.getHistorialVisitas().stream()
                        .filter(h -> h.getFechaVisita().equals(hoy))
                        .findFirst()
                        .orElse(null);

        if (historialHoy == null) {
            historialHoy = new co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.HistorialVisita(
                    "H-" + System.currentTimeMillis(), visitante);
            visitante.getHistorialVisitas().add(historialHoy);
        }
        historialHoy.registrarAtraccion(atraccion);
        if (visitante.getTicketActivo() != null) {
            historialHoy.registrarTicket(visitante.getTicketActivo());
        }
        AppContext.getInstance().guardarDatos();

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        var imgStream = getClass().getResourceAsStream(
                "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/imgs/1.png");
        if (imgStream != null) {
            ImageView imageView = new ImageView(new Image(imgStream));
            imageView.setPreserveRatio(false);
            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());
            root.getChildren().add(imageView);
        }

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(stage.widthProperty());
        overlay.heightProperty().bind(stage.heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.45));

        Label lblTurno = new Label("🎉 ¡ES TU TURNO!");
        lblTurno.setStyle("""
                -fx-font-size: 40px;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 12, 0.5, 2, 4);
                """);

        Label lblAtraccion = new Label(atraccion.getNombre());
        lblAtraccion.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #FFD700;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.4, 1, 3);
                """);

        Label lblSub = new Label("Dirígete a la atracción, ¡te están esperando!");
        lblSub.setStyle("""
                -fx-font-size: 15px;
                -fx-text-fill: #eeeeee;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0.3, 1, 2);
                """);

        Button btnCerrar = new Button("¡Entendido, voy!");
        btnCerrar.setStyle("""
                -fx-background-color: #4caf50;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-padding: 10 36;
                -fx-background-radius: 30;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0.3, 0, 3);
                """);
        btnCerrar.setOnMouseEntered(e ->
                btnCerrar.setStyle(btnCerrar.getStyle().replace("#4caf50", "#388e3c")));
        btnCerrar.setOnMouseExited(e ->
                btnCerrar.setStyle(btnCerrar.getStyle().replace("#388e3c", "#4caf50")));
        btnCerrar.setOnAction(e -> {
            detenerAudio(mediaPlayerRef);
            FadeTransition ft = new FadeTransition(Duration.millis(400), stage.getScene().getRoot());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(ev -> stage.close());
            ft.play();
        });

        VBox contenido = new VBox(20, lblTurno, lblAtraccion, lblSub, btnCerrar);
        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(32));

        root.getChildren().addAll(overlay, contenido);

        root.setOpacity(0);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        stage.setOnHidden(e -> {
            detenerAudio(mediaPlayerRef);
            Platform.runLater(() -> {
                btnUnirseACola.setDisable(false);
                btnUnirseACola.setText("Unirse a la Cola");
                cargarInfoCola();
            });
        });
    }

    private void detenerAudio(MediaPlayer[] ref) {
        if (ref[0] != null) {
            MediaPlayer.Status status = ref[0].getStatus();
            if (status != MediaPlayer.Status.DISPOSED && status != MediaPlayer.Status.UNKNOWN) {
                ref[0].stop();
            }
            ref[0].dispose();
            ref[0] = null;
        }
    }

    private void evaluarAcceso() {
        if (visitante.getTicketActivo() == null) {
            lblMensajeAcceso.setText("⚠ No tienes un ticket activo.");
            lblMensajeAcceso.setStyle("-fx-text-fill: #f44336;");
            btnUnirseACola.setDisable(true);
            return;
        }

        if (atraccion.getEstado() != EstadoAtraccion.ACTIVA) {
            lblMensajeAcceso.setText("❌ Esta atracción no está disponible actualmente.");
            lblMensajeAcceso.setStyle("-fx-text-fill: #f44336;");
            btnUnirseACola.setDisable(true);
            return;
        }

        boolean cumpleEdad   = visitante.getEdad()     >= atraccion.getEdadMinima();
        boolean cumpleAltura = visitante.getEstatura() >= atraccion.getAlturaMinima();

        if (!cumpleEdad) {
            lblMensajeAcceso.setText("❌ No cumples con la edad mínima requerida ("
                    + atraccion.getEdadMinima() + " años).");
            lblMensajeAcceso.setStyle("-fx-text-fill: #f44336;");
            btnUnirseACola.setDisable(true);
            return;
        }
        if (!cumpleAltura) {
            lblMensajeAcceso.setText(String.format(
                    "❌ No cumples con la altura mínima requerida (%.0f cm).",
                    atraccion.getAlturaMinima() * 100));
            lblMensajeAcceso.setStyle("-fx-text-fill: #f44336;");
            btnUnirseACola.setDisable(true);
            return;
        }

        if (atraccion.getCostoAdicional() > 0) {
            if (!visitante.validarPago(atraccion.getCostoAdicional())) {
                lblMensajeAcceso.setText(String.format(
                        "⚠ Saldo insuficiente. Esta atracción requiere $%.0f adicionales.",
                        atraccion.getCostoAdicional()));
                lblMensajeAcceso.setStyle("-fx-text-fill: #ff9800;");
                btnUnirseACola.setDisable(true);
                return;
            }
        }

        lblMensajeAcceso.setText("✅ Cumples todos los requisitos para ingresar.");
        lblMensajeAcceso.setStyle("-fx-text-fill: #4caf50;");
        btnUnirseACola.setDisable(false);
    }

    private void actualizarBotonFavorita() {
        if (visitante.getAtraccionesFavoritas().contains(atraccion)) {
            btnAgregarFavorita.setText("★ En favoritas");
        } else {
            btnAgregarFavorita.setText("☆ Agregar a favoritas");
        }
    }

    @FXML
    private void unirseACola() {
        ColaVirtual cola = atraccion.getColaVirtual();
        if (cola == null || !cola.isActiva()) {
            mostrarAlerta("Cola no disponible", "Esta atracción no tiene cola virtual activa.");
            return;
        }
        if (cola.estaLlena()) {
            mostrarAlerta("Cola llena", "La cola de esta atracción está llena. Intenta más tarde.");
            return;
        }
        if (cola.getVisitantesEnCola().contains(visitante) ||
            cola.getVisitantesFastPass().contains(visitante)) {
            mostrarAlerta("Ya estás en cola", "Ya te encuentras en la cola de esta atracción.");
            return;
        }

        boolean agregado;
        Ticket ticket = visitante.getTicketActivo();

        if (ticket instanceof TicketFastPass tfp && tfp.validarFastPass(atraccion)) {
            agregado = cola.agregarFastPass(visitante);
        } else {
            agregado = cola.agregarVisitante(visitante);
        }

        if (agregado) {
            if (atraccion.getCostoAdicional() > 0) {
                visitante.realizarPago(atraccion.getCostoAdicional());
            }
          
            cargarInfoCola();
            btnUnirseACola.setDisable(true);
            btnUnirseACola.setText("⏳ En cola");
        } else {
            mostrarError("No fue posible unirte a la cola.");
        }
    }

    @FXML
    private void toggleFavorita() {
        List<Atraccion> favoritas = visitante.getAtraccionesFavoritas();
        if (favoritas.contains(atraccion)) {
            visitante.removerFavorita(atraccion);
            btnAgregarFavorita.setText("☆ Agregar a favoritas");
            AppContext.getInstance().guardarDatos();
            mostrarAlerta("Removida", atraccion.getNombre() + " fue removida de tus favoritas.");
        } else {
            visitante.agregarFavorita(atraccion);
            btnAgregarFavorita.setText("★ En favoritas");
            AppContext.getInstance().guardarDatos();
            mostrarAlerta("Agregada", atraccion.getNombre() + " fue añadida a tus favoritas.");
        }
    }

    @FXML
    private void volverAlDashboard() {
        appContext.clearCallbackTurno();
        navegarA("/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/visitante/dashboard-visitante.fxml");
    }

    private String encontrarZona() {
        for (Zona zona : parque.getZonas()) {
            if (zona.getAtracciones().contains(atraccion)) {
                return zona.getNombre();
            }
        }
        return "Sin zona";
    }
}