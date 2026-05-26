/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationUtils {

    private static final String BASE_PATH = "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/";

    private NavigationUtils() {}

    public static FXMLLoader cargarVista(Stage stage, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(BASE_PATH + rutaFxml));
            Parent root = loader.load();
            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
            stage.show();
            return loader;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista: " + rutaFxml, e);
        }
    }

    public static FXMLLoader cargarVistaEnNuevaVentana(String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(BASE_PATH + rutaFxml));
            Parent root = loader.load();
            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle(titulo);
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.show();
            return loader;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir la ventana: " + rutaFxml, e);
        }
    }

    public static void navegarA(Stage stage, String rutaFxml) {
        cargarVista(stage, rutaFxml);
    }
}