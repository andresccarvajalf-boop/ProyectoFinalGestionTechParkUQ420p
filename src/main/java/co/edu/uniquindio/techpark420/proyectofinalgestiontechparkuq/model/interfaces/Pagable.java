package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;

public interface Pagable {

    boolean realizarPago(double valor);

    boolean validarPago(double valor);

    String generarFactura();

    double consultarSaldo();
}
