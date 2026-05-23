package co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.EstadoAtraccion;
import co.edu.uniquindio.techpark420.proyectofinalgestiontechparkuq.model.enums.TipoAtraccion;

public class Atraccion implements Serializable {

    private String id;
    private String nombre;
    private TipoAtraccion tipoAtraccion;
    private int capacidadMaxima;
    private double alturaMinima;
    private int edadMinima;
    private double costoAdicional;
    private int visitantesAcumulados;
    private int tiempoEspera;
    private EstadoAtraccion estado;
    private String motivoCierre;
    private ColaVirtual colaVirtual;
    private List<RevisionTecnica> revisionesTecnicas;
    private List<Operador> operadoresResponsables;

    public Atraccion() {
        this.estado = EstadoAtraccion.ACTIVA;
        this.revisionesTecnicas = new ArrayList<>();
        this.operadoresResponsables = new ArrayList<>();
    }

    public Atraccion(String id, String nombre, TipoAtraccion tipoAtraccion, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.tipoAtraccion = tipoAtraccion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = EstadoAtraccion.ACTIVA;

        this.revisionesTecnicas = new ArrayList<>();
        this.operadoresResponsables = new ArrayList<>();
    }

    public boolean validarAcceso(Visitante visitante) {
        if (visitante == null) {
            return false;
        }

        if (estado != EstadoAtraccion.ACTIVA) {
            return false;
        }

        return visitante.getEdad() >= edadMinima
                && visitante.getEstatura() >= alturaMinima;
    }

    public void cambiarEstado(EstadoAtraccion nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void registrarIngreso(Visitante visitante) {
        if (validarAcceso(visitante)) {
            visitantesAcumulados++;
            verificarMantenimiento();
        }
    }

    public void actualizarTiempoEspera(int tiempo) {
        if (tiempo >= 0) {
            this.tiempoEspera = tiempo;
        }
    }

    public void verificarMantenimiento() {
        if (visitantesAcumulados >= 3) { //PUSE 3 DE EJEMPLO PARA COMPROBAR, OBVIAMENTE ES 500
            estado = EstadoAtraccion.EN_MANTENIMIENTO;
            motivoCierre = "Mantenimiento preventivo";
        }
    }

    public void registrarRevision(RevisionTecnica revision) {
        if (revision != null) {
            revisionesTecnicas.add(revision);

            if (revision.isAprobada()) {
                estado = EstadoAtraccion.ACTIVA;
                motivoCierre = "";
                visitantesAcumulados = 0;
            }
        }
    }

    public void cerrarPorClima(AlertaClimatica alerta) {
        estado = EstadoAtraccion.CERRADA;

        if (alerta != null) {
            motivoCierre = "Cierre por alerta climática: " + alerta.getTipoAlerta();
        }
    }

    public void notificarCierre(String mensaje) {
        System.out.println("Notificación: " + mensaje);
    }

    public void agregarOperador(Operador operador) {
        if (operador != null && !operadoresResponsables.contains(operador)) {
            operadoresResponsables.add(operador);
        }
    }

    public void removerOperador(Operador operador) {
        operadoresResponsables.remove(operador);
    }

    public int calcularCapacidadDisponible() {
        if (colaVirtual == null) {
            return capacidadMaxima;
        }

        return capacidadMaxima - colaVirtual.obtenerCantidadPersonas();
    }

    public boolean esFastPassDisponible() {
        return colaVirtual != null && colaVirtual.isActiva();
    }

    public void reiniciarContadorVisitantes() {
        visitantesAcumulados = 0;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoAtraccion getTipoAtraccion() {
        return tipoAtraccion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getAlturaMinima() {
        return alturaMinima;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public double getCostoAdicional() {
        return costoAdicional;
    }

    public int getVisitantesAcumulados() {
        return visitantesAcumulados;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public EstadoAtraccion getEstado() {
        return estado;
    }

    public String getMotivoCierre() {
        return motivoCierre;
    }

    public ColaVirtual getColaVirtual() {
        return colaVirtual;
    }

    public List<RevisionTecnica> getRevisiones() {
        return revisionesTecnicas;
    }

    public List<Operador> getOperadoresResponsables() {
        return operadoresResponsables;
    }
    public void setId(String id) {
    this.id = id;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public void setTipoAtraccion(TipoAtraccion tipoAtraccion) {
    this.tipoAtraccion = tipoAtraccion;
}

public void setCapacidadMaxima(int capacidadMaxima) {
    this.capacidadMaxima = capacidadMaxima;
}

public void setAlturaMinima(double alturaMinima) {
    this.alturaMinima = alturaMinima;
}

public void setEdadMinima(int edadMinima) {
    this.edadMinima = edadMinima;
}

public void setCostoAdicional(double costoAdicional) {
    this.costoAdicional = costoAdicional;
}

public void setVisitantesAcumulados(int visitantesAcumulados) {
    this.visitantesAcumulados = visitantesAcumulados;
}

public void setTiempoEspera(int tiempoEspera) {
    this.tiempoEspera = tiempoEspera;
}

public void setEstado(EstadoAtraccion estado) {
    this.estado = estado;
}

public void setMotivoCierre(String motivoCierre) {
    this.motivoCierre = motivoCierre;
}

public void setColaVirtual(ColaVirtual colaVirtual) {
    this.colaVirtual = colaVirtual;
}

public void setRevisionesTecnicas(List<RevisionTecnica> revisionesTecnicas) {
    this.revisionesTecnicas = revisionesTecnicas;
}

public void setOperadoresResponsables(List<Operador> operadoresResponsables) {
    this.operadoresResponsables = operadoresResponsables;
}
}
