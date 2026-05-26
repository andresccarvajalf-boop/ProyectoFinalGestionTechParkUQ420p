/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class DashboardVisitanteController extends BaseController implements Initializable {

    @FXML private Label nombreVisitanteLabel;
    @FXML private Label saldoLabel;
    @FXML private Label saldoCardLabel;
    @FXML private Label notifBadgeLabel;
    @FXML private FlowPane mapaFlowPane;
    @FXML private ScrollPane mapaScrollPane;
    @FXML private ImageView imgPerfil;

    private Visitante visitante;


    private static final String[] ZONE_GRADIENTS = {
        "linear-gradient(to bottom right, #e74c3c, #c0392b)",
        "linear-gradient(to bottom right, #3498db, #1a5276)",
        "linear-gradient(to bottom right, #2ecc71, #1a8a4a)",
        "linear-gradient(to bottom right, #9b59b6, #6c3483)",
        "linear-gradient(to bottom right, #f39c12, #b7770d)",
        "linear-gradient(to bottom right, #1abc9c, #148f77)",
        "linear-gradient(to bottom right, #e67e22, #a04000)",
        "linear-gradient(to bottom right, #e91e63, #880e4f)",
    };

    private static final String[] ATTRACTION_EMOJIS =
        {"🎢", "🎡", "🎠", "🚀", "⚡", "🌊", "🐉", "🦁", "🏎", "🎯"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visitante = appContext.getVisitanteEnSesion();
        if (visitante == null) { navegarA(VISTA_LOGIN); return; }
        cargarDatos();
    }

    private void cargarDatos() {
        nombreVisitanteLabel.setText(visitante.getNombre());
        String saldo = String.format("💰 $%.0f", visitante.getSaldoVirtual());
        saldoLabel.setText(saldo);
        saldoCardLabel.setText(saldo);
        long noLeidas = visitante.listarNotificaciones().stream().filter(n -> !n.isLeida()).count();
        notifBadgeLabel.setText(String.valueOf(noLeidas));
        cargarMapaVisual();
        cargarFotoPerfil();
    }



    private void cargarMapaVisual() {
        mapaFlowPane.getChildren().clear();

        Set<String> nombresFavoritas = visitante.getAtraccionesFavoritas()
                .stream().map(Atraccion::getNombre).collect(Collectors.toSet());

        List<Zona> zonas = parque.getZonas();
        for (int i = 0; i < zonas.size(); i++) {
            String gradient = ZONE_GRADIENTS[i % ZONE_GRADIENTS.length];
            mapaFlowPane.getChildren().add(crearModuloZona(zonas.get(i), gradient, nombresFavoritas));
        }
    }

    private VBox crearModuloZona(Zona zona, String gradient, Set<String> favoritas) {
        VBox card = new VBox(0);
        card.setPrefWidth(210);
        card.setMaxWidth(210);
        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,255,255,0.18);" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 14, 0, 0, 4);"
        );


        VBox header = new VBox(4);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(14, 12, 12, 12));
        header.setStyle("-fx-background-color: " + gradient + "; -fx-background-radius: 13 13 0 0;");

        Label emojiLbl = new Label(getZonaEmoji(zona.getNombre()));
        emojiLbl.setStyle("-fx-font-size: 28px;");

        Label zonaNombre = new Label(zona.getNombre().toUpperCase());
        zonaNombre.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-wrap-text: true;");
        zonaNombre.setAlignment(Pos.CENTER);
        zonaNombre.setMaxWidth(186);
        zonaNombre.setWrapText(true);

        long activas = zona.getAtracciones().stream().filter(a -> a.getEstado() == EstadoAtraccion.ACTIVA).count();
        Label statsLbl = new Label(activas + "/" + zona.getAtracciones().size() + " activas");
        statsLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 10px;" +
            "-fx-background-color: rgba(0,0,0,0.25); -fx-background-radius: 10; -fx-padding: 2 8 2 8;");

        header.getChildren().addAll(emojiLbl, zonaNombre, statsLbl);


        VBox body = new VBox(2);
        body.setPadding(new Insets(8, 10, 10, 10));

        if (zona.getAtracciones().isEmpty()) {
            Label vacio = new Label("Sin atracciones");
            vacio.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 11px; -fx-font-style: italic;");
            body.getChildren().add(vacio);
        } else {
            for (Atraccion a : zona.getAtracciones()) {
                body.getChildren().add(crearFilaAtraccion(a, favoritas, zona));
            }
        }

        card.getChildren().addAll(header, body);


        String baseStyle = "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 14; -fx-border-color: rgba(255,255,255,0.18);" +
            "-fx-border-radius: 14; -fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 14, 0, 0, 4);";
        String hoverStyle = "-fx-background-color: rgba(255,255,255,0.13);" +
            "-fx-background-radius: 14; -fx-border-color: rgba(255,255,255,0.35);" +
            "-fx-border-radius: 14; -fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 20, 0, 0, 6);";
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(baseStyle));

        return card;
    }

    private HBox crearFilaAtraccion(Atraccion atraccion, Set<String> favoritas, Zona zona) {
        boolean activa = atraccion.getEstado() == EstadoAtraccion.ACTIVA;
        boolean esFav = favoritas.contains(atraccion.getNombre());

        HBox fila = new HBox(6);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(5, 6, 5, 6));
        fila.setStyle("-fx-background-color: rgba(255,255,255," + (activa ? "0.09" : "0.04") + ");" +
            "-fx-background-radius: 8; -fx-cursor: hand;");

        Label estadoIcon = new Label(activa ? "✅" : "❌");
        estadoIcon.setStyle("-fx-font-size: 11px;");

        String texto = (esFav ? "⭐ " : "") + atraccion.getNombre();
        Label nombreLbl = new Label(texto);
        nombreLbl.setStyle("-fx-text-fill: " + (activa ? "rgba(255,255,255,0.92)" : "rgba(255,255,255,0.42)") + ";" +
            "-fx-font-size: 11px; -fx-wrap-text: true;");
        nombreLbl.setMaxWidth(155);
        nombreLbl.setWrapText(true);

        Tooltip.install(fila, new Tooltip(
            atraccion.getNombre() + "\nEstado: " + atraccion.getEstado() +
            (esFav ? "\n⭐ En tus favoritas" : "") + "\nZona: " + zona.getNombre()
        ));

        fila.getChildren().addAll(estadoIcon, nombreLbl);

        fila.setOnMouseClicked(e -> {
            if (activa) {
                appContext.setAtraccionSeleccionada(atraccion);
                navegarA(VISTA_DETALLE_ATRACCION);
            } else {
                mostrarAlerta("Atracción no disponible",
                    "\"" + atraccion.getNombre() + "\" está fuera de servicio actualmente.");
            }
        });

        String baseStyle = "-fx-background-color: rgba(255,255,255," + (activa ? "0.09" : "0.04") + ");" +
            "-fx-background-radius: 8; -fx-cursor: hand;";
        fila.setOnMouseEntered(ev -> { if (activa) fila.setStyle(
            "-fx-background-color: rgba(255,255,255,0.18); -fx-background-radius: 8; -fx-cursor: hand;"); });
        fila.setOnMouseExited(ev -> fila.setStyle(baseStyle));

        return fila;
    }

    private String getZonaEmoji(String nombre) {
        String lower = nombre.toLowerCase();
        if (lower.contains("agua") || lower.contains("acuatic")) return "🌊";
        if (lower.contains("aventura") || lower.contains("extremo")) return "🎢";
        if (lower.contains("niño") || lower.contains("kids") || lower.contains("infantil")) return "🎠";
        if (lower.contains("natura") || lower.contains("selva") || lower.contains("bosque")) return "🌿";
        if (lower.contains("terror") || lower.contains("miedo")) return "👻";
        if (lower.contains("espacio") || lower.contains("galaxia") || lower.contains("ciencia")) return "🚀";
        if (lower.contains("medieval") || lower.contains("castillo")) return "🏰";
        if (lower.contains("tech") || lower.contains("tecnolog")) return "⚡";
        if (lower.contains("safari") || lower.contains("animal")) return "🦁";
        if (lower.contains("pirata") || lower.contains("mar")) return "🏴‍☠️";
        return ATTRACTION_EMOJIS[Math.abs(nombre.hashCode()) % ATTRACTION_EMOJIS.length];
    }



    private void cargarFotoPerfil() {
        if (imgPerfil == null) return;
        String ruta = visitante.getFotoPerfil();
        Image imagen;
        if (ruta != null && new File(ruta).exists()) {
            imagen = new Image(new File(ruta).toURI().toString());
        } else {
            imagen = new Image(getClass().getResourceAsStream(
                "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/imgs/1.png"
            ));
        }
        imgPerfil.setImage(imagen);
    }

    @FXML
    private void subirFoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar foto de perfil");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File archivo = chooser.showOpenDialog(nombreVisitanteLabel.getScene().getWindow());
        if (archivo == null) return;
        try {
            new File("fotos").mkdirs();
            String ext = archivo.getName().substring(archivo.getName().lastIndexOf('.'));
            String destino = "fotos/" + visitante.getDocumento() + ext;
            Files.copy(archivo.toPath(), Path.of(destino), StandardCopyOption.REPLACE_EXISTING);
            visitante.setFotoPerfil(destino);
            AppContext.getInstance().guardarDatos();
            cargarFotoPerfil();
            mostrarAlerta("Foto actualizada", "Tu foto de perfil fue guardada correctamente.");
        } catch (IOException e) {
            mostrarError("No se pudo guardar la foto: " + e.getMessage());
        }
    }



    @FXML
    private void irARecargarSaldo() {
        javafx.scene.control.TextInputDialog dialogo =
                new javafx.scene.control.TextInputDialog("10000");
        dialogo.setTitle("Recargar Saldo");
        dialogo.setHeaderText("Recarga de saldo virtual");
        dialogo.setContentText("Ingresa el monto a recargar ($):");
        dialogo.showAndWait().ifPresent(valor -> {
            try {
                double monto = Double.parseDouble(valor.trim());
                if (monto <= 0) { mostrarAlerta("Valor inválido", "El monto debe ser mayor a 0."); return; }
                visitante.recargarSaldo(monto);
                AppContext.getInstance().guardarDatos();
                cargarDatos();
                mostrarAlerta("Recarga exitosa",
                    String.format("Se recargaron $%.0f.\nSaldo actual: $%.0f", monto, visitante.getSaldoVirtual()));
            } catch (NumberFormatException e) {
                mostrarAlerta("Valor inválido", "Ingresa un número válido.");
            }
        });
    }

    @FXML private void irAInicio()         { cargarDatos(); }
    @FXML private void irACompraTicket()   { navegarA(VISTA_COMPRA_TICKET); }
    @FXML private void irANotificaciones() { navegarA(VISTA_NOTIFICACIONES); }

    @FXML
    private void cerrarSesion() {
        appContext.cerrarSesion();
        navegarA(VISTA_LOGIN);
    }
}