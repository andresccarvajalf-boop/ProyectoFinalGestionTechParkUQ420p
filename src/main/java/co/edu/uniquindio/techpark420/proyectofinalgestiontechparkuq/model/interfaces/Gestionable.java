/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;

import java.util.List;

public interface Gestionable {

    void crear();

    void actualizar();

    void eliminar();

    void consultar();

    List<String> listarElementos();
}
