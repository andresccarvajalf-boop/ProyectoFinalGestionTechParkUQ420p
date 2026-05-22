package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.interfaces;

import java.util.List;

public interface Gestionable {

    void crear();

    void actualizar();

    void eliminar();

    void consultar();

    List<String> listarElementos();
}
