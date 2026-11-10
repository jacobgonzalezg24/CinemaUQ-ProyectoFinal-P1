package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private List<Pelicula> listaPeliculas;
    private List<Sala> listaSalas;
    private List<Funcion> listaFunciones;
    private List<Venta> listaVentas;
    private List<Cliente> listaClientes;
    private List<Empleado> listaEmpleados;
    private List<Entrada> listaTiposEntrada;

    private int contadorFuncion = 100;
    private int contadorVenta = 1000;

    public Cinema() {
        this.listaPeliculas = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaVentas = new ArrayList<>();
        this.listaClientes = new ArrayList<>();
        this.listaEmpleados = new ArrayList<>();
        this.listaTiposEntrada = new ArrayList<>();
        cargarTiposEntrada();
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public List<Sala> getListaSalas() {
        return listaSalas;
    }

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public List<Venta> getListaVentas() {
        return listaVentas;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public List<Entrada> getListaTiposEntrada() {
        return listaTiposEntrada;
    }

    private void cargarTiposEntrada() {
        listaTiposEntrada.add(new EntradaGeneral());
        listaTiposEntrada.add(new EntradaEstudiante(20.0));
        listaTiposEntrada.add(new EntradaNino(30.0));
        listaTiposEntrada.add(new EntradaAdultoMayor(25.0));
    }

    private int generarCodigoFuncion() {
        return ++contadorFuncion;
    }

    private int generarCodigoVenta() {
        return ++contadorVenta;
    }

    // --- PELICULAS ---
    public boolean verificarPelicula(int codigoPelicula) {
        for (Pelicula p : listaPeliculas) {
            if (p.getCodigoPelicula() == codigoPelicula) return true;
        }
        return false;
    }

    public void registrarPelicula(Pelicula pelicula) throws PeliculaDuplicadaException {
        if (verificarPelicula(pelicula.getCodigoPelicula())) {
            throw new PeliculaDuplicadaException("La pelicula ya esta registrada.");
        }
        listaPeliculas.add(pelicula);
    }

    public Pelicula buscarPelicula(int codigoPelicula) throws PeliculaNoEncontradaException {
        for (Pelicula p : listaPeliculas) {
            if (p.getCodigoPelicula() == codigoPelicula) return p;
        }
        throw new PeliculaNoEncontradaException("Pelicula no encontrada.");
    }

    public void modificarPelicula(int codigoPelicula, String nombre, String genero, int duracion, Clasificacion clasificacion, EstadoPelicula estado) throws PeliculaNoEncontradaException {
        Pelicula p = buscarPelicula(codigoPelicula);
        p.modificar(nombre, genero, duracion, clasificacion, estado);
    }

    public void deshabilitarPelicula(int codigoPelicula) throws PeliculaNoEncontradaException {
        Pelicula p = buscarPelicula(codigoPelicula);
        p.deshabilitar();
    }

    public List<Pelicula> consultarPeliculas() {
        return listaPeliculas;
    }

    public List<Pelicula> consultarPeliculasDisponibles() {
        List<Pelicula> dispon = new ArrayList<>();
        for (Pelicula p : listaPeliculas) {
            if (p.estaDisponible()) dispon.add(p);
        }
        return dispon;
    }

    // --- SALAS ---
    public boolean verificarSala(int codigoSala) {
        for (Sala s : listaSalas) {
            if (s.getCodigoSala() == codigoSala) return true;
        }
        return false;
    }

    public void registrarSala(Sala sala) throws SalaDuplicadaException {
        if (verificarSala(sala.getCodigoSala())) {
            throw new SalaDuplicadaException("La sala ya esta registrada.");
        }
        listaSalas.add(sala);
    }

    public Sala buscarSala(int codigoSala) throws SalaNoEncontradaException {
        for (Sala s : listaSalas) {
            if (s.getCodigoSala() == codigoSala) return s;
        }
        throw new SalaNoEncontradaException("Sala no encontrada.");
    }

    public void modificarSala(int codigoSala, String nombre, EstadoSala estado) throws SalaNoEncontradaException {
        Sala s = buscarSala(codigoSala);
        s.setNombre(nombre);
        s.setEstado(estado);
    }

    public void deshabilitarSala(int codigoSala) throws SalaNoEncontradaException {
        Sala s = buscarSala(codigoSala);
        s.deshabilitar();
    }

    public List<Sala> consultarSalas() {
        return listaSalas;
    }

    // --- CLIENTES ---
    public boolean verificarCliente(String identificacion) {
        for (Cliente c : listaClientes) {
            if (c.getIdentificacion().equalsIgnoreCase(identificacion)) return true;
        }
        return false;
    }

    public void registrarCliente(Cliente cliente) throws ClienteDuplicadoException {
        if (verificarCliente(cliente.getIdentificacion())) {
            throw new ClienteDuplicadoException("El cliente ya esta registrado.");
        }
        listaClientes.add(cliente);
    }

    public Cliente buscarCliente(String identificacion) throws ClienteNoEncontradoException {
        for (Cliente c : listaClientes) {
            if (c.getIdentificacion().equalsIgnoreCase(identificacion)) return c;
        }
        throw new ClienteNoEncontradoException("Cliente no encontrado.");
    }

    public List<Cliente> consultarClientes() {
        return listaClientes;
    }

    // --- EMPLEADOS ---
    public boolean verificarEmpleado(int codigoEmpleado) {
        for (Empleado e : listaEmpleados) {
            if (e.getCodigoEmpleado() == codigoEmpleado) return true;
        }
        return false;
    }

    public void registrarEmpleado(Empleado empleado) throws EmpleadoDuplicadoException {
        if (verificarEmpleado(empleado.getCodigoEmpleado())) {
            throw new EmpleadoDuplicadoException("El empleado ya esta registrado.");
        }
        listaEmpleados.add(empleado);
    }

    public Empleado buscarEmpleado(int codigoEmpleado) throws EmpleadoNoEncontradoException {
        for (Empleado e : listaEmpleados) {
            if (e.getCodigoEmpleado() == codigoEmpleado) return e;
        }
        throw new EmpleadoNoEncontradoException("Empleado no encontrado.");
    }

    public List<Empleado> consultarEmpleados() {
        return listaEmpleados;
    }

    // --- ENTRADAS ---
    public Entrada buscarTipoEntrada(int codigoEntrada) throws TipoEntradaNoEncontradaException {
        for (Entrada e : listaTiposEntrada) {
            if (e.getCodigoEntrada() == codigoEntrada) return e;
        }
        throw new TipoEntradaNoEncontradaException("Tipo de entrada no encontrado.");
    }

    public void modificarDescuento(int codigoEntrada, double porcentajeDescuento) throws TipoEntradaNoEncontradaException {
        Entrada e = buscarTipoEntrada(codigoEntrada);
        e.setPorcentajeDescuento(porcentajeDescuento);
    }

    public List<Entrada> consultarTiposEntrada() {
        return listaTiposEntrada;
    }

    // --- FUNCIONES ---
    public boolean verificarFuncion(int codigoFuncion) {
        for (Funcion f : listaFunciones) {
            if (f.getCodigoFuncion() == codigoFuncion) return true;
        }
        return false;
    }

    public Funcion programarFuncion(int codigoPelicula, int codigoSala, LocalDate fecha, LocalTime hora, double valorBase) throws CinemaException {
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        Sala sala = buscarSala(codigoSala);

        if (!sala.estaDisponible()) {
            throw new SalaNoDisponibleException("La sala seleccionada se encuentra fuera de servicio.");
        }

        int codigoFuncion = generarCodigoFuncion();
        Funcion funcion = new Funcion(codigoFuncion, pelicula, sala, fecha, hora, valorBase);
        listaFunciones.add(funcion);
        return funcion;
    }

    public Funcion buscarFuncion(int codigoFuncion) throws FuncionNoEncontradaException {
        for (Funcion f : listaFunciones) {
            if (f.getCodigoFuncion() == codigoFuncion) return f;
        }
        throw new FuncionNoEncontradaException("Funcion no encontrada.");
    }

    public void cancelarFuncion(int codigoFuncion) throws FuncionNoEncontradaException {
        Funcion f = buscarFuncion(codigoFuncion);
        f.setEstado(uniquindio.edu.co.cinemauq.model.enums.EstadoFuncion.CANCELADA);
    }

    public void finalizarFuncion(int codigoFuncion) throws FuncionNoEncontradaException {
        Funcion f = buscarFuncion(codigoFuncion);
        f.setEstado(uniquindio.edu.co.cinemauq.model.enums.EstadoFuncion.FINALIZADA);
    }

    public List<Funcion> consultarFuncionesProgramadas() {
        List<Funcion> prog = new ArrayList<>();
        for (Funcion f : listaFunciones) {
            if (f.estaProgramada()) prog.add(f);
        }
        return prog;
    }

    public List<Funcion> consultarFuncionesPorPelicula(int codigoPelicula) {
        List<Funcion> res = new ArrayList<>();
        for (Funcion f : listaFunciones) {
            if (f.getPelicula().getCodigoPelicula() == codigoPelicula) {
                res.add(f);
            }
        }
        return res;
    }

    public String consultarDisponibilidad(int codigoFuncion) throws FuncionNoEncontradaException {
        Funcion f = buscarFuncion(codigoFuncion);
        return f.mostrarMapaAsientos();
    }

    // --- VENTAS ---
    public boolean verificarVenta(int codigoVenta) {
        for (Venta v : listaVentas) {
            if (v.getCodigoVenta() == codigoVenta) return true;
        }
        return false;
    }

    public Venta venderEntrada(String identificacionCliente, int codigoEmpleado, int codigoFuncion, int codigoEntrada, String codigoAsiento) throws CinemaException {
        Cliente cliente = buscarCliente(identificacionCliente);
        Empleado empleado = buscarEmpleado(codigoEmpleado);
        Funcion funcion = buscarFuncion(codigoFuncion);

        if (!funcion.hayDisponibles()) {
            throw new SinAsientosDisponiblesException("No hay asientos disponibles en esta funcion.");
        }

        Entrada entrada = buscarTipoEntrada(codigoEntrada);
        Asiento asiento = funcion.buscarAsiento(codigoAsiento);

        // Ocupa el asiento en la matriz de la funcion
        funcion.ocuparAsiento(codigoAsiento);

        int codigoVenta = generarCodigoVenta();
        Venta venta = new Venta(codigoVenta, cliente, empleado, funcion, asiento, entrada);
        listaVentas.add(venta);
        return venta;
    }

    public void cancelarVenta(int codigoVenta) throws VentaNoEncontradaException {
        Venta v = buscarVenta(codigoVenta);
        v.cancelar();
    }

    public Venta buscarVenta(int codigoVenta) throws VentaNoEncontradaException {
        for (Venta v : listaVentas) {
            if (v.getCodigoVenta() == codigoVenta) return v;
        }
        throw new VentaNoEncontradaException("Venta no encontrada.");
    }

    public List<Venta> consultarVentas() {
        return listaVentas;
    }

    public List<Venta> consultarVentasPorCliente(String identificacion) throws ClienteNoEncontradoException {
        buscarCliente(identificacion);
        List<Venta> res = new ArrayList<>();
        for (Venta v : listaVentas) {
            if (v.getCliente().getIdentificacion().equalsIgnoreCase(identificacion)) {
                res.add(v);
            }
        }
        return res;
    }

    public List<Venta> consultarVentasPorEmpleado(int codigoEmpleado) throws EmpleadoNoEncontradoException {
        buscarEmpleado(codigoEmpleado);
        List<Venta> res = new ArrayList<>();
        for (Venta v : listaVentas) {
            if (v.getEmpleado().getCodigoEmpleado() == codigoEmpleado) {
                res.add(v);
            }
        }
        return res;
    }

    // --- REPORTES ---
    public int totalEntradasVendidas() {
        int total = 0;
        for (Venta v : listaVentas) {
            if (esVentaContable(v)) total++;
        }
        return total;
    }

    public double totalIngresos() {
        double total = 0;
        for (Venta v : listaVentas) {
            if (esVentaContable(v)) total += v.getValorPagado();
        }
        return total;
    }

    public Pelicula peliculaMasVendida() {
        Pelicula masVendida = null;
        int maxVentas = -1;

        for (Pelicula p : listaPeliculas) {
            int ventasPeli = contarEntradasVendidasPorPelicula(p.getCodigoPelicula());
            if (ventasPeli > maxVentas) {
                maxVentas = ventasPeli;
                masVendida = p;
            }
        }
        return masVendida;
    }

    public String ocupacionFuncion(int codigoFuncion) throws FuncionNoEncontradaException {
        Funcion f = buscarFuncion(codigoFuncion);
        return "Funcion #" + f.getCodigoFuncion() + " | Pelicula: " + f.getPelicula().getNombre() +
                "\nTotales: " + f.contarTotales() +
                " | Disponibles: " + f.contarDisponibles() +
                " | Ocupados: " + f.contarOcupados();
    }

    public List<Funcion> funcionesPorFecha(LocalDate fecha) {
        List<Funcion> res = new ArrayList<>();
        for (Funcion f : listaFunciones) {
            if (f.getFecha().equals(fecha)) {
                res.add(f);
            }
        }
        return res;
    }

    private int contarEntradasVendidasPorPelicula(int codigoPelicula) {
        int contador = 0;
        for (Venta v : listaVentas) {
            if (esVentaContable(v) && v.getFuncion().getPelicula().getCodigoPelicula() == codigoPelicula) {
                contador++;
            }
        }
        return contador;
    }

    private boolean esVentaContable(Venta venta) {
        return venta != null && venta.estaActiva();
    }
}
