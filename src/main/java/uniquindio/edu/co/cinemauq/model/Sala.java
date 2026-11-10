package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoNoEncontradoException;

public class Sala {
    private int codigoSala;
    private String nombre;
    private int filas;
    private int columnas;
    private int capacidad;
    private EstadoSala estado;
    private Asiento[][] matrizAsientos;

    public Sala(int codigoSala, String nombre, int filas, int columnas, EstadoSala estado) {
        this.codigoSala = codigoSala;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.capacidad = filas * columnas;
        this.estado = estado;
        generarAsientos();
    }

    public int getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(int codigoSala) {
        this.codigoSala = codigoSala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }

    public Asiento[][] getMatrizAsientos() {
        return matrizAsientos;
    }

    private void generarAsientos() {
        this.matrizAsientos = new Asiento[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                this.matrizAsientos[i][j] = new Asiento(i + 1, j + 1);
            }
        }
    }

    public Asiento buscarAsiento(String codigoAsiento) throws AsientoNoEncontradoException {
        return Asiento.buscarEnMatriz(this.matrizAsientos, codigoAsiento);
    }

    public void deshabilitar() {
        this.estado = EstadoSala.FUERA_DE_SERVICIO;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoSala.DISPONIBLE;
    }
}
