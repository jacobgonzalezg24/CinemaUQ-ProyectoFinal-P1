package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoNoEncontradoException;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoOcupadoException;

public class Asiento {
    private String codigoAsiento;
    private int fila;
    private int columna;
    private EstadoAsiento estado;

    public Asiento(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.codigoAsiento = generarCodigo(fila, columna);
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public String getCodigoAsiento() {
        return codigoAsiento;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    private String generarCodigo(int fila, int columna) {
        char letraFila = (char) ('A' + (fila - 1));
        return letraFila + String.valueOf(columna);
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
