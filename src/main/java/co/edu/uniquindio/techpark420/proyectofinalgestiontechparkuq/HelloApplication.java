/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AppContext.getInstance().setStagePrincipal(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/views/login-view.fxml"
        ));
        StackPane root = loader.load();

        Image icono = new Image(getClass().getResourceAsStream(
            "/co/edu/uniquindio/techpark420/proyectofinalgestiontechparkuq/imgs/001.png"
        ));
        stage.getIcons().add(icono);

        Scene scene = new Scene(root, 1024, 700);
        stage.setTitle("Tech-Park UQ");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        AppContext.getInstance().guardarDatos();
    }

    public static void main(String[] args) {
        launch(args);
    }
}