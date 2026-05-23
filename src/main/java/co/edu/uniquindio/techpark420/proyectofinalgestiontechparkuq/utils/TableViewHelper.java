package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewHelper {

    private TableViewHelper() {}

    public static <T> void configurarColumna(TableColumn<T, ?> columna, String propiedad) {
        columna.setCellValueFactory(new PropertyValueFactory<>(propiedad));
    }

    public static <T> void configurarColumnas(TableView<T> tabla, String... propiedades) {
        if (tabla.getColumns().size() < propiedades.length) return;
        for (int i = 0; i < propiedades.length; i++) {
            @SuppressWarnings("unchecked")
            TableColumn<T, Object> col = (TableColumn<T, Object>) tabla.getColumns().get(i);
            col.setCellValueFactory(new PropertyValueFactory<>(propiedades[i]));
        }
    }

    public static <T> void ajustarAnchoColumnas(TableView<T> tabla) {
        int total = tabla.getColumns().size();
        if (total == 0) return;
        double porcentaje = 1.0 / total;
        for (TableColumn<T, ?> col : tabla.getColumns()) {
            col.prefWidthProperty().bind(tabla.widthProperty().multiply(porcentaje));
        }
    }

    public static <T> void deshabilitarEdicion(TableView<T> tabla) {
        tabla.setEditable(false);
    }

    public static <T> void configurarPlaceholder(TableView<T> tabla, String mensaje) {
        tabla.setPlaceholder(new javafx.scene.control.Label(mensaje));
    }
}