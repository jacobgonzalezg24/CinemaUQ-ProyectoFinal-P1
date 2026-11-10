package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoVenta;
import uniquindio.edu.co.cinemauq.model.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private String nombre;
    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<Venta> ventas;
    private List<Cliente> clientes;
    private List<Empleado> empleados;

    public Cinema(String nombre) {
        this.nombre = nombre;
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    // --- GESTION DE PELICULAS ---
    public void registrarPelicula(Pelicula pelicula) throws PeliculaDuplicadaException {
        if (buscarPelicula(pelicula.getCodigo()) != null) {
            throw new PeliculaDuplicadaException("Ya existe una pelicula registrada con el codigo " + pelicula.getCodigo());
        }
        peliculas.add(pelicula);
    }

    public Pelicula buscarPelicula(String codigo) {
        for (Pelicula p : peliculas) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public Pelicula obtenerPelicula(String codigo) throws PeliculaNoEncontradaException {
        Pelicula pelicula = buscarPelicula(codigo);
        if (pelicula == null) {
            throw new PeliculaNoEncontradaException("La pelicula con codigo " + codigo + " no existe.");
        }
        return pelicula;
    }

    // --- GESTION DE SALAS ---
    public void registrarSala(Sala sala) throws SalaDuplicadaException {
        if (buscarSala(sala.getCodigo()) != null) {
            throw new SalaDuplicadaException("Ya existe una sala registrada con el codigo " + sala.getCodigo());
        }
        salas.add(sala);
    }

    public Sala buscarSala(String codigo) {
        for (Sala s : salas) {
            if (s.getCodigo().equalsIgnoreCase(codigo)) {
                return s;
            }
        }
        return null;
    }

    public Sala obtenerSala(String codigo) throws SalaNoEncontradaException {
        Sala sala = buscarSala(codigo);
        if (sala == null) {
            throw new SalaNoEncontradaException("La sala con codigo " + codigo + " no existe.");
        }
        return sala;
    }

    // --- GESTION DE FUNCIONES ---
    public void programarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha, LocalTime hora, double valorBase)
            throws PeliculaNoEncontradaException, SalaNoEncontradaException, SalaNoDisponibleException {
        Pelicula pelicula = obtenerPelicula(codigoPelicula);
        Sala sala = obtenerSala(codigoSala);
        Funcion funcion = new Funcion(codigo, pelicula, sala, fecha, hora, valorBase);
        funciones.add(funcion);
    }

    public Funcion buscarFuncion(String codigo) {
        for (Funcion f : funciones) {
            if (f.getCodigo().equalsIgnoreCase(codigo)) {
                return f;
            }
        }
        return null;
    }

    public Funcion obtenerFuncion(String codigo) throws FuncionNoEncontradaException {
        Funcion funcion = buscarFuncion(codigo);
        if (funcion == null) {
            throw new FuncionNoEncontradaException("La funcion con codigo " + codigo + " no existe.");
        }
        return funcion;
    }

    // --- VENTA Y CANCELACION DE ENTRADAS ---
    public Venta venderEntrada(String codigoVenta, Cliente cliente, String codigoFuncion, String codigoAsiento, Entrada entrada)
            throws FuncionNoEncontradaException, SinAsientosDisponiblesException, AsientoNoEncontradoException, AsientoOcupadoException {

        Funcion funcion = obtenerFuncion(codigoFuncion);
        Sala sala = funcion.getSala();

        if (sala.contarAsientosDisponibles() == 0) {
            throw new SinAsientosDisponiblesException("No hay asientos disponibles para la funcion seleccionada.");
        }

        Asiento asiento = sala.buscarAsiento(codigoAsiento);
        if (!asiento.estaDisponible()) {
            throw new AsientoOcupadoException("El asiento " + codigoAsiento + " ya se encuentra ocupado.");
        }

        Venta venta = new Venta(codigoVenta, cliente, funcion, asiento, entrada);
        ventas.add(venta);
        return venta;
    }

    public void cancelarVenta(String codigoVenta) throws VentaNoEncontradaException {
        Venta venta = null;
        for (Venta v : ventas) {
            if (v.getCodigo().equalsIgnoreCase(codigoVenta) && v.getEstado() == EstadoVenta.ACTIVA) {
                venta = v;
                break;
            }
        }

        if (venta == null) {
            throw new VentaNoEncontradaException("No se encontro una venta activa con el codigo " + codigoVenta);
        }

        venta.cancelarVenta();
    }

    // --- REPORTES ---
    public int calcularTotalEntradasVendidas() {
        int total = 0;
        for (Venta v : ventas) {
            if (v.getEstado() == EstadoVenta.ACTIVA) {
                total++;
            }
        }
        return total;
    }

    public double calcularTotalIngresos() {
        double total = 0;
        for (Venta v : ventas) {
            if (v.getEstado() == EstadoVenta.ACTIVA) {
                total += v.getValorPagado();
            }
        }
        return total;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}
