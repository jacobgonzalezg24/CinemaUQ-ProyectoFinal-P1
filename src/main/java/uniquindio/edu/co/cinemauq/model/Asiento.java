package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;

public class Asiento {
    private String codigo;
    private int fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(String codigo, int fila, int numero) {
        this.codigo = codigo;
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoAsiento.DISPONIBLE;
    }

    public void ocupar() {
        this.estado = EstadoAsiento.OCUPADO;
    }

    public void liberar() {
        this.estado = EstadoAsiento.DISPONIBLE;
    }
}
