package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import java.util.ArrayList;
import java.util.List;

public class Sala {
    private String codigoSala;
    private String nombre;
    private int numeroFilas;
    private int numeroColumnas;
    private EstadoSala estado;
    private List<Asiento> asientos;

    public Sala(String codigoSala, String nombre, int numeroFilas, int numeroColumnas, EstadoSala estado) {
        this.codigoSala = codigoSala;
        this.nombre = nombre;
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.estado = estado;
        this.asientos = new ArrayList<>();
        generarMatrizAsientos();
    }

    public void generarMatrizAsientos() {
        this.asientos.clear();
        for (int i = 0; i < numeroFilas; i++) {
            char fila = (char) ('A' + i);
            for (int j = 1; j <= numeroColumnas; j++) {
                this.asientos.add(new Asiento(fila, j));
            }
        }
    }

    public int getCapacidadTotal() {
        return numeroFilas * numeroColumnas;
    }

    // Getters y Setters
    public String getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala) {
        this.codigoSala = codigoSala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroFilas() {
        return numeroFilas;
    }

    public void setNumeroFilas(int numeroFilas) {
        this.numeroFilas = numeroFilas;
        generarMatrizAsientos();
    }

    public int getNumeroColumnas() {
        return numeroColumnas;
    }

    public void setNumeroColumnas(int numeroColumnas) {
        this.numeroColumnas = numeroColumnas;
        generarMatrizAsientos();
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }
}