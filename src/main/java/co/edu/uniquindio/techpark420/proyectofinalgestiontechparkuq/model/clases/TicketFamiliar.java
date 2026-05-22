package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.abstractas.Ticket;

public class TicketFamiliar extends Ticket {

    private int cantidadPersonas;
    private double descuentoAplicado;
    private List<Visitante> miembrosFamilia;

    public TicketFamiliar() {
        this.miembrosFamilia = new ArrayList<>();
        this.cantidadPersonas = 0;
        this.descuentoAplicado = 0;
    }

    public TicketFamiliar(List<Visitante> miembrosFamilia) {
        this.miembrosFamilia = miembrosFamilia != null ? miembrosFamilia : new ArrayList<>();
        this.cantidadPersonas = this.miembrosFamilia.size();
        this.descuentoAplicado = 0;
    }

    @Override
    public boolean validarAcceso(Atraccion atraccion) {
        return atraccion != null && validarGrupoFamiliar();
    }

    public boolean validarGrupoFamiliar() {
        return cantidadPersonas >= 2;
    }

    public double calcularDescuento() {
        if (cantidadPersonas >= 5) {
            descuentoAplicado = 0.20;
        } else if (cantidadPersonas >= 3) {
            descuentoAplicado = 0.10;
        } else {
            descuentoAplicado = 0.0;
        }
        return descuentoAplicado;
    }

    @Override
    public double calcularPrecio() {
        double precioBase = 10000;
        calcularDescuento();
        return precioBase * cantidadPersonas * (1 - descuentoAplicado);
    }

    public void agregarMiembro(Visitante visitante) {
        if (visitante != null && !miembrosFamilia.contains(visitante)) {
            miembrosFamilia.add(visitante);
            cantidadPersonas = miembrosFamilia.size();
        }
    }

    public void removerMiembro(Visitante visitante) {
        if (miembrosFamilia.remove(visitante)) {
            cantidadPersonas = miembrosFamilia.size();
        }
    }

    public String mostrarBeneficios() {
        return "Ticket Familiar: descuento hasta " + (calcularDescuento() * 100) + "% según cantidad de personas.";
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public List<Visitante> getMiembrosFamilia() {
        return miembrosFamilia;
    }
}
