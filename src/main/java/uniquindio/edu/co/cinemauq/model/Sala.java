package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoNoEncontradoException;

public class Sala {
    private String codigo;
    private String nombre;
    private int capacidad;
    private EstadoSala estado;
    // La distribucion fisica de la sala se gestiona mediante una matriz bidimensional [filas][columnas]
    private Asiento[][] asientos;

    public Sala(String codigo, String nombre, int filas, int columnas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = filas * columnas;
        this.estado = EstadoSala.DISPONIBLE;
        this.asientos = new Asiento[filas][columnas];
        inicializarAsientos(filas, columnas);
    }

    // Crea y posiciona los asientos en la matriz ordenados por filas (A, B, C...) y numero (1, 2, 3...)
    private void inicializarAsientos(int filas, int columnas) {
        for (int i = 0; i < filas; i++) {
            char letraFila = (char) ('A' + i);
            for (int j = 0; j < columnas; j++) {
                String codigoAsiento = letraFila + String.valueOf(j + 1);
                asientos[i][j] = new Asiento(codigoAsiento, i + 1, j + 1);
            }
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }

    public Asiento[][] getAsientos() {
        return asientos;
    }

    // Busca un asiento recorriendo la matriz bidimensional mediante sus indices [i][j]
    public Asiento buscarAsiento(String codigoAsiento) throws AsientoNoEncontradoException {
        for (int i = 0; i < asientos.length; i++) {
            for (int j = 0; j < asientos[i].length; j++) {
                if (asientos[i][j].getCodigo().equalsIgnoreCase(codigoAsiento)) {
                    return asientos[i][j];
                }
            }
        }
        throw new AsientoNoEncontradoException("El asiento " + codigoAsiento + " no existe en la sala.");
    }

    // Recorre la matriz para contar cuantos asientos estan disponibles
    public int contarAsientosDisponibles() {
        int contador = 0;
        for (int i = 0; i < asientos.length; i++) {
            for (int j = 0; j < asientos[i].length; j++) {
                if (asientos[i][j].getEstado() == EstadoAsiento.DISPONIBLE) {
                    contador++;
                }
            }
        }
        return contador;
    }

    // Recorre la matriz para contar cuantos asientos se encuentran ocupados
    public int contarAsientosOcupados() {
        int contador = 0;
        for (int i = 0; i < asientos.length; i++) {
            for (int j = 0; j < asientos[i].length; j++) {
                if (asientos[i][j].getEstado() == EstadoAsiento.OCUPADO) {
                    contador++;
                }
            }
        }
        return contador;
    }
}

