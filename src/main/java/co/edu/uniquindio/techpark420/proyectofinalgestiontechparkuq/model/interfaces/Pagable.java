/*
Universidad del Quindío - Facultad de Ingeniería
Autor: Andres Camilo Carvajal Figueroa C.C 1066602456
Profesor: Ing. Jhan Carlos Martínez Ceballos 
2026-05
GNU GPL
*/

package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;

public interface Pagable {

    boolean realizarPago(double valor);

    boolean validarPago(double valor);

    String generarFactura();

    double consultarSaldo();
}
