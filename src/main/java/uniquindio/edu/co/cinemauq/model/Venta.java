package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoVenta;

import java.time.LocalDateTime;

public class Venta {
    private int codigoVenta;
    private Cliente cliente;
    private Empleado empleado;
    private Funcion funcion;
    private Asiento asiento;
    private Entrada entrada;
    private LocalDateTime fechaHora;
    private double valorPagado;
    private EstadoVenta estado;

    public Venta(int codigoVenta, Cliente cliente, Empleado empleado, Funcion funcion, Asiento asiento, Entrada entrada) {
        this.codigoVenta = codigoVenta;
        this.cliente = cliente;
        this.empleado = empleado;
        this.funcion = funcion;
        this.asiento = asiento;
        this.entrada = entrada;
        this.fechaHora = LocalDateTime.now();
        this.valorPagado = calcularValorPagado();
        this.estado = EstadoVenta.ACTIVA;
    }

    public int getCodigoVenta() {
        return codigoVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    private double calcularValorPagado() {
        return entrada.calcularValor(funcion.getValorBase());
    }

    public boolean estaActiva() {
        return this.estado == EstadoVenta.ACTIVA;
    }

    public void cancelar() {
        this.estado = EstadoVenta.CANCELADA;
        this.asiento.liberar();
    }

    public String generarInformacionEntrada() {
        return "TICKET CINEMAUQ \n" +
                "Venta #: " + codigoVenta + "\n" +
                "Pelicula: " + funcion.getPelicula().getNombre() + "\n" +
                "Sala: " + funcion.getSala().getNombre() + "\n" +
                "Fecha/Hora Funcion: " + funcion.getFecha() + " " + funcion.getHora() + "\n" +
                "Asiento: " + asiento.getCodigoAsiento() + "\n" +
                "Cliente: " + cliente.getNombre() + "\n" +
                "Tipo Entrada: " + entrada.getNombre() + "\n" +
                "Valor Pagado: $" + valorPagado;
    }
}
