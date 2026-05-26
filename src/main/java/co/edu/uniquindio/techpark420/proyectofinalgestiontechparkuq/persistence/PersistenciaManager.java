/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases.Parque;

public class PersistenciaManager {

    private static final String ARCHIVO = "techpark_data.dat";

    public static void guardar(Parque parque) {

        new File("fotos").mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(parque);
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    public static Parque cargar() {

        new File("fotos").mkdirs();

        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Parque) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            return null;
        }
    }
}
