package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;

public class Pelicula {
    private int codigoPelicula;
    private String nombre;
    private String genero;
    private int duracion;
    private Clasificacion clasificacion;
    private EstadoPelicula estado;

    public Pelicula(int codigoPelicula, String nombre, String genero, int duracion, Clasificacion clasificacion, EstadoPelicula estado) {
        this.codigoPelicula = codigoPelicula;
        this.nombre = nombre;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
        this.estado = estado;
    }

    public int getCodigoPelicula() {
        return codigoPelicula;
    }

    public void setCodigoPelicula(int codigoPelicula) {
        this.codigoPelicula = codigoPelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public EstadoPelicula getEstado() {
        return estado;
    }

    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }

    public void modificar(String nombre, String genero, int duracion, Clasificacion clasificacion, EstadoPelicula estado) {
        this.nombre = nombre;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
        this.estado = estado;
    }

    public void deshabilitar() {
        this.estado = EstadoPelicula.NO_DISPONIBLE;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoPelicula.DISPONIBLE;
    }
}
