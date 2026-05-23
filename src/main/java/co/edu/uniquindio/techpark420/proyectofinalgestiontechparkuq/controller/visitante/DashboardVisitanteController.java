package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.visitante;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.controller.base.BaseController;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Visitante;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class DashboardVisitanteController extends BaseController implements Initializable {


    @FXML private Label nombreVisitanteLabel;
    @FXML private Label saldoLabel;
    @FXML private Label saldoCardLabel;
    @FXML private Label notifBadgeLabel;
    @FXML private TreeView<String> mapaTreeView;
    @FXML private ListView<String> favoritasListView;
    @FXML private ImageView imgPerfil;          // ← NUEVO

    private Visitante visitante;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visitante = appContext.getVisitanteEnSesion();
        if (visitante == null) {
            navegarA(VISTA_LOGIN);
            return;
        }
        cargarDatos();
    }

    private void cargarDatos() {
        nombreVisitanteLabel.setText(visitante.getNombre());

        String saldo = String.format("💰 $%.0f", visitante.getSaldoVirtual());
        saldoLabel.setText(saldo);
        saldoCardLabel.setText(saldo);

        long noLeidas = visitante.listarNotificaciones()
                .stream().filter(n -> !n.isLeida()).count();
        notifBadgeLabel.setText(String.valueOf(noLeidas));

        cargarMapa();
        cargarFavoritas();
        cargarFotoPerfil();   // ← NUEVO
    }



    /**
     * Muestra la foto guardada del visitante, o la imagen por defecto (1.png)
     * si todavía no tiene foto asignada o si el archivo fue movido/eliminado.
     */
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

    /**
     * Abre un FileChooser para que el visitante seleccione su foto de perfil.
     * Copia el archivo a la carpeta "fotos/" y persiste la ruta en el modelo.
     */
    @FXML
    private void subirFoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar foto de perfil");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = chooser.showOpenDialog(nombreVisitanteLabel.getScene().getWindow());
        if (archivo == null) return;   // el usuario canceló

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



    private void cargarMapa() {
        TreeItem<String> raiz = new TreeItem<>(parque.getNombre());
        raiz.setExpanded(true);
        for (Zona zona : parque.getZonas()) {
            TreeItem<String> nodoZona = new TreeItem<>("📍 " + zona.getNombre());
            for (Atraccion a : zona.getAtracciones()) {
                String icono = a.getEstado() == EstadoAtraccion.ACTIVA ? "✅" : "❌";
                nodoZona.getChildren().add(new TreeItem<>(icono + " " + a.getNombre()));
            }
            raiz.getChildren().add(nodoZona);
        }
        mapaTreeView.setRoot(raiz);
    }

    private void cargarFavoritas() {
        List<String> nombres = visitante.getAtraccionesFavoritas()
                .stream().map(Atraccion::getNombre).collect(Collectors.toList());
        favoritasListView.setItems(FXCollections.observableArrayList(nombres));
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
                if (monto <= 0) {
                    mostrarAlerta("Valor inválido", "El monto debe ser mayor a 0.");
                    return;
                }
                visitante.recargarSaldo(monto);
                AppContext.getInstance().guardarDatos();
                cargarDatos();
                mostrarAlerta("Recarga exitosa",
                        String.format("Se recargaron $%.0f.\nSaldo actual: $%.0f",
                                monto, visitante.getSaldoVirtual()));
            } catch (NumberFormatException e) {
                mostrarAlerta("Valor inválido", "Ingresa un número válido.");
            }
        });
    }

    @FXML private void irAInicio()          { cargarDatos(); }
    @FXML private void irACompraTicket()    { navegarA(VISTA_COMPRA_TICKET); }
    @FXML private void irAFavoritas()       { cargarFavoritas(); }
    @FXML private void irANotificaciones()  { navegarA(VISTA_NOTIFICACIONES); }

    @FXML
    private void cerrarSesion() {
        appContext.cerrarSesion();
        navegarA(VISTA_LOGIN);
    }

    @FXML
    private void handleAtraccionSeleccionada(MouseEvent event) {
        if (event.getClickCount() == 2) verDetalleAtraccion();
    }

    @FXML
    private void verDetalleAtraccion() {
        TreeItem<String> seleccionado = mapaTreeView.getSelectionModel().getSelectedItem();
        if (seleccionado == null || seleccionado.getParent() == null
                || seleccionado.getParent().getValue() == null
                || seleccionado.getParent().getValue().equals(parque.getNombre())) {
            mostrarAlerta("Sin selección", "Selecciona una atracción del mapa.");
            return;
        }
        String nombreLimpio = seleccionado.getValue()
                .replace("✅ ", "").replace("❌ ", "");
        Atraccion encontrada = parque.buscarAtraccion(nombreLimpio);
        if (encontrada != null) {
            appContext.setAtraccionSeleccionada(encontrada);
            navegarA(VISTA_DETALLE_ATRACCION);
        }
    }
}
