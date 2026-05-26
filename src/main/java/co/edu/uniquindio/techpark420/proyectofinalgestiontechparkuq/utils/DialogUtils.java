/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.utils;

import java.util.Optional;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Administrador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.AlertaClimatica;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Atraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Operador;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.RevisionTecnica;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Zona;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DialogUtils {

    private DialogUtils() {}

    public static Optional<Zona> mostrarDialogoZona(Zona zonaExistente) {
        Dialog<Zona> dialog = new Dialog<>();
        dialog.setTitle(zonaExistente == null ? "Nueva Zona" : "Editar Zona");

        ButtonType guardarBtn = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombre = new TextField();
        TextField capacidad = new TextField();

        if (zonaExistente != null) {
            nombre.setText(zonaExistente.getNombre());
            capacidad.setText(String.valueOf(zonaExistente.getCapacidadMaxima()));
        }

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombre, 1, 0);
        grid.add(new Label("Capacidad máxima:"), 0, 1);
        grid.add(capacidad, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == guardarBtn) {
                try {
                    String nombreVal = nombre.getText().trim();
                    int capacidadVal = Integer.parseInt(capacidad.getText().trim());
                    if (nombreVal.isEmpty()) return null;

                    if (zonaExistente != null) {
                        zonaExistente.setNombre(nombreVal);
                        zonaExistente.setCapacidadMaxima(capacidadVal);
                        return zonaExistente;
                    } else {
                        String id = "ZONA-" + System.currentTimeMillis();
                        return new Zona(id, nombreVal, capacidadVal);
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<Atraccion> mostrarDialogoAtraccion(Atraccion atraccionExistente) {
        Dialog<Atraccion> dialog = new Dialog<>();
        dialog.setTitle(atraccionExistente == null ? "Nueva Atracción" : "Editar Atracción");

        ButtonType guardarBtn = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombre = new TextField();
        ComboBox<TipoAtraccion> tipo = new ComboBox<>();
        tipo.getItems().addAll(TipoAtraccion.values());
        TextField capacidad = new TextField();
        TextField alturaMinima = new TextField();
        TextField edadMinima = new TextField();
        TextField costoAdicional = new TextField();

        if (atraccionExistente != null) {
            nombre.setText(atraccionExistente.getNombre());
            tipo.setValue(atraccionExistente.getTipoAtraccion());
            capacidad.setText(String.valueOf(atraccionExistente.getCapacidadMaxima()));
            alturaMinima.setText(String.valueOf(atraccionExistente.getAlturaMinima()));
            edadMinima.setText(String.valueOf(atraccionExistente.getEdadMinima()));
            costoAdicional.setText(String.valueOf(atraccionExistente.getCostoAdicional()));
        }

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombre, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1);
        grid.add(tipo, 1, 1);
        grid.add(new Label("Capacidad máxima:"), 0, 2);
        grid.add(capacidad, 1, 2);
        grid.add(new Label("Altura mínima (m):"), 0, 3);
        grid.add(alturaMinima, 1, 3);
        grid.add(new Label("Edad mínima:"), 0, 4);
        grid.add(edadMinima, 1, 4);
        grid.add(new Label("Costo adicional:"), 0, 5);
        grid.add(costoAdicional, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == guardarBtn) {
                try {
                    String nombreVal = nombre.getText().trim();
                    if (nombreVal.isEmpty() || tipo.getValue() == null) return null;

                    String id = "ATRAC-" + System.currentTimeMillis();



                    Atraccion atraccion = atraccionExistente != null
                            ? atraccionExistente
                            : new Atraccion(id, nombreVal, tipo.getValue(),
                                            Integer.parseInt(capacidad.getText().trim()));

                    atraccion.setNombre(nombreVal);
                    atraccion.setTipoAtraccion(tipo.getValue());
                    atraccion.setCapacidadMaxima(Integer.parseInt(capacidad.getText().trim()));
                    atraccion.setAlturaMinima(Double.parseDouble(alturaMinima.getText().trim()));
                    atraccion.setEdadMinima(Integer.parseInt(edadMinima.getText().trim()));
                    atraccion.setCostoAdicional(Double.parseDouble(costoAdicional.getText().trim()));

                    return atraccion;

                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<RevisionTecnica> mostrarDialogoRevision(Operador operador, Atraccion atraccion) {
        Dialog<RevisionTecnica> dialog = new Dialog<>();
        dialog.setTitle("Registrar Revisión Técnica");

        ButtonType guardarBtn = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea descripcion = new TextArea();
        descripcion.setPrefRowCount(3);
        TextArea observaciones = new TextArea();
        observaciones.setPrefRowCount(3);
        CheckBox aprobada = new CheckBox("Revisión aprobada");

        grid.add(new Label("Descripción:"), 0, 0);
        grid.add(descripcion, 1, 0);
        grid.add(new Label("Observaciones:"), 0, 1);
        grid.add(observaciones, 1, 1);
        grid.add(aprobada, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == guardarBtn) {
                String id = "REV-" + System.currentTimeMillis();
                RevisionTecnica revision = new RevisionTecnica(
                        id, descripcion.getText().trim(), operador, atraccion);
                revision.actualizarObservaciones(observaciones.getText().trim());
                if (aprobada.isSelected()) {
                    revision.aprobarRevision();
                } else {
                    revision.rechazarRevision("No aprobada por el operador");
                }
                return revision;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<AlertaClimatica> mostrarDialogoAlerta(Administrador administrador) {
        Dialog<AlertaClimatica> dialog = new Dialog<>();
        dialog.setTitle("Nueva Alerta Climática");

        ButtonType guardarBtn = new ButtonType("Activar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> tipoAlerta = new ComboBox<>();
        tipoAlerta.getItems().addAll(
                "Tormenta eléctrica", "Lluvia fuerte", "Vientos fuertes", "Granizo");
        TextArea descripcion = new TextArea();
        descripcion.setPrefRowCount(3);

        grid.add(new Label("Tipo de alerta:"), 0, 0);
        grid.add(tipoAlerta, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(descripcion, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == guardarBtn && tipoAlerta.getValue() != null) {
                String id = "ALERTA-" + System.currentTimeMillis();
                return new AlertaClimatica(
                        id, tipoAlerta.getValue(), descripcion.getText().trim(), administrador);
            }
            return null;
        });

        return dialog.showAndWait();
    }



    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    public static void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}