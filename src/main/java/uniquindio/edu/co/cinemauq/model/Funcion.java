package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.enums.EstadoFuncion;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoNoEncontradoException;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoOcupadoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Funcion {
    private int codigoFuncion;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double valorBase;
    private EstadoFuncion estado;
    private Asiento[][] matrizAsientos;

    public Funcion(int codigoFuncion, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase) {
        this.codigoFuncion = codigoFuncion;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.valorBase = valorBase;
        this.estado = EstadoFuncion.PROGRAMADA;
        generarMatrizAsientos();
    }

    public int getCodigoFuncion() {
        return codigoFuncion;
    }

    public void setCodigoFuncion(int codigoFuncion) {
        this.codigoFuncion = codigoFuncion;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public void setEstado(EstadoFuncion estado) {
        this.estado = estado;
    }

    public Asiento[][] getMatrizAsientos() {
        return matrizAsientos;
    }

    private void generarMatrizAsientos() {
        int filas = sala.getFilas();
        int columnas = sala.getColumnas();
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

    public void ocuparAsiento(String codigoAsiento) throws AsientoNoEncontradoException, AsientoOcupadoException {
        Asiento asiento = buscarAsiento(codigoAsiento);
        asiento.ocupar();
    }

    public void liberarAsiento(String codigoAsiento) throws AsientoNoEncontradoException {
        Asiento asiento = buscarAsiento(codigoAsiento);
        asiento.liberar();
    }

    public int contarTotales() {
        return sala.getCapacidad();
    }

    public int contarDisponibles() {
        int contador = 0;
        for (int i = 0; i < matrizAsientos.length; i++) {
            for (int j = 0; j < matrizAsientos[i].length; j++) {
                if (matrizAsientos[i][j].estaDisponible()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public int contarOcupados() {
        return contarTotales() - contarDisponibles();
    }

    public boolean hayDisponibles() {
        return contarDisponibles() > 0;
    }

    public List<Asiento> obtenerAsientosDisponibles() {
        List<Asiento> disponibles = new ArrayList<>();
        for (int i = 0; i < matrizAsientos.length; i++) {
            for (int j = 0; j < matrizAsientos[i].length; j++) {
                if (matrizAsientos[i][j].estaDisponible()) {
                    disponibles.add(matrizAsientos[i][j]);
                }
            }
        }
        return disponibles;
    }

    public String mostrarMapaAsientos() {
        StringBuilder mapa = new StringBuilder();
        for (int i = 0; i < matrizAsientos.length; i++) {
            for (int j = 0; j < matrizAsientos[i].length; j++) {
                Asiento a = matrizAsientos[i][j];
                mapa.append(a.getCodigoAsiento())
                        .append(" (")
                        .append(a.getEstado())
                        .append(")  ");
            }
            mapa.append("\n");
        }
        return mapa.toString();
    }

    public boolean estaProgramada() {
        return this.estado == EstadoFuncion.PROGRAMADA;
    }
}

