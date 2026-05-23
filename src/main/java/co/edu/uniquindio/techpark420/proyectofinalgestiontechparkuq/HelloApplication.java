package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.app.AppContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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