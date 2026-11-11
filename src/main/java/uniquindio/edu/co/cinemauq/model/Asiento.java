package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoNoEncontradoException;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoOcupadoException;

public class Asiento {
    private String codigoAsiento;
    private char fila;
    private int columna;
    private EstadoAsiento estado;

    public Asiento(char fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.codigoAsiento = generarCodigo(fila, columna);
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    private String generarCodigo(char fila, int columna) {
        return String.valueOf(fila) + columna;
    }

    public String getCodigoAsiento() {
        return codigoAsiento;
    }

    public void setCodigoAsiento(String codigoAsiento) {
        this.codigoAsiento = codigoAsiento;
    }

    public char getFila() {
        return fila;
    }

    public void setFila(char fila) {
        this.fila = fila;
        this.codigoAsiento = generarCodigo(this.fila, this.columna);
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
        this.codigoAsiento = generarCodigo(this.fila, this.columna);
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    public void ocupar() throws AsientoOcupadoException {
        if (!estaDisponible()) {
            throw new AsientoOcupadoException("El asiento " + codigoAsiento + " ya se encuentra ocupado.");
        }
        this.estado = EstadoAsiento.OCUPADO;
    }

    public void liberar() {
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoAsiento.DISPONIBLE;
    }

    public static Asiento buscarEnMatriz(Asiento[][] matriz, String codigoAsiento) throws AsientoNoEncontradoException {
        if (matriz != null) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    if (matriz[i][j] != null && matriz[i][j].getCodigoAsiento().equalsIgnoreCase(codigoAsiento)) {
                        return matriz[i][j];
                    }
                }
            }
        }
        throw new AsientoNoEncontradoException("El asiento con codigo " + codigoAsiento + " no existe.");
    }
}